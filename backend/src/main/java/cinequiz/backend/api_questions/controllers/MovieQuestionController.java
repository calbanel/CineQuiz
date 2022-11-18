package cinequiz.backend.api_questions.controllers;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import cinequiz.backend.BackendApplication;
import cinequiz.backend.api_questions.Language;
import cinequiz.backend.api_questions.controllers.exceptions.LanguageNotSupportedException;
import cinequiz.backend.api_questions.controllers.exceptions.NotEnoughSimilarMoviesInTMDBException;
import cinequiz.backend.api_questions.controllers.exceptions.NotaValidMovieException;
import cinequiz.backend.api_questions.controllers.utils.TmdbFetchOptions;
import cinequiz.backend.api_questions.mcq.Choices;
import cinequiz.backend.api_questions.mcq.MCQQuestion;
import cinequiz.backend.api_questions.questions.MovieQuestion;
import cinequiz.backend.api_questions.tmdb_objects.MovieInfos;
import cinequiz.backend.api_questions.tmdb_objects.MovieListResult;
import cinequiz.backend.api_questions.tmdb_objects.PageResult;
import edu.emory.mathcs.backport.java.util.Collections;

@RestController
@RequestMapping("/questions/movie")
public class MovieQuestionController {

    private final int NB_CHOICES_IN_MCQ = 4;
    private final int NB_DEFINED_QUESTIONS = 4;

    @GetMapping("/")
    public ResponseEntity<?> random_question(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        int randomQuestion = (int) (0 + (Math.random() * (NB_DEFINED_QUESTIONS - 0)));
        switch (randomQuestion) {
            case 0:
                return which_by_image(language);
            case 1:
                return which_by_description(language);
            case 2:
                return budget(language);
            case 3:
                return revenue(language);
            default:
                return which_by_image(language);

        }
    }

    @GetMapping(value = "/which-by-image", produces = { "application/json" })
    public ResponseEntity<?> which_by_image(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();
        try {
            TmdbFetchOptions answerOptions = new TmdbFetchOptions(true, true, false, false, false, false);
            TmdbFetchOptions similaryOptions = new TmdbFetchOptions(true, false, false, false, false, false);
            movieList = getRandomCoherentMovies(internLanguage.getTmdbLanguage(), NB_CHOICES_IN_MCQ, answerOptions,
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

    @GetMapping(value = "/which-by-description", produces = { "application/json" })
    public ResponseEntity<?> which_by_description(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();
        try {
            TmdbFetchOptions answerOptions = new TmdbFetchOptions(true, false, true, false, false, false);
            TmdbFetchOptions similaryOptions = new TmdbFetchOptions(true, false, false, false, false, false);
            movieList = getRandomCoherentMovies(internLanguage.getTmdbLanguage(), NB_CHOICES_IN_MCQ, answerOptions,
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

    @GetMapping(value = "/budget", produces = { "application/json" })
    public ResponseEntity<?> budget(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();
        try {
            TmdbFetchOptions answerOptions = new TmdbFetchOptions(true, true, false, true, false, false);
            TmdbFetchOptions similaryOptions = new TmdbFetchOptions(false, false, false, true, false, false);
            movieList = getRandomCoherentMovies(internLanguage.getTmdbLanguage(), NB_CHOICES_IN_MCQ, answerOptions,
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

    @GetMapping(value = "/revenue", produces = { "application/json" })
    public ResponseEntity<?> revenue(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();
        try {
            TmdbFetchOptions answerOptions = new TmdbFetchOptions(true, true, false, false, true, false);
            TmdbFetchOptions similaryOptions = new TmdbFetchOptions(false, false, false, false, true, false);
            movieList = getRandomCoherentMovies(internLanguage.getTmdbLanguage(), NB_CHOICES_IN_MCQ, answerOptions,
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

    /*
     * 
     * Utils functions
     * 
     */

    private Language languageCheck(String language) throws LanguageNotSupportedException {
        Language lang;
        if (language.equals("fr"))
            lang = Language.FR;
        else if (language.equals("en"))
            lang = Language.EN;
        else
            throw new LanguageNotSupportedException();

        return lang;
    }

    /*
     *
     * TMDB fetching functions
     *
     */

    /*
     * private final int NB_RESCUE_IDS = 4;
     * private final int[] RESCUE_MOVIE_ID = { 829280, 675, 299534, 260514 };
     * // Enola Holmes 2, Harry Potter and the OP, Avengers: Endgame, Cars 3
     * 
     * private int getRandomRescueMovieId() {
     * int randomRescueMovieId = (int) (0 + (Math.random() * (NB_RESCUE_IDS - 0)));
     * return RESCUE_MOVIE_ID[randomRescueMovieId];
     * }
     */

    private final int RANDOM_PAGE_MIN = 1;
    private final int RANDOM_PAGE_MAX = 100; // I want one of the 100 first pages (the 2000 actual most popular films)

    private ArrayList<MovieInfos> getRandomCoherentMovies(String tmdbLanguage, int number,
            TmdbFetchOptions answerOptions, TmdbFetchOptions similaryOptions) {
        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();

        ArrayList<MovieInfos> similarMovieList = null;
        MovieInfos movie = null;
        while (similarMovieList == null) {

            movie = getOneRandomValidMovie(tmdbLanguage, answerOptions);

            try {
                similarMovieList = getSimilarValidMovies(movie, number - 1, tmdbLanguage, similaryOptions);
            } catch (NotEnoughSimilarMoviesInTMDBException e) {
                System.err.println(e.getMessage());
                movieList.clear();
            }
        }

        movieList.add(movie);
        movieList.addAll(similarMovieList);

        return movieList;
    }

    private ArrayList<MovieInfos> getSimilarValidMovies(MovieInfos movie, int number, String tmdbLanguage,
            TmdbFetchOptions options) throws NotEnoughSimilarMoviesInTMDBException {
        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();
        int page_number = 1;
        while (movieList.size() < number) {
            PageResult page = getSimilarMoviesPage(movie.id, tmdbLanguage, page_number);
            if (page == null)
                throw new NotEnoughSimilarMoviesInTMDBException();

            // remove movies where we don't have title, description or image
            ArrayList<MovieListResult> filtredResults = getFiltredResultListInPage(page, options);

            for (MovieListResult result : filtredResults) {
                MovieInfos similar;
                try {
                    similar = isaValidMovie(result, tmdbLanguage, options);
                    movieList.add(similar);
                    if (movieList.size() >= number)
                        break;
                } catch (NotaValidMovieException e) {
                    System.err.println(e.getMessage());
                }
            }

            page_number++;
        }
        return movieList;
    }

    private MovieInfos getOneRandomValidMovie(String tmdbLanguage, TmdbFetchOptions options) {
        MovieInfos movie = null;
        while (movie == null) {
            PageResult page = getRandomPopularMoviesPage(tmdbLanguage);

            // remove movies where we don't have title, description or image
            ArrayList<MovieListResult> filtredResults = getFiltredResultListInPage(page, options);
            Collections.shuffle(filtredResults);

            for (MovieListResult result : filtredResults) {
                try {
                    movie = isaValidMovie(result, tmdbLanguage, options);
                } catch (NotaValidMovieException e) {
                    System.err.println(e.getMessage());
                }

            }
        }
        return movie;
    }

    private MovieInfos isaValidMovie(MovieListResult result, String tmdbLanguage, TmdbFetchOptions options)
            throws NotaValidMovieException {
        MovieInfos movie = null;

        String url = "https://api.themoviedb.org/3/movie/" + result.id + "?api_key="
                + BackendApplication.API_KEY
                + "&language="
                + tmdbLanguage;

        RestTemplate rt = new RestTemplate();
        MovieInfos tmp = null;
        try {
            tmp = rt.getForObject(url, MovieInfos.class);
        } catch (final HttpClientErrorException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
        }

        // check if it's a valid movie
        if (tmp != null && (!options.isBudget() || tmp.budget > 0) && (!options.isRevenue() || tmp.revenue > 0)
                && (!options.isRelease_date() || (tmp.release_date != null
                        && tmp.release_date != "")))
            movie = tmp;

        if (movie == null)
            throw new NotaValidMovieException();

        return movie;
    }

    private PageResult getRandomPopularMoviesPage(String tmdbLanguage) {
        PageResult page = null;
        RestTemplate rt = new RestTemplate();
        while (page == null) {
            int randomPage = (int) (RANDOM_PAGE_MIN + (Math.random() * (RANDOM_PAGE_MAX - RANDOM_PAGE_MIN)));
            String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + BackendApplication.API_KEY
                    + "&language="
                    + tmdbLanguage + "&page="
                    + randomPage;

            try {
                page = rt.getForObject(url, PageResult.class);
            } catch (final HttpClientErrorException e) {
                System.out.println(e.getStatusCode());
                System.out.println(e.getResponseBodyAsString());
            }

        }
        return page;
    }

    private PageResult getSimilarMoviesPage(int movieId, String tmdbLanguage, int num_page) {
        PageResult page = null;
        RestTemplate rt = new RestTemplate();
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/similar?api_key=" + BackendApplication.API_KEY
                + "&language=" + tmdbLanguage + "&page=" + num_page;
        try {
            page = rt.getForObject(url, PageResult.class);
        } catch (final HttpClientErrorException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
        }

        return page;
    }

    private ArrayList<MovieListResult> getFiltredResultListInPage(PageResult page, TmdbFetchOptions options) {
        return (ArrayList<MovieListResult>) page.results
                .stream()
                .filter(
                        (m) -> (!options.isTitle() || (m.title != null && m.title != ""))
                                && (!options.isImage() || (m.backdrop_path != null && m.backdrop_path != ""))
                                && (!options.isDescription() || (m.overview != null && m.overview != "")))
                .collect(Collectors.toList());
    }

}