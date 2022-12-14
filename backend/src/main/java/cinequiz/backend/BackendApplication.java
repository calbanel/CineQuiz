package cinequiz.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

// import cinequiz.backend.repository.UserRepository;

@SpringBootApplication
// @EnableMongoRepositories(basePackageClasses=UserRepository.class)
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	public static final String API_KEY = "c7d238dde9b0efbe8deb61921ee13f06";
	public static final String IMG_URL_BASE = "https://image.tmdb.org/t/p/w500";

	public static int random(int min, int max) {
		return (int) (min + (Math.random() * ((max + 1) - min)));
	}

}
