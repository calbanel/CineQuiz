package cinequiz.backend.api_questions.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinequiz.backend.BackendApplication;
import cinequiz.backend.api_questions.schemas.MCQQuestion;
import cinequiz.backend.api_questions.utils.Language;
import cinequiz.backend.api_questions.utils.MCQStrategy;
import cinequiz.backend.api_questions.utils.MovieStrategy;
import cinequiz.backend.api_questions.utils.PersonStrategy;
import cinequiz.backend.api_questions.utils.TvStrategy;
import cinequiz.backend.api_questions.utils.exceptions.LanguageNotSupportedException;
import cinequiz.backend.api_questions.utils.exceptions.TypeNotSupportedException;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/questions", method = RequestMethod.GET)
public class QuestionController {

    private final int NB_DEFINED_QUESTIONS = 1;

    @ApiOperation(value = "Gets a random mcq")
    @GetMapping("/random")
    public ResponseEntity<?> random_question(
            @RequestParam(required = false, value = "type", defaultValue = "random") String type,
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        int randomQuestion = BackendApplication.random(1, NB_DEFINED_QUESTIONS);
        switch (randomQuestion) {
            case 1:
                return whichByImage(type, language);
            default:
                return whichByImage(type, language);

        }
    }

    private class CleanParams {
        private Language language;
        private InfosType type;

        public CleanParams(String language, String type)
                throws LanguageNotSupportedException, TypeNotSupportedException {
            this.language = Language.languageCheck(language);

            if (type.equals("random"))
                this.type = InfosType.getRandomType();
            else {
                this.type = InfosType.typeCheck(type);
            }
        }

        public Language getLanguage() {
            return language;
        }

        public InfosType getType() {
            return type;
        }
    }

    private MCQStrategy getStrategyForType(InfosType type) {
        MCQStrategy strategy = null;
        if (type.equals(InfosType.MOVIE)) {
            strategy = new MovieStrategy();
        } else if (type.equals(InfosType.TV)) {
            strategy = new TvStrategy();
        } else {
            strategy = new PersonStrategy();
        }
        return strategy;
    }

    @ApiOperation(value = "Gets a mcq : guess from a picture")
    @GetMapping(value = "/which-by-image", produces = { "application/json" })
    public ResponseEntity<?> whichByImage(
            @RequestParam(required = false, value = "type", defaultValue = "random") String type,
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        try {

            CleanParams params = new CleanParams(language, type);

            MCQStrategy strategy = getStrategyForType(params.getType());

            MCQQuestion mcq = strategy.whichByImage(params.getLanguage());

            return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}