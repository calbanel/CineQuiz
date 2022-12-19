package cinequiz.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	public static int random(int min, int max) {
		return (int) (min + (Math.random() * ((max + 1) - min)));
	}

}
