package cinequiz.backend.api_questions.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value="/cinequiz/questions/tv-show", method = RequestMethod.GET)
public class TvShowQuestionController {

    @ApiOperation(value = "Gets a random mcq about a tv show")
    @GetMapping("/")
    public String random_question() {
        return "tv-show";
    }
}