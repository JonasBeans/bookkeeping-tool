package be.jonasboon.book_keeping_tool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class BookKeepingToolApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookKeepingToolApplication.class, args);
	}

}
