package com.aluracursos.literalura;

import com.aluracursos.literalura.service.ConsoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class LiteraluraApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Component
	public class ConsoleRunner implements CommandLineRunner {
		private final ConsoleMenu consoleMenu;

		@Autowired
		public ConsoleRunner(ConsoleMenu consoleMenu) {
			this.consoleMenu = consoleMenu;
		}

		@Override
		public void run(String... args) {
			consoleMenu.showMenu();
		}
	}
}






