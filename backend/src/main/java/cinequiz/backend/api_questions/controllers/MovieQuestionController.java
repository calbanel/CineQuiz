package cinequiz.backend.api_questions.controllers;

import java.util.ArrayList;
import java.util.HashSet;
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

    private final int NB_CHOICES = 4;
    private final int NB_RESCUE_IDS = 4;
    private final int[] RESCUE_MOVIE_ID = { 829280, 675, 299534, 260514 };
    // Enola Holmes 2, Harry Potter and the OP, Avengers: Endgame, Cars 3

    private final int RANDOM_PAGE_MIN = 1;
    private final int RANDOM_PAGE_MAX = 500; // I want one of the 100 first pages (the 10000 actual most popular films)

    private ArrayList<MovieInfos> getRandomCoherentMovies(String tmdbLanguage, int number) {
        HashSet<Integer> movieIDlist = getRandomCoherentMovieIDs(tmdbLanguage, number);

        RestTemplate rt = new RestTemplate();
        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();

        for (Integer movieId : movieIDlist) {
            String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + BackendApplication.API_KEY
                    + "&language="
                    + tmdbLanguage;

            MovieInfos movie;
            try {
                movie = rt.getForObject(url, MovieInfos.class);
            } catch (final HttpClientErrorException e) {
                // if this movis had not an info page, we take the page of an knowed valid movie
                url = "https://api.themoviedb.org/3/movie/" + RESCUE_MOVIE_ID[movieList.size()] + "?api_key="
                        + BackendApplication.API_KEY
                        + "&language="
                        + tmdbLanguage;
                movie = rt.getForObject(url, MovieInfos.class);
            }
            movieList.add(movie);
        }

        return movieList;
    }

    private HashSet<Integer> getRandomCoherentMovieIDs(String tmdbLanguage, int number) {
        PageResult page = getRandomPopularMoviesPage(tmdbLanguage);

        // remove movies where we don't have title, description or image
        page.results = (ArrayList<MovieListResult>) page.results
                .stream()
                .filter(
                        (m) -> m.title != null && m.title != "" && m.backdrop_path != null && m.backdrop_path != ""
                                && m.overview != null
                                && m.overview != "")
                .collect(Collectors.toList());

        int randomMovieInPage = (int) (0 + (Math.random() * (page.results.size() - 0)));
        MovieListResult oneMovie = page.results.get(randomMovieInPage);

        HashSet<Integer> idSet = getSetOfSimilarMoviesId(oneMovie.id, number, tmdbLanguage);
        return idSet;
    }

    private HashSet<Integer> getSetOfSimilarMoviesId(int movieId, int number, String tmdbLanguage) {
        int id = movieId;
        PageResult pageOfSimilarMovies = getSimilarMoviesPage(id, tmdbLanguage);
        if (pageOfSimilarMovies == null)
            pageOfSimilarMovies = getSimilarMoviesPage(getRandomRescueMovieId(), tmdbLanguage);

        // remove movies where we don't have title, description or image
        pageOfSimilarMovies.results = (ArrayList<MovieListResult>) pageOfSimilarMovies.results
                .stream()
                .filter(
                        (m) -> m.title != null && m.title != "" && m.backdrop_path != null && m.backdrop_path != ""
                                && m.overview != null
                                && m.overview != "")
                .collect(Collectors.toList());

        // if the similar movies page of this movie had not enough valid movies, we take
        // similar movie page of a knowed valid movie
        if (pageOfSimilarMovies.results.size() < number - 1) {
            pageOfSimilarMovies = getSimilarMoviesPage(getRandomRescueMovieId(), tmdbLanguage);
            pageOfSimilarMovies.results = (ArrayList<MovieListResult>) pageOfSimilarMovies.results
                    .stream()
                    .filter(
                            (m) -> m.title != null && m.title != "" && m.backdrop_path != null && m.backdrop_path != ""
                                    && m.overview != null
                                    && m.overview != "")
                    .collect(Collectors.toList());
        }

        Collections.shuffle(pageOfSimilarMovies.results);
        HashSet<Integer> idSet = new HashSet<Integer>();
        idSet.add(id);
        for (int i = 0; i < number - 1; i++) {
            int similarMovieId = pageOfSimilarMovies.results.get(i).id;
            idSet.add(similarMovieId);
        }
        return idSet;
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

    private PageResult getSimilarMoviesPage(int movieId, String tmdbLanguage) {
        PageResult page = null;
        RestTemplate rt = new RestTemplate();
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/similar?api_key=" + BackendApplication.API_KEY
                + "&language=" + tmdbLanguage + "&page=1";
        try {
            page = rt.getForObject(url, PageResult.class);
        } catch (final HttpClientErrorException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
        }

        return page;
    }

    private int getRandomRescueMovieId() {
        int randomRescueMovieId = (int) (0 + (Math.random() * (NB_RESCUE_IDS - 0)));
        return RESCUE_MOVIE_ID[randomRescueMovieId];
    }

    private final int NB_QUESTIONS = 2;

    @GetMapping("/")
    public ResponseEntity<?> random_question(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        int randomQuestion = (int) (0 + (Math.random() * (NB_QUESTIONS - 0)));
        switch (randomQuestion) {
            case 0:
                return which_by_image(language);
            case 1:
                return which_by_description(language);
            default:
                return which_by_image(language);

        }
    }

    @GetMapping(value = "/which-by-image", produces = { "application/json" })
    public ResponseEntity<?> which_by_image(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        if (language.equals("fr"))
            internLanguage = Language.FR;
        else if (language.equals("en"))
            internLanguage = Language.EN;
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();
        try {
            movieList = getRandomCoherentMovies(internLanguage.getTmdbLanguage(), NB_CHOICES);
        } catch (Exception e) {
            System.err.print(e.getMessage());
            return new ResponseEntity<String>("This language isn't supported", HttpStatus.BAD_REQUEST);
        }

        Choices choicesObject = new Choices(movieList.get(0).title, movieList.get(1).title, movieList.get(2).title,
                movieList.get(3).title);

        int randomAnswer = (int) (0 + (Math.random() * (NB_CHOICES - 0)));
        MovieInfos answer = movieList.get(randomAnswer);
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
        if (language.equals("fr"))
            internLanguage = Language.FR;
        else if (language.equals("en"))
            internLanguage = Language.EN;
        else
            return new ResponseEntity<String>("This language isn't supported", HttpStatus.BAD_REQUEST);

        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();
        try {
            movieList = getRandomCoherentMovies(internLanguage.getTmdbLanguage(), NB_CHOICES);
        } catch (Exception e) {
            System.err.print(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Choices choicesObject = new Choices(movieList.get(0).title, movieList.get(1).title, movieList.get(2).title,
                movieList.get(3).title);

        int randomAnswer = (int) (0 + (Math.random() * (NB_CHOICES - 0)));
        MovieInfos answer = movieList.get(randomAnswer);
        MCQQuestion mcq = new MCQQuestion("", answer.overview,
                MovieQuestion.WHICH_BY_DESCRIPTION.getQuestion(internLanguage),
                choicesObject,
                answer.title);

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }

}