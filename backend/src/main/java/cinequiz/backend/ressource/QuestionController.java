package cinequiz.backend.ressource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinequiz.backend.BackendApplication;
import cinequiz.backend.api_questions.utils.Language;
import cinequiz.backend.api_questions.utils.MCQStrategy;
import cinequiz.backend.api_questions.utils.MovieStrategy;
import cinequiz.backend.api_questions.utils.PersonStrategy;
import cinequiz.backend.api_questions.utils.TvStrategy;
import cinequiz.backend.api_questions.utils.exceptions.ImpossibleToFetchTmdbException;
import cinequiz.backend.api_questions.utils.exceptions.LanguageNotSupportedException;
import cinequiz.backend.api_questions.utils.exceptions.TypeNotSupportedException;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosType;
import cinequiz.backend.model.ListOfMCQ;
import cinequiz.backend.model.MCQQuestion;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/questions", method = RequestMethod.GET)
public class QuestionController {

    private final int NB_DEFINED_QUESTIONS = 5;

    @ApiOperation(value = "Gets a random mcq")
    @GetMapping("/random")
    public ResponseEntity<MCQQuestion> random_question(
            @RequestParam(required = false, value = "type", defaultValue = "random") String type,
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        int randomQuestion = BackendApplication.random(1, NB_DEFINED_QUESTIONS);
        switch (randomQuestion) {
            case 1:
                return whichByImage(type, language);
            case 2:
                return whichByDescription(type, language);
            case 3:
                return date(type, language);
            case 4:
                return takePart(type, language);
            case 5:
                return doesntTakePart(type, language);
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
    public ResponseEntity<MCQQuestion> whichByImage(
            @RequestParam(required = false, value = "type", defaultValue = "random") String type,
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        try {
            CleanParams params = new CleanParams(language, type);

            MCQStrategy strategy = getStrategyForType(params.getType());

            MCQQuestion mcq = strategy.whichByImage(params.getLanguage());

            return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
        } catch (ImpossibleToFetchTmdbException e) {
            e.printStackTrace();
            return new ResponseEntity<MCQQuestion>(new MCQQuestion(), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (LanguageNotSupportedException | TypeNotSupportedException e) {
            e.printStackTrace();
            return new ResponseEntity<MCQQuestion>(new MCQQuestion(), HttpStatus.BAD_REQUEST);
        }

    }

    @ApiOperation(value = "Gets a mcq : guess from a description")
    @GetMapping(value = "/which-by-description", produces = { "application/json" })
    public ResponseEntity<MCQQuestion> whichByDescription(
            @RequestParam(required = false, value = "type", defaultValue = "random") String type,
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        try {
            CleanParams params = new CleanParams(language, type);

            MCQStrategy strategy = getStrategyForType(params.getType());

            MCQQuestion mcq = strategy.whichByDescription(params.getLanguage());

            return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
        } catch (ImpossibleToFetchTmdbException e) {
            e.printStackTrace();
            return new ResponseEntity<MCQQuestion>(new MCQQuestion(), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (LanguageNotSupportedException | TypeNotSupportedException e) {
            e.printStackTrace();
            return new ResponseEntity<MCQQuestion>(new MCQQuestion(), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Gets a mcq : find the good date")
    @GetMapping(value = "/date", produces = { "application/json" })
    public ResponseEntity<MCQQuestion> date(
            @RequestParam(required = false, value = "type", defaultValue = "random") String type,
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        try {
            CleanParams params = new CleanParams(language, type);

            MCQStrategy strategy = getStrategyForType(params.getType());

            MCQQuestion mcq = strategy.date(params.getLanguage());

            return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
        } catch (ImpossibleToFetchTmdbException e) {
            e.printStackTrace();
            return new ResponseEntity<MCQQuestion>(new MCQQuestion(), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (LanguageNotSupportedException | TypeNotSupportedException e) {
            e.printStackTrace();
            return new ResponseEntity<MCQQuestion>(new MCQQuestion(), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Gets a mcq : find who took part")
    @GetMapping(value = "/take-part", produces = { "application/json" })
    public ResponseEntity<MCQQuestion> takePart(
            @RequestParam(required = false, value = "type", defaultValue = "random") String type,
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        try {
            CleanParams params = new CleanParams(language, type);

            MCQStrategy strategy = getStrategyForType(params.getType());

            MCQQuestion mcq = strategy.takePart(params.getLanguage());

            return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
        } catch (ImpossibleToFetchTmdbException e) {
            e.printStackTrace();
            return new ResponseEntity<MCQQuestion>(new MCQQuestion(), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (LanguageNotSupportedException | TypeNotSupportedException e) {
            e.printStackTrace();
            return new ResponseEntity<MCQQuestion>(new MCQQuestion(), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Gets a mcq : find who did not take part")
    @GetMapping(value = "/doesnt-take-part", produces = { "application/json" })
    public ResponseEntity<MCQQuestion> doesntTakePart(
            @RequestParam(required = false, value = "type", defaultValue = "random") String type,
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        try {
            CleanParams params = new CleanParams(language, type);

            MCQStrategy strategy = getStrategyForType(params.getType());

            MCQQuestion mcq = strategy.doesntTakePart(params.getLanguage());

            return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
        } catch (ImpossibleToFetchTmdbException e) {
            e.printStackTrace();
            return new ResponseEntity<MCQQuestion>(new MCQQuestion(), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (LanguageNotSupportedException | TypeNotSupportedException e) {
            e.printStackTrace();
            return new ResponseEntity<MCQQuestion>(new MCQQuestion(), HttpStatus.BAD_REQUEST);
        }
    }

    private final int MIN_NUMBER_PARAM = 1;
    private final int MAX_NUMBER_PARAM = 10;

    @ApiOperation(value = "Gets a random list of mcq")
    @GetMapping("/random-list")
    public ResponseEntity<ListOfMCQ> random_list(
            @RequestParam(required = false, value = "number", defaultValue = "1") Integer number,
            @RequestParam(required = false, value = "type", defaultValue = "random") String type,
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        if (number < MIN_NUMBER_PARAM || number > MAX_NUMBER_PARAM)
            return new ResponseEntity<ListOfMCQ>(new ListOfMCQ(), HttpStatus.BAD_REQUEST);

        List<MCQQuestion> list = new ArrayList<MCQQuestion>();
        for (int i = 0; i < number; i++) {
            ResponseEntity<MCQQuestion> randomQuestion = random_question(type, language);

            if (randomQuestion.getStatusCode().equals(HttpStatus.BAD_REQUEST))
                return new ResponseEntity<ListOfMCQ>(new ListOfMCQ(), HttpStatus.BAD_REQUEST);

            if (randomQuestion.getStatusCode().equals(HttpStatus.SERVICE_UNAVAILABLE))
                return new ResponseEntity<ListOfMCQ>(new ListOfMCQ(), HttpStatus.SERVICE_UNAVAILABLE);

            list.add(randomQuestion.getBody());
        }

        ListOfMCQ result = new ListOfMCQ(list, type, number, language);
        return new ResponseEntity<ListOfMCQ>(result, HttpStatus.OK);
    }
}