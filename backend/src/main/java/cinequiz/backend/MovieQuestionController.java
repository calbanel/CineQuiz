package cinequiz.backend;

import javax.print.attribute.standard.Media;

import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questions/movie")
public class MovieQuestionController {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @GetMapping(value = "/which-by-image", produces = { "application/json" })
    public ResponseEntity<MCQQuestion> which_by_image() {
        Choices choices = new Choices("Titanic", "Seven", "Charlie et la Chocolaterie", "Django Unchained");
        MCQQuestion mcq = new MCQQuestion("blabla.jpg", MovieQuestion.WHICH_BY_IMAGE.getQuestion(), choices, "Titanic");

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }

}