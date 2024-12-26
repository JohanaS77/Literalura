package com.aluracursos.literalura.service;

import com.aluracursos.literalura.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class ConsoleMenu {
    @Autowired
    private BookService bookService;
    private final Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        boolean running = true;
        try {
            //while (true) {
            while (running) {
                displayMenuOptions();
                int option = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer

                switch (option) {
                    case 1:
                        searchBook();
                        break;
                    case 2:
                        listBooks();
                        break;
                    case 3:
                        listAuthors();
                        break;
                    case 4:
                        listAuthorsAliveInYear();
                        break;
                    case 5:
                        listBooksByLanguage();
                        break;
                    case 0:
                        System.out.println("Saliendo del programa...");
                        running = false;
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opción no válida, intente nuevamente.");
                }
            }
             } finally {
             scanner.close();
        }
    }

    private void displayMenuOptions() {
        System.out.println("------Elija la opción a través de su número------");
        System.out.println("1. Buscar libro por título");
        System.out.println("2. Listar libros registrados");
        System.out.println("3. Listar autores registrados");
        System.out.println("4. Listar autores vivos en un determinado año");
        System.out.println("5. Listar libros por idioma");
        System.out.println("0. Salir");
    }

    private void searchBook() {
        System.out.println("Ingrese el nombre del libro que desea buscar:");
        String title = scanner.nextLine();
        Optional<BookDTO> bookDTO = bookService.searchBookByTitle(title);
        if (bookDTO.isPresent()) {
            System.out.println("Libro encontrado y guardado en la base de datos.");
            printBookDetails(bookDTO.get());
        } else {
            System.out.println("Libro no encontrado.");
        }
    }

    private void listBooks() {
        List<BookDTO> books = bookService.listAllBooks();
        books.forEach(this::printBookDetails);
    }

    private void listAuthors() {
        List<BookDTO> allBooks = bookService.listAllBooks();
        if (allBooks.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            System.out.println("Detalles de los autores:\n");
            allBooks.stream()
                    .collect(Collectors.groupingBy(BookDTO::getAuthor))
                    .forEach(this::printAuthorDetails);
        }
    }

    private void listAuthorsAliveInYear() {
        System.out.print("Ingrese el año: ");
        int year = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Map<String, List<BookDTO>> authorsDetailsMap = bookService.listAuthorsDetailsAliveInYear(year);
        if (authorsDetailsMap.isEmpty()) {
            System.out.println("No hay autores vivos en el año especificado.");
        } else {
            System.out.println("Detalles de los autores vivos en el año " + year + ":\n");
            authorsDetailsMap.forEach(this::printAuthorBooks);
        }
    }

    private void listBooksByLanguage() {
        System.out.println("Ingrese el idioma (ES, EN, FR, PT):");
        String language = scanner.nextLine();
        List<BookDTO> booksByLanguage = bookService.listBooksByLanguage(language);
        booksByLanguage.forEach(this::printBookDetails);
    }

    private void printBookDetails(BookDTO book) {
        System.out.println("Detalles del libro:");
        System.out.println("  Título: " + book.getTitle());
        System.out.println("  Autor: " + book.getAuthor());
        System.out.println("  Fecha de nacimiento del autor: " +
                (book.getBirth_year() != null ? book.getBirth_year() : "No disponible"));
        System.out.println("  Fecha de fallecimiento del autor: " +
                (book.getDeath_year() != null ? book.getDeath_year() : "No disponible"));
        System.out.println("  Idioma: " + book.getLanguage());
        System.out.println("  Descargas: " + book.getDownload_count());
        System.out.println("--------------------");
    }

    private void printAuthorDetails(String author, List<BookDTO> books) {
        books.forEach(book -> {
            System.out.println("  Autor: " + book.getAuthor());
            System.out.println("  Título del libro: " + book.getTitle());
            System.out.println("  Fecha de nacimiento del autor: " + book.getBirth_year());
            System.out.println("  Fecha de fallecimiento del autor: " + book.getDeath_year());
            System.out.println("  Descargas: " + book.getDownload_count());
            System.out.println("-----------------------------------------------------------");
        });
    }

    private void printAuthorBooks(String author, List<BookDTO> books) {
        System.out.println("  Autor: " + author);
        books.forEach(book -> {
            System.out.println("  Título del libro: " + book.getTitle());
            System.out.println("  Fecha de nacimiento del autor: " + book.getBirth_year());
            System.out.println("  Fecha de fallecimiento del autor: " + book.getDeath_year());
            System.out.println("  Descargas: " + book.getDownload_count());
            System.out.println("-----------------------------------------------------------");
        });
    }
}



