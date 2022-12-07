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
import cinequiz.backend.api_questions.tmdb_objects.show.cast.CastMember;
import cinequiz.backend.api_questions.tmdb_objects.show.movie.MovieInfos;
import cinequiz.backend.api_questions.utils.Language;
import cinequiz.backend.api_questions.utils.MovieTmdbFetchOptions;
import cinequiz.backend.api_questions.utils.MovieTmdbFetching;
import cinequiz.backend.api_questions.utils.questions.MovieQuestion;
import edu.emory.mathcs.backport.java.util.Collections;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/cinequiz/questions/movie", method = RequestMethod.GET)
public class MovieQuestionController {

    private final int NB_CHOICES_IN_MCQ = 4;
    private final int NB_DEFINED_QUESTIONS = 7;

    @ApiOperation(value = "Gets a random mcq about a movie")
    @GetMapping("/random")
    public ResponseEntity<?> random_question(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        int randomQuestion = BackendApplication.random(1, NB_DEFINED_QUESTIONS);
        switch (randomQuestion) {
            case 1:
                return which_by_image(language);
            case 2:
                return which_by_description(language);
            case 3:
                return budget(language);
            case 4:
                return revenue(language);
            case 5:
                return takePart(language);
            case 6:
                return doesntTakePart(language);
            case 7:
                return releaseDate(language);
            default:
                return which_by_image(language);

        }
    }

    @ApiOperation(value = "Gets a mcq : [Image] What is this film?")
    @GetMapping(value = "/which-by-image", produces = { "application/json" })
    public ResponseEntity<?> which_by_image(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = Language.languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();
        try {
            MovieTmdbFetchOptions answerOptions = new MovieTmdbFetchOptions(true, true, false, false, false, false);
            MovieTmdbFetchOptions similaryOptions = new MovieTmdbFetchOptions(true, false, false, false, false, false);
            movieList = MovieTmdbFetching.getRandomCoherentMovies(internLanguage.getTmdbLanguage(), NB_CHOICES_IN_MCQ,
                    answerOptions,
                    similaryOptions);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MovieInfos answer = movieList.get(0);
        Collections.shuffle(movieList);
        String[] choices = { movieList.get(0).title, movieList.get(1).title, movieList.get(2).title,
                movieList.get(3).title };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(answer.backdrop_path, "",
                MovieQuestion.WHICH_BY_IMAGE.getQuestion(internLanguage),
                choicesObject,
                answer.title);

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }

    @ApiOperation(value = "Gets a mcq : [Description] Which film fits this description?")
    @GetMapping(value = "/which-by-description", produces = { "application/json" })
    public ResponseEntity<?> which_by_description(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = Language.languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();
        try {
            MovieTmdbFetchOptions answerOptions = new MovieTmdbFetchOptions(true, false, true, false, false, false);
            MovieTmdbFetchOptions similaryOptions = new MovieTmdbFetchOptions(true, false, false, false, false, false);
            movieList = MovieTmdbFetching.getRandomCoherentMovies(internLanguage.getTmdbLanguage(), NB_CHOICES_IN_MCQ,
                    answerOptions,
                    similaryOptions);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MovieInfos answer = movieList.get(0);
        Collections.shuffle(movieList);
        String[] choices = { movieList.get(0).title, movieList.get(1).title, movieList.get(2).title,
                movieList.get(3).title };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion("", answer.overview,
                MovieQuestion.WHICH_BY_DESCRIPTION.getQuestion(internLanguage),
                choicesObject,
                answer.title);

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }

    @ApiOperation(value = "Gets a mcq : What is the budget used for this film?")
    @GetMapping(value = "/budget", produces = { "application/json" })
    public ResponseEntity<?> budget(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = Language.languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();
        try {
            MovieTmdbFetchOptions answerOptions = new MovieTmdbFetchOptions(true, true, false, true, false, false);
            MovieTmdbFetchOptions similaryOptions = new MovieTmdbFetchOptions(false, false, false, true, false, false);
            movieList = MovieTmdbFetching.getRandomCoherentMovies(internLanguage.getTmdbLanguage(), NB_CHOICES_IN_MCQ,
                    answerOptions,
                    similaryOptions);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MovieInfos answer = movieList.get(0);
        Collections.shuffle(movieList);
        String[] choices = { Long.toString(movieList.get(0).budget), Long.toString(movieList.get(1).budget),
                Long.toString(movieList.get(2).budget),
                Long.toString(movieList.get(3).budget) };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(answer.backdrop_path, answer.title,
                MovieQuestion.BUDGET.getQuestion(internLanguage),
                choicesObject,
                Long.toString(answer.budget));

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }

    @ApiOperation(value = "Gets a mcq : What are the revenues generated by this film?")
    @GetMapping(value = "/revenue", produces = { "application/json" })
    public ResponseEntity<?> revenue(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = Language.languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();
        try {
            MovieTmdbFetchOptions answerOptions = new MovieTmdbFetchOptions(true, true, false, false, true, false);
            MovieTmdbFetchOptions similaryOptions = new MovieTmdbFetchOptions(false, false, false, false, true, false);
            movieList = MovieTmdbFetching.getRandomCoherentMovies(internLanguage.getTmdbLanguage(), NB_CHOICES_IN_MCQ,
                    answerOptions,
                    similaryOptions);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MovieInfos answer = movieList.get(0);
        Collections.shuffle(movieList);
        String[] choices = { Long.toString(movieList.get(0).revenue), Long.toString(movieList.get(1).revenue),
                Long.toString(movieList.get(2).revenue),
                Long.toString(movieList.get(3).revenue) };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(answer.backdrop_path, answer.title,
                MovieQuestion.REVENUE.getQuestion(internLanguage),
                choicesObject,
                Long.toString(answer.revenue));

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

        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();
        ArrayList<CastMember> cast = null;
        try {
            while (cast == null) {
                MovieTmdbFetchOptions manswerOptions = new MovieTmdbFetchOptions(true, true, false, false, false,
                        false);
                MovieTmdbFetchOptions msimilaryOptions = new MovieTmdbFetchOptions(false, false, false, false, false,
                        false);
                movieList = MovieTmdbFetching.getRandomCoherentMovies(internLanguage.getTmdbLanguage(), 2,
                        manswerOptions,
                        msimilaryOptions);

                MovieInfos movie = movieList.get(0);
                MovieInfos similaryMovie = movieList.get(1);

                cast = MovieTmdbFetching.getRandomCoherentPeopleListInTheseMovies(movie.id, 1, similaryMovie.id, 3,
                        internLanguage.getTmdbLanguage());
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MovieInfos movieOfQuestion = movieList.get(0);
        CastMember answer = cast.get(0);
        Collections.shuffle(cast);
        String[] choices = { cast.get(0).name, cast.get(1).name, cast.get(2).name, cast.get(3).name };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(movieOfQuestion.backdrop_path, movieOfQuestion.title,
                MovieQuestion.TAKE_PART.getQuestion(internLanguage),
                choicesObject,
                answer.name);

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

        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();
        ArrayList<CastMember> cast = null;
        try {
            while (cast == null) {
                MovieTmdbFetchOptions manswerOptions = new MovieTmdbFetchOptions(false, false, false, false, false,
                        false);
                MovieTmdbFetchOptions msimilaryOptions = new MovieTmdbFetchOptions(true, true, false, false, false,
                        false);
                movieList = MovieTmdbFetching.getRandomCoherentMovies(internLanguage.getTmdbLanguage(),
                        2,
                        manswerOptions,
                        msimilaryOptions);

                MovieInfos movie = movieList.get(1);
                MovieInfos similaryMovie = movieList.get(0);

                cast = MovieTmdbFetching.getRandomCoherentPeopleListInTheseMovies(similaryMovie.id, 1, movie.id, 3,
                        internLanguage.getTmdbLanguage());
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MovieInfos movieOfQuestion = movieList.get(1);
        CastMember answer = cast.get(0);
        Collections.shuffle(cast);
        String[] choices = { cast.get(0).name, cast.get(1).name, cast.get(2).name, cast.get(3).name };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(movieOfQuestion.backdrop_path, movieOfQuestion.title,
                MovieQuestion.DOESNT_TAKE_PART.getQuestion(internLanguage),
                choicesObject,
                answer.name);

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

        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();
        try {
            MovieTmdbFetchOptions answerOptions = new MovieTmdbFetchOptions(true, true, false, false, false, true);
            MovieTmdbFetchOptions similaryOptions = new MovieTmdbFetchOptions(false, false, false, false, false, true);
            movieList = MovieTmdbFetching.getRandomCoherentMovies(internLanguage.getTmdbLanguage(), NB_CHOICES_IN_MCQ,
                    answerOptions,
                    similaryOptions);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MovieInfos answer = movieList.get(0);
        Collections.shuffle(movieList);
        String[] choices = { movieList.get(0).release_date, movieList.get(1).release_date,
                movieList.get(2).release_date,
                movieList.get(3).release_date };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(answer.backdrop_path, answer.title,
                MovieQuestion.RELEASE_DATE.getQuestion(internLanguage),
                choicesObject,
                answer.release_date);

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }

}