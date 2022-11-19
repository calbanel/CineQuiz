package cinequiz.backend.api_questions.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@RequestMapping(value="/cinequiz/questions/people", method = RequestMethod.GET)
public class PeopleQuestionController {

    @GetMapping("/")
    public String random_question() {
        return "people";
    }
}