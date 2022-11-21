package cinequiz.backend.api_questions.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value="/cinequiz/questions/people", method = RequestMethod.GET)
public class PeopleQuestionController {
    
    @ApiOperation(value = "Gets a random mcq about a person")
    @GetMapping("/")
    public String random_question() {
        return "people";
    }
}