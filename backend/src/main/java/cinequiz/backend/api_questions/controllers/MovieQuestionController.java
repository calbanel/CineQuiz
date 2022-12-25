package cinequiz.backend.api_questions.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cinequiz.backend.BackendApplication;
import cinequiz.backend.api_questions.schemas.Choices;
import cinequiz.backend.api_questions.schemas.MCQQuestion;
import cinequiz.backend.api_questions.utils.Language;
import cinequiz.backend.api_questions.utils.exceptions.LanguageNotSupportedException;
import cinequiz.backend.api_questions.utils.questions.MovieQuestion;
import cinequiz.backend.api_questions.utils.tmdb.fetching.MediaTmdbFetching;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosTmdbFetchingOptions;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosType;
import cinequiz.backend.api_questions.utils.tmdb.fetching.TmdbFetching;
import cinequiz.backend.api_questions.utils.tmdb.model.InfosInterface;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonMediaCredits;
import edu.emory.mathcs.backport.java.util.Collections;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RestController
@RequestMapping(value = "/cinequiz/questions/movie", method = RequestMethod.GET)
public class MovieQuestionController {

    private final int NB_CHOICES_IN_MCQ = 4;
    private final int NB_DEFINED_QUESTIONS = 5;

    @ApiOperation(value = "Gets a random mcq about a movie")
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
                return takePart(language);
            case 4:
                return doesntTakePart(language);
            case 5:
                return releaseDate(language);
            default:
                return whichByImage(language);

        }
    }

    @ApiOperation(value = "Gets a mcq : [Image] What is this film?")
    @GetMapping(value = "/which-by-image", produces = { "application/json" })
    public ResponseEntity<?> whichByImage(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = Language.languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        List<InfosInterface> movieList = new ArrayList<InfosInterface>();
        try {
            InfosTmdbFetchingOptions answerOptions = new InfosTmdbFetchingOptions(true, true, false, false, false);
            InfosTmdbFetchingOptions similaryOptions = new InfosTmdbFetchingOptions(true, false, false, false, false);
            movieList = MediaTmdbFetching.getRandomCoherentMedias(internLanguage, NB_CHOICES_IN_MCQ,
                    answerOptions,
                    similaryOptions, InfosType.MOVIE);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        InfosInterface answer = movieList.get(0);
        Collections.shuffle(movieList);
        String[] choices = { movieList.get(0).getName(), movieList.get(1).getName(), movieList.get(2).getName(),
                movieList.get(3).getName() };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(TmdbFetching.IMG_URL_BASE + answer.getImage(), "",
                MovieQuestion.WHICH_BY_IMAGE.getQuestion(internLanguage),
                choicesObject,
                answer.getName());

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }

    @ApiOperation(value = "Gets a mcq : [Description] Which film fits this description?")
    @GetMapping(value = "/which-by-description", produces = { "application/json" })
    public ResponseEntity<?> whichByDescription(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = Language.languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        List<InfosInterface> movieList = new ArrayList<InfosInterface>();
        try {
            InfosTmdbFetchingOptions answerOptions = new InfosTmdbFetchingOptions(true, false, true, false, false);
            InfosTmdbFetchingOptions similaryOptions = new InfosTmdbFetchingOptions(true, false, false, false, false);
            movieList = MediaTmdbFetching.getRandomCoherentMedias(internLanguage, NB_CHOICES_IN_MCQ,
                    answerOptions,
                    similaryOptions, InfosType.MOVIE);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        InfosInterface answer = movieList.get(0);
        Collections.shuffle(movieList);
        String[] choices = { movieList.get(0).getName(), movieList.get(1).getName(), movieList.get(2).getName(),
                movieList.get(3).getName() };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion("", answer.getDescription(),
                MovieQuestion.WHICH_BY_DESCRIPTION.getQuestion(internLanguage),
                choicesObject,
                answer.getName());

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }

    @ApiOperation(value = "Gets a mcq : Who was involved in this film?")
    @GetMapping(value = "/take-part", produces = { "application/json" })
    public ResponseEntity<?> takePart(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = Language.languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        List<InfosInterface> movieList = new ArrayList<InfosInterface>();
        List<PersonMediaCredits> cast = null;
        try {
            while (cast == null) {
                InfosTmdbFetchingOptions manswerOptions = new InfosTmdbFetchingOptions(true, true, false, false, false);
                InfosTmdbFetchingOptions msimilaryOptions = new InfosTmdbFetchingOptions(false, false, false, false,
                        false);
                movieList = MediaTmdbFetching.getRandomCoherentMedias(internLanguage, 2,
                        manswerOptions,
                        msimilaryOptions, InfosType.MOVIE);

                InfosInterface movie = movieList.get(0);
                InfosInterface similaryMovie = movieList.get(1);

                cast = MediaTmdbFetching.getRandomCoherentPeopleListInTheseMedias(movie.getId(), 1,
                        similaryMovie.getId(), 3,
                        internLanguage, InfosType.MOVIE);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        InfosInterface movieOfQuestion = movieList.get(0);
        PersonMediaCredits answer = cast.get(0);
        Collections.shuffle(cast);
        String[] choices = { cast.get(0).getName(), cast.get(1).getName(), cast.get(2).getName(),
                cast.get(3).getName() };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(TmdbFetching.IMG_URL_BASE + movieOfQuestion.getImage(),
                movieOfQuestion.getName(),
                MovieQuestion.TAKE_PART.getQuestion(internLanguage),
                choicesObject,
                answer.getName());

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }

    @ApiOperation(value = "Gets a mcq : Who wasn't involved in this film?")
    @GetMapping(value = "/doesnt-take-part", produces = { "application/json" })
    public ResponseEntity<?> doesntTakePart(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = Language.languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        List<InfosInterface> movieList = new ArrayList<InfosInterface>();
        List<PersonMediaCredits> cast = null;
        try {
            while (cast == null) {
                InfosTmdbFetchingOptions manswerOptions = new InfosTmdbFetchingOptions(false, false, false,
                        false, false);
                InfosTmdbFetchingOptions msimilaryOptions = new InfosTmdbFetchingOptions(true, true, false,
                        false, false);
                movieList = MediaTmdbFetching.getRandomCoherentMedias(internLanguage,
                        2,
                        manswerOptions,
                        msimilaryOptions, InfosType.MOVIE);

                InfosInterface movie = movieList.get(0);
                InfosInterface similaryMovie = movieList.get(1);

                cast = MediaTmdbFetching.getRandomCoherentPeopleListInTheseMedias(movie.getId(), 1,
                        similaryMovie.getId(), 3,
                        internLanguage, InfosType.MOVIE);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        InfosInterface movieOfQuestion = movieList.get(1);
        PersonMediaCredits answer = cast.get(0);
        Collections.shuffle(cast);
        String[] choices = { cast.get(0).getName(), cast.get(1).getName(), cast.get(2).getName(),
                cast.get(3).getName() };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(TmdbFetching.IMG_URL_BASE + movieOfQuestion.getImage(),
                movieOfQuestion.getName(),
                MovieQuestion.DOESNT_TAKE_PART.getQuestion(internLanguage),
                choicesObject,
                answer.getName());

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }

    @ApiOperation(value = "Gets a mcq : When was this film released?")
    @GetMapping(value = "/release-date", produces = { "application/json" })
    public ResponseEntity<?> releaseDate(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = Language.languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        List<InfosInterface> movieList = new ArrayList<InfosInterface>();
        try {
            InfosTmdbFetchingOptions answerOptions = new InfosTmdbFetchingOptions(true, true, false, true, false);
            InfosTmdbFetchingOptions similaryOptions = new InfosTmdbFetchingOptions(false, false, false, true, false);
            movieList = MediaTmdbFetching.getRandomCoherentMedias(internLanguage, NB_CHOICES_IN_MCQ,
                    answerOptions,
                    similaryOptions, InfosType.MOVIE);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        InfosInterface answer = movieList.get(0);
        Collections.shuffle(movieList);
        String[] choices = { movieList.get(0).getDate(), movieList.get(1).getDate(),
                movieList.get(2).getDate(),
                movieList.get(3).getDate() };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(TmdbFetching.IMG_URL_BASE + answer.getImage(), answer.getName(),
                MovieQuestion.DATE.getQuestion(internLanguage),
                choicesObject,
                answer.getDate());

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }

}