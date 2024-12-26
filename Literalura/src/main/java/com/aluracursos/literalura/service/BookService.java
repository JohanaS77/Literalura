package com.aluracursos.literalura.service;

import com.aluracursos.literalura.dto.BookDTO;
import com.aluracursos.literalura.model.Book;
import com.aluracursos.literalura.repository.BookRepository;
import com.aluracursos.literalura.mapper.BookMapper;
import com.aluracursos.literalura.response.BookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final RestTemplate restTemplate;
    private final String apiUrl;

    @Autowired
    public BookService(
            BookRepository bookRepository,
            BookMapper bookMapper,
            RestTemplate restTemplate,
            @Value("${api.gutendex.url:https://gutendex.com/books/}") String apiUrl) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
    }

    public Optional<BookDTO> searchBookByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }

        try {
            // Buscar primero en base de datos
            List<Book> existingBooks = bookRepository.findByTitleContaining(title);
            if (!existingBooks.isEmpty()) {
                Book book = existingBooks.get(0);
                if (!book.isDownloaded()) {
                    book.setDownloaded(true);
                    book.setDownload_count(book.getDownload_count() + 1);
                    bookRepository.save(book);
                    logger.info("Libro encontrado en base de datos y marcado como descargado: {}", title);
                }
                return Optional.of(bookMapper.toDTO(book));
            }

            // Buscar en API externa
            String encodedUrl = apiUrl + "?search=" + title.replace(" ", "%20");
            logger.debug("URL de búsqueda: {}", encodedUrl);
            ResponseEntity<BookResponse> response = restTemplate.getForEntity(encodedUrl, BookResponse.class);
            logger.debug("Respuesta de la API: {}", response.getBody());

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null
                    && response.getBody().getResults() != null
                    && !response.getBody().getResults().isEmpty()) {
                BookResponse.BookDTO apiBookDTO = response.getBody().getResults().get(0);
                BookDTO newBookDTO = convertApiResponseToBookDTO(apiBookDTO);
                //newBookDTO.setDownloaded(true); // Marcamos como descargado
                // Usar el contador de descargas de la API si está disponible

                // Incrementar contador de descargas
                int downloads = apiBookDTO.getDownloadCount();
                newBookDTO.setDownload_count(downloads > 0 ? downloads : 1);

                // Guardar en base de datos
                Book savedBook = bookRepository.save(bookMapper.toEntity(newBookDTO));
                logger.info("Libro guardado con download_count: {}", savedBook.getDownload_count());

//                if (apiBookDTO.getDownloadCount() > 0) {
//                    newBookDTO.setDownload_count(apiBookDTO.getDownloadCount());
//                } else {
//                    newBookDTO.setDownload_count(newBookDTO.getDownload_count() + 1); // Incrementamos el contador
//                }
//
//                // Guardar en base de datos
//                Book savedBook = bookRepository.save(bookMapper.toEntity(newBookDTO));
//                logger.info("Nuevo libro guardado: {}", savedBook.getTitle());

                return Optional.of(bookMapper.toDTO(savedBook));
            }

            logger.warn("Libro no encontrado: {}", title);
            return Optional.empty();

        } catch (RestClientException e) {
            logger.error("Error al consultar la API externa: {}", e.getMessage());
            throw new RuntimeException("Error al buscar el libro en la API externa", e);
        } catch (Exception e) {
            logger.error("Error inesperado: {}", e.getMessage());
            throw new RuntimeException("Error inesperado al procesar la búsqueda del libro", e);
        }
    }

    private BookDTO convertApiResponseToBookDTO(BookResponse.BookDTO apiBookDTO) {
        BookDTO newBookDTO = new BookDTO();
        newBookDTO.setTitle(apiBookDTO.getTitle());
        //newBookDTO.setDownload_count(apiBookDTO.getDownloadCount());

        logger.debug("Procesando libro: {}", apiBookDTO.getTitle());
        //logger.debug("Download count recibido: {}", apiBookDTO.getDownloadCount());

        if (apiBookDTO.getAuthors() != null && !apiBookDTO.getAuthors().isEmpty()) {
            BookResponse.BookDTO.Author firstAuthor = apiBookDTO.getAuthors().get(0);
            newBookDTO.setAuthor(firstAuthor.getName());

            // Agregar logs de depuración
            logger.debug("Datos del autor recibidos de la API:");
            logger.debug("Nombre: {}", firstAuthor.getName());
            logger.debug("Año de nacimiento: {}", firstAuthor.getBirthYear());
            logger.debug("Año de muerte: {}", firstAuthor.getDeathYear());

            // Manejo de birth_year
            String birthYear = firstAuthor.getBirthYear();
            if (birthYear != null && !birthYear.isEmpty()) {
                newBookDTO.setBirth_year(birthYear);
                logger.debug("Año de nacimiento asignado: {}", birthYear);
            } else {
                newBookDTO.setBirth_year("No disponible");
                logger.debug("Año de nacimiento no disponible");
            }

            // Manejo de death_year
            String deathYear = firstAuthor.getDeathYear();
            if (deathYear != null && !deathYear.isEmpty()) {
                newBookDTO.setDeath_year(deathYear);
                logger.debug("Año de muerte asignado: {}", deathYear);
            } else {
                newBookDTO.setDeath_year("No disponible");
                logger.debug("Año de muerte no disponible");
            }

            // Logging para depuración
            logger.debug("Autor: {}", firstAuthor.getName());
            logger.debug("Año nacimiento: {}", firstAuthor.getBirthYear());
            logger.debug("Año muerte: {}", firstAuthor.getDeathYear());

            // Corregir el manejo de birth_year y death_year
            newBookDTO.setBirth_year(firstAuthor.getBirthYear());
            newBookDTO.setDeath_year(firstAuthor.getDeathYear());


            // Manejo de birth_year
//            String birthYear = firstAuthor.getBirthYear();
//            if (birthYear != null && !birthYear.isEmpty()) {
//                newBookDTO.setBirth_year(birthYear);
//                logger.debug("Año de nacimiento asignado: {}", birthYear);
//            }

            // Manejo de death_year
//            String deathYear = firstAuthor.getDeathYear();
//            if (deathYear != null && !deathYear.isEmpty()) {
//                newBookDTO.setDeath_year(deathYear);
//                logger.debug("Año de muerte asignado: {}", deathYear);
//            }
       }

        if (apiBookDTO.getLanguages() != null && !apiBookDTO.getLanguages().isEmpty()) {
            newBookDTO.setLanguage(apiBookDTO.getLanguages().get(0).toUpperCase());
        }

        newBookDTO.setDownload_count(apiBookDTO.getDownloadCount());

//        int downloadCount = apiBookDTO.getDownloadCount();
//        newBookDTO.setDownload_count(downloadCount);
//        logger.debug("Download count asignado: {}", downloadCount);

        return newBookDTO;
    }

    public List<BookDTO> listAllBooks() {
        List<Book> books = bookRepository.findAll();
        logger.info("Recuperados {} libros de la base de datos", books.size());
        return books.stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<String> listAllAuthors() {
        return bookRepository.findAll().stream()
                .map(Book::getAuthor)
                .filter(Objects::nonNull)
                .filter(author -> !author.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    public Map<String, List<BookDTO>> listAuthorsDetailsAliveInYear(int year) {
        if (year < 0) {
            throw new IllegalArgumentException("El año debe ser un valor positivo");
        }

        return bookRepository.findAll().stream()
                .filter(book -> isAuthorAliveInYear(book, year))
                .collect(Collectors.groupingBy(
                        Book::getAuthor,
                        Collectors.mapping(bookMapper::toDTO, Collectors.toList())
                ));
    }

    private boolean isAuthorAliveInYear(Book book, int year) {
        try {
            if (book.getBirth_year() == null || book.getBirth_year().isEmpty()) {
                return false;
            }

            int birthYear = Integer.parseInt(book.getBirth_year());
            int deathYear = Optional.ofNullable(book.getDeath_year())
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .orElse(Integer.MAX_VALUE);

            return birthYear <= year && year <= deathYear;
        } catch (NumberFormatException e) {
            logger.warn("Error al parsear año para el autor {}: {}",
                    book.getAuthor(), e.getMessage());
            return false;
        }
    }

    public List<BookDTO> listBooksByLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            throw new IllegalArgumentException("El idioma no puede estar vacío");
        }

        List<Book> books = bookRepository.findByLanguage(language.toUpperCase());
        logger.info("Encontrados {} libros en el idioma {}", books.size(), language);
        return books.stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BookDTO saveBook(BookDTO bookDTO) {
        if (bookDTO == null) {
            throw new IllegalArgumentException("El libro no puede ser null");
        }

        if (bookDTO.getTitle() == null || bookDTO.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("El título del libro no puede estar vacío");
        }

        if (bookRepository.existsByTitle(bookDTO.getTitle())) {
            throw new RuntimeException("El libro ya existe en la base de datos");
        }

        try {
            Book book = bookMapper.toEntity(bookDTO);
            Book savedBook = bookRepository.save(book);
            logger.info("Libro guardado exitosamente: {}", savedBook.getTitle());
            return bookMapper.toDTO(savedBook);
        } catch (Exception e) {
            logger.error("Error al guardar el libro: {}", e.getMessage());
            throw new RuntimeException("Error al guardar el libro en la base de datos", e);
        }
    }
    public void testApiResponse(String title) {
        String encodedUrl = apiUrl + "?search=" + title.replace(" ", "%20");
        ResponseEntity<BookResponse> response = restTemplate.getForEntity(encodedUrl, BookResponse.class);

        if (response.getBody() != null && !response.getBody().getResults().isEmpty()) {
            BookResponse.BookDTO apiBook = response.getBody().getResults().get(0);
            if (apiBook.getAuthors() != null && !apiBook.getAuthors().isEmpty()) {
                BookResponse.BookDTO.Author author = apiBook.getAuthors().get(0);
                System.out.println("Datos directos de la API:");
                System.out.println("Autor: " + author.getName());
                System.out.println("Año de nacimiento: " + author.getBirthYear());
                System.out.println("Año de muerte: " + author.getDeathYear());
            }
        }
    }
}





