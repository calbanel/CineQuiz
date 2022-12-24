package cinequiz.backend.api_questions.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinequiz.backend.BackendApplication;
import cinequiz.backend.api_questions.schemas.Choices;
import cinequiz.backend.api_questions.schemas.MCQQuestion;
import cinequiz.backend.api_questions.utils.Language;
import cinequiz.backend.api_questions.utils.exceptions.LanguageNotSupportedException;
import cinequiz.backend.api_questions.utils.questions.PeopleQuestion;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosTmdbFetchingOptions;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosType;
import cinequiz.backend.api_questions.utils.tmdb.fetching.TmdbFetching;
import cinequiz.backend.api_questions.utils.tmdb.model.InfosInterface;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@RequestMapping(value = "/cinequiz/questions/person", method = RequestMethod.GET)
public class PeopleQuestionController {

    private final int NB_CHOICES_IN_MCQ = 4;
    private final int NB_DEFINED_QUESTIONS = 1;

    @ApiOperation(value = "Gets a random mcq about a person")
    @GetMapping("/random")
    public ResponseEntity<?> random_question(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        int randomQuestion = BackendApplication.random(1, NB_DEFINED_QUESTIONS);
        switch (randomQuestion) {
            case 1:
                return whichByImage(language);
            default:
                return whichByImage(language);

        }
    }

    @ApiOperation(value = "Gets a mcq : [Image] Who is this person?")
    @GetMapping(value = "/which-by-image", produces = { "application/json" })
    public ResponseEntity<?> whichByImage(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = Language.languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        List<InfosInterface> personList = new ArrayList<InfosInterface>();
        try {
            InfosTmdbFetchingOptions options = new InfosTmdbFetchingOptions(true, true, false, false, false);
            personList = TmdbFetching.getRandomValidInfos(internLanguage, options, InfosType.PERSON, NB_CHOICES_IN_MCQ);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        InfosInterface answer = personList.get(0);
        Collections.shuffle(personList);
        String[] choices = { personList.get(0).getName(), personList.get(1).getName(), personList.get(2).getName(),
                personList.get(3).getName() };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(TmdbFetching.IMG_URL_BASE + answer.getImage(), "",
                PeopleQuestion.WHICH_BY_IMAGE.getQuestion(internLanguage),
                choicesObject,
                answer.getName());

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }
}