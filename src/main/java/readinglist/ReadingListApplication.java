package readinglist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication // This annotation enables component scanning and JPA configuration
@EnableJpaRepositories

public class ReadingListApplication {
	public static void main(String[] args) {
		try {
			SpringApplication.run(ReadingListApplication.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}