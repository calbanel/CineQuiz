package cinequiz.backend.api_questions.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinequiz.backend.api_questions.mcq.*;
import cinequiz.backend.api_questions.questions.MovieQuestion;

@RestController
@RequestMapping("/questions/movie")
public class MovieQuestionController {

    @GetMapping("/")
    public String random_question() {
        return "movie";
    }

    @GetMapping(value = "/which-by-image", produces = { "application/json" })
    public ResponseEntity<MCQQuestion> which_by_image() {
        Choices choices = new Choices("Titanic", "Seven", "Charlie et la Chocolaterie", "Django Unchained");
        MCQQuestion mcq = new MCQQuestion("blabla.jpg", "", MovieQuestion.WHICH_BY_IMAGE.getQuestion(), choices,
                "Titanic");

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }

}