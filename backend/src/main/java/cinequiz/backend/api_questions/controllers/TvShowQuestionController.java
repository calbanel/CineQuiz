package cinequiz.backend.api_questions.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@RequestMapping(value="/cinequiz/questions/tv-show", method = RequestMethod.GET)
public class TvShowQuestionController {

    @GetMapping("/")
    public String random_question() {
        return "tv-show";
    }
}