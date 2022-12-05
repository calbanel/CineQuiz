package cinequiz.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	public static final String API_KEY = "c7d238dde9b0efbe8deb61921ee13f06";

	public static int random(int min, int max) {
		return (int) (min + (Math.random() * ((max + 1) - min)));
	}

}
