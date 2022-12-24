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
import cinequiz.backend.api_questions.utils.questions.TvShowQuestion;
import cinequiz.backend.api_questions.utils.tmdb.fetching.TmdbFetching;
import cinequiz.backend.api_questions.utils.tmdb.fetching.MediaTmdbFetching;
import cinequiz.backend.api_questions.utils.tmdb.fetching.PeopleTmdbFetching;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosType;
import cinequiz.backend.api_questions.utils.tmdb.fetching.options.InfosTmdbFetchingOptions;
import cinequiz.backend.api_questions.utils.tmdb.model.InfosInterface;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonMediaCredits;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@RequestMapping(value = "/cinequiz/questions/tv-show", method = RequestMethod.GET)
public class TvShowQuestionController {

    private final int NB_CHOICES_IN_MCQ = 4;
    private final int NB_DEFINED_QUESTIONS = 5;

    @ApiOperation(value = "Gets a random mcq about a tv show")
    @GetMapping("/random")
    public ResponseEntity<?> random_question(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        int randomQuestion = BackendApplication.random(1, NB_DEFINED_QUESTIONS);
        switch (randomQuestion) {
            case 1:
                return whichByImage(language);
            case 2:
                return whichByDescription(language);
            case 3:
                return firstAirDate(language);
            case 4:
                return takePart(language);
            case 5:
                return doesntTakePart(language);
            default:
                return whichByImage(language);

        }
    }

    @ApiOperation(value = "Gets a mcq : [Image] What is this tv show?")
    @GetMapping(value = "/which-by-image", produces = { "application/json" })
    public ResponseEntity<?> whichByImage(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = Language.languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        List<InfosInterface> tvList = new ArrayList<InfosInterface>();
        try {
            InfosTmdbFetchingOptions answerOptions = new InfosTmdbFetchingOptions(true, true, false, false);
            InfosTmdbFetchingOptions similaryOptions = new InfosTmdbFetchingOptions(true, false, false, false);
            tvList = MediaTmdbFetching.getRandomCoherentMedias(internLanguage, NB_CHOICES_IN_MCQ,
                    answerOptions,
                    similaryOptions, InfosType.TV);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        InfosInterface answer = tvList.get(0);
        Collections.shuffle(tvList);
        String[] choices = { tvList.get(0).getName(), tvList.get(1).getName(), tvList.get(2).getName(),
                tvList.get(3).getName() };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(TmdbFetching.IMG_URL_BASE + answer.getImage(), "",
                TvShowQuestion.WHICH_BY_IMAGE.getQuestion(internLanguage),
                choicesObject,
                answer.getName());

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }

    @ApiOperation(value = "Gets a mcq : [Description] Which tv show fits this description?")
    @GetMapping(value = "/which-by-description", produces = { "application/json" })
    public ResponseEntity<?> whichByDescription(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = Language.languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        List<InfosInterface> tvList = new ArrayList<InfosInterface>();
        try {
            InfosTmdbFetchingOptions answerOptions = new InfosTmdbFetchingOptions(true, false, true, false);
            InfosTmdbFetchingOptions similaryOptions = new InfosTmdbFetchingOptions(true, false, false, false);
            tvList = MediaTmdbFetching.getRandomCoherentMedias(internLanguage, NB_CHOICES_IN_MCQ,
                    answerOptions, similaryOptions, InfosType.TV);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        InfosInterface answer = tvList.get(0);
        Collections.shuffle(tvList);
        String[] choices = { tvList.get(0).getName(), tvList.get(1).getName(), tvList.get(2).getName(),
                tvList.get(3).getName() };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion("", answer.getDescription(),
                TvShowQuestion.WHICH_BY_DESCRIPTION.getQuestion(internLanguage),
                choicesObject,
                answer.getName());

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }

    @ApiOperation(value = "Gets a mcq : When did this tv show first air?")
    @GetMapping(value = "/first-air-date", produces = { "application/json" })
    public ResponseEntity<?> firstAirDate(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = Language.languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        List<InfosInterface> tvList = new ArrayList<InfosInterface>();
        try {
            InfosTmdbFetchingOptions answerOptions = new InfosTmdbFetchingOptions(true, true, false, true);
            InfosTmdbFetchingOptions similaryOptions = new InfosTmdbFetchingOptions(false, false, false, true);
            tvList = MediaTmdbFetching.getRandomCoherentMedias(internLanguage, NB_CHOICES_IN_MCQ,
                    answerOptions, similaryOptions, InfosType.TV);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        InfosInterface answer = tvList.get(0);
        Collections.shuffle(tvList);
        String[] choices = { tvList.get(0).getDate(),
                tvList.get(1).getDate(),
                tvList.get(2).getDate(),
                tvList.get(3).getDate() };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(TmdbFetching.IMG_URL_BASE + answer.getImage(), answer.getName(),
                TvShowQuestion.FIRST_AIR_DATE.getQuestion(internLanguage),
                choicesObject,
                answer.getDate());

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }

    @ApiOperation(value = "Gets a mcq : Who was involved in this tv show?")
    @GetMapping(value = "/take-part", produces = { "application/json" })
    public ResponseEntity<?> doesntTakePart(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = Language.languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        List<InfosInterface> tvList = new ArrayList<InfosInterface>();
        List<PersonMediaCredits> cast = null;
        try {
            while (cast == null) {
                InfosTmdbFetchingOptions manswerOptions = new InfosTmdbFetchingOptions(true, true, false, false);
                InfosTmdbFetchingOptions msimilaryOptions = new InfosTmdbFetchingOptions(false, false, false, false);
                tvList = MediaTmdbFetching.getRandomCoherentMedias(internLanguage, 2,
                        manswerOptions, msimilaryOptions, InfosType.TV);

                InfosInterface tvShow = tvList.get(0);
                InfosInterface similarTvShow = tvList.get(1);

                cast = PeopleTmdbFetching.getRandomCoherentPeopleListInTheseMedias(tvShow.getId(), 1,
                        similarTvShow.getId(), 3,
                        internLanguage, InfosType.TV);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        InfosInterface tvShowOfQuestion = tvList.get(0);
        PersonMediaCredits answer = cast.get(0);
        Collections.shuffle(cast);
        String[] choices = { cast.get(0).getName(), cast.get(1).getName(), cast.get(2).getName(),
                cast.get(3).getName() };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(TmdbFetching.IMG_URL_BASE + tvShowOfQuestion.getImage(),
                tvShowOfQuestion.getName(),
                TvShowQuestion.TAKE_PART.getQuestion(internLanguage),
                choicesObject,
                answer.getName());

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }

    @ApiOperation(value = "Gets a mcq : Who wasn't involved in this tv show?")
    @GetMapping(value = "/doesnt-take-part", produces = { "application/json" })
    public ResponseEntity<?> takePart(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = Language.languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        List<InfosInterface> tvList = new ArrayList<InfosInterface>();
        List<PersonMediaCredits> cast = null;
        try {
            while (cast == null) {
                InfosTmdbFetchingOptions manswerOptions = new InfosTmdbFetchingOptions(true, false, false, false);
                InfosTmdbFetchingOptions msimilaryOptions = new InfosTmdbFetchingOptions(true, true, false, false);
                tvList = MediaTmdbFetching.getRandomCoherentMedias(internLanguage, 2,
                        manswerOptions, msimilaryOptions, InfosType.TV);

                InfosInterface tvShow = tvList.get(0);
                InfosInterface similarTvShow = tvList.get(1);

                cast = PeopleTmdbFetching.getRandomCoherentPeopleListInTheseMedias(tvShow.getId(), 1,
                        similarTvShow.getId(), 3,
                        internLanguage, InfosType.TV);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        InfosInterface tvShowOfQuestion = tvList.get(1);
        PersonMediaCredits answer = cast.get(0);
        Collections.shuffle(cast);
        String[] choices = { cast.get(0).getName(), cast.get(1).getName(), cast.get(2).getName(),
                cast.get(3).getName() };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(TmdbFetching.IMG_URL_BASE + tvShowOfQuestion.getImage(),
                tvShowOfQuestion.getName(),
                TvShowQuestion.DOESNT_TAKE_PART.getQuestion(internLanguage),
                choicesObject,
                answer.getName());

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }
}