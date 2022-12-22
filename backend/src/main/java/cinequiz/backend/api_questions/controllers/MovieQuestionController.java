package cinequiz.backend.api_questions.controllers;

import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cinequiz.backend.BackendApplication;
import cinequiz.backend.api_questions.exceptions.LanguageNotSupportedException;
import cinequiz.backend.api_questions.mcq.Choices;
import cinequiz.backend.api_questions.mcq.MCQQuestion;
import cinequiz.backend.api_questions.utils.Language;
import cinequiz.backend.api_questions.utils.questions.MovieQuestion;
import cinequiz.backend.api_questions.utils.tmdb.fetching.MediaTmdbFetching;
import cinequiz.backend.api_questions.utils.tmdb.fetching.TmdbFetching;
import cinequiz.backend.api_questions.utils.tmdb.fetching.options.MediaTmdbFetchingOptions;
import cinequiz.backend.api_questions.utils.tmdb.model.media.MediaInterface;
import cinequiz.backend.api_questions.utils.tmdb.model.media.MediaType;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonMovieCredits;
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

        ArrayList<MediaInterface> movieList = new ArrayList<MediaInterface>();
        try {
            MediaTmdbFetchingOptions answerOptions = new MediaTmdbFetchingOptions(true, false, true, false, false);
            MediaTmdbFetchingOptions similaryOptions = new MediaTmdbFetchingOptions(true, false, false, false, false);
            movieList = MediaTmdbFetching.getRandomCoherentMedias(internLanguage.getTmdbLanguage(), NB_CHOICES_IN_MCQ,
                    answerOptions,
                    similaryOptions, MediaType.MOVIE);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MediaInterface answer = movieList.get(0);
        Collections.shuffle(movieList);
        String[] choices = { movieList.get(0).getTitle(), movieList.get(1).getTitle(), movieList.get(2).getTitle(),
                movieList.get(3).getTitle() };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(TmdbFetching.IMG_URL_BASE + answer.getBackdropPath(), "",
                MovieQuestion.WHICH_BY_IMAGE.getQuestion(internLanguage),
                choicesObject,
                answer.getTitle());

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

        ArrayList<MediaInterface> movieList = new ArrayList<MediaInterface>();
        try {
            MediaTmdbFetchingOptions answerOptions = new MediaTmdbFetchingOptions(true, false, false, true, false);
            MediaTmdbFetchingOptions similaryOptions = new MediaTmdbFetchingOptions(true, false, false, false, false);
            movieList = MediaTmdbFetching.getRandomCoherentMedias(internLanguage.getTmdbLanguage(), NB_CHOICES_IN_MCQ,
                    answerOptions,
                    similaryOptions, MediaType.MOVIE);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MediaInterface answer = movieList.get(0);
        Collections.shuffle(movieList);
        String[] choices = { movieList.get(0).getTitle(), movieList.get(1).getTitle(), movieList.get(2).getTitle(),
                movieList.get(3).getTitle() };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion("", answer.getOverview(),
                MovieQuestion.WHICH_BY_DESCRIPTION.getQuestion(internLanguage),
                choicesObject,
                answer.getTitle());

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

        ArrayList<MediaInterface> movieList = new ArrayList<MediaInterface>();
        ArrayList<PersonMovieCredits> cast = null;
        try {
            while (cast == null) {
                MediaTmdbFetchingOptions manswerOptions = new MediaTmdbFetchingOptions(true, false, true, false,
                        false);
                MediaTmdbFetchingOptions msimilaryOptions = new MediaTmdbFetchingOptions(false, false, false, false,
                        false);
                movieList = MediaTmdbFetching.getRandomCoherentMedias(internLanguage.getTmdbLanguage(), 2,
                        manswerOptions,
                        msimilaryOptions, MediaType.MOVIE);

                MediaInterface movie = movieList.get(0);
                MediaInterface similaryMovie = movieList.get(1);

                cast = MediaTmdbFetching.getRandomCoherentPeopleListInTheseMedias(movie.getId(), 1,
                        similaryMovie.getId(), 3,
                        internLanguage.getTmdbLanguage(), MediaType.MOVIE);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MediaInterface movieOfQuestion = movieList.get(0);
        PersonMovieCredits answer = cast.get(0);
        Collections.shuffle(cast);
        String[] choices = { cast.get(0).getName(), cast.get(1).getName(), cast.get(2).getName(),
                cast.get(3).getName() };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(TmdbFetching.IMG_URL_BASE + movieOfQuestion.getBackdropPath(),
                movieOfQuestion.getTitle(),
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

        ArrayList<MediaInterface> movieList = new ArrayList<MediaInterface>();
        ArrayList<PersonMovieCredits> cast = null;
        try {
            while (cast == null) {
                MediaTmdbFetchingOptions manswerOptions = new MediaTmdbFetchingOptions(false, false, false, false,
                        false);
                MediaTmdbFetchingOptions msimilaryOptions = new MediaTmdbFetchingOptions(true, false, true, false,
                        false);
                movieList = MediaTmdbFetching.getRandomCoherentMedias(internLanguage.getTmdbLanguage(),
                        2,
                        manswerOptions,
                        msimilaryOptions, MediaType.MOVIE);

                MediaInterface movie = movieList.get(0);
                MediaInterface similaryMovie = movieList.get(1);

                cast = MediaTmdbFetching.getRandomCoherentPeopleListInTheseMedias(movie.getId(), 1,
                        similaryMovie.getId(), 3,
                        internLanguage.getTmdbLanguage(), MediaType.MOVIE);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MediaInterface movieOfQuestion = movieList.get(1);
        PersonMovieCredits answer = cast.get(0);
        Collections.shuffle(cast);
        String[] choices = { cast.get(0).getName(), cast.get(1).getName(), cast.get(2).getName(),
                cast.get(3).getName() };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(TmdbFetching.IMG_URL_BASE + movieOfQuestion.getBackdropPath(),
                movieOfQuestion.getTitle(),
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

        ArrayList<MediaInterface> movieList = new ArrayList<MediaInterface>();
        try {
            MediaTmdbFetchingOptions answerOptions = new MediaTmdbFetchingOptions(true, true, false, false, true);
            MediaTmdbFetchingOptions similaryOptions = new MediaTmdbFetchingOptions(false, false, false, false, true);
            movieList = MediaTmdbFetching.getRandomCoherentMedias(internLanguage.getTmdbLanguage(), NB_CHOICES_IN_MCQ,
                    answerOptions,
                    similaryOptions, MediaType.MOVIE);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MediaInterface answer = movieList.get(0);
        Collections.shuffle(movieList);
        String[] choices = { movieList.get(0).getReleaseDate(), movieList.get(1).getReleaseDate(),
                movieList.get(2).getReleaseDate(),
                movieList.get(3).getReleaseDate() };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(TmdbFetching.IMG_URL_BASE + answer.getPosterPath(), answer.getTitle(),
                MovieQuestion.RELEASE_DATE.getQuestion(internLanguage),
                choicesObject,
                answer.getReleaseDate());

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }

}