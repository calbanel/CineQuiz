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
import cinequiz.backend.api_questions.utils.exceptions.BadInfosTypeException;
import cinequiz.backend.api_questions.utils.exceptions.ImpossibleToFetchTmdbException;
import cinequiz.backend.api_questions.utils.exceptions.LanguageNotSupportedException;
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

    @ApiOperation(value = "Gets a mcq : guess from a picture")
    @GetMapping(value = "/which-by-image", produces = { "application/json" })
    public ResponseEntity<?> whichByImage(
            @RequestParam(required = false, value = "type", defaultValue = "random") String type,
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = Language.languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        String infosType;
        if (type.equals("random"))
            infosType = InfosType.values()[BackendApplication.random(0, InfosType.values().length - 1)].getTmdbValue();
        else
            infosType = type;

        if (!InfosType.checkType(infosType)) {
            return new ResponseEntity<String>("Invalid type: " + type, HttpStatus.BAD_REQUEST);
        }

        MCQStrategy strategy;

        if (infosType.equals(InfosType.MOVIE.getTmdbValue())) {
            strategy = new MovieStrategy();
        } else if (infosType.equals(InfosType.TV.getTmdbValue())) {
            strategy = new TvStrategy();
        } else if (infosType.equals(InfosType.PERSON.getTmdbValue())) {
            strategy = new PersonStrategy();
        } else {
            return new ResponseEntity<String>("Invalid type: " + type, HttpStatus.BAD_REQUEST);
        }

        MCQQuestion mcq = null;
        try {
            mcq = strategy.whichByImage(internLanguage);
        } catch (ImpossibleToFetchTmdbException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (BadInfosTypeException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }
}