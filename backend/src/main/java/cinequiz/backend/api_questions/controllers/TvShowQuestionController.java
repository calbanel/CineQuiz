package cinequiz.backend.api_questions.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questions/tv-show")
public class TvShowQuestionController {

    @GetMapping("/")
    public String random_question() {
        return "tv-show";
    }
}