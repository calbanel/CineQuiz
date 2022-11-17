package cinequiz.backend.api_questions.controllers;

import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import cinequiz.backend.BackendApplication;
import cinequiz.backend.api_questions.mcq.Choices;
import cinequiz.backend.api_questions.mcq.MCQQuestion;
import cinequiz.backend.api_questions.questions.MovieQuestion;
import cinequiz.backend.api_questions.tmdb_objects.MovieInfos;
import cinequiz.backend.api_questions.tmdb_objects.MovieListResult;
import cinequiz.backend.api_questions.tmdb_objects.PageResult;

@RestController
@RequestMapping("/questions/movie")
public class MovieQuestionController {

    private final int NB_CHOICES = 4;
    // private final int[] RESCUE_MOVIE_ID = { 829280, 675, 299534, 260514 };
    // Enola Holmes 2, Harry Potter and the OP, Avengers: Endgame, Cars 3

    private final int RANDOM_PAGE_MIN = 1;
    private final int RANDOM_PAGE_MAX = 500; // I want one of the 100 first pages (the 10000 actual most popular films)
    private final int NB_RESULT_PER_PAGES_ON_TMDB = 20;

    private ArrayList<MovieInfos> getRandomPopularMovies(String langage, int number) {
        HashSet<Integer> movieIDlist = getRandomPopularMovieIDs(langage, number);

        RestTemplate rt = new RestTemplate();
        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();

        for (Integer movieId : movieIDlist) {
            String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + BackendApplication.API_KEY
                    + "&language="
                    + langage;
            MovieInfos movie = rt.getForObject(url, MovieInfos.class);
            movieList.add(movie);
        }

        return movieList;
    }

    private HashSet<Integer> getRandomPopularMovieIDs(String langage, int number) {
        PageResult page = getRandomPopularMoviesPage(langage);

        HashSet<Integer> idSet = new HashSet<Integer>();
        for (int i = 0; i < number; i++) {
            int id = -1;
            while (id < 0) {
                int randomMovieInPage = (int) (0 + (Math.random() * ((NB_RESULT_PER_PAGES_ON_TMDB - 1) - 0)));
                MovieListResult result = page.results.get(randomMovieInPage);
                if (!idSet.contains(result.id))
                    id = result.id;
            }
            idSet.add(id);
        }
        return idSet;
    }

    private PageResult getRandomPopularMoviesPage(String langage) {
        PageResult page = null;
        RestTemplate rt = new RestTemplate();
        while (page == null) {
            int randomPage = (int) (RANDOM_PAGE_MIN + (Math.random() * (RANDOM_PAGE_MAX - RANDOM_PAGE_MIN)));
            String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + BackendApplication.API_KEY
                    + "&language="
                    + langage + "&page="
                    + randomPage;
            page = rt.getForObject(url, PageResult.class);
        }
        return page;
    }

    @GetMapping("/")
    public String random_question() {
        return "movie";
    }

    @GetMapping(value = "/which-by-image", produces = { "application/json" })
    public ResponseEntity<MCQQuestion> which_by_image() {
        String langage = "fr-FR";

        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();
        try {
            movieList = getRandomPopularMovies(langage, NB_CHOICES);
        } catch (Exception e) {
            System.err.print(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Choices choicesObject = new Choices(movieList.get(0).title, movieList.get(1).title, movieList.get(2).title,
                movieList.get(3).title);

        int randomAnswer = (int) (0 + (Math.random() * ((NB_CHOICES - 1) - 0)));
        MovieInfos answer = movieList.get(randomAnswer);
        MCQQuestion mcq = new MCQQuestion(answer.poster_path, "", MovieQuestion.WHICH_BY_IMAGE.getQuestion(),
                choicesObject,
                answer.title);

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }

    @GetMapping(value = "/which-by-description", produces = { "application/json" })
    public ResponseEntity<MCQQuestion> which_by_description() {
        String langage = "fr-FR";

        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();
        try {
            movieList = getRandomPopularMovies(langage, NB_CHOICES);
        } catch (Exception e) {
            System.err.print(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Choices choicesObject = new Choices(movieList.get(0).title, movieList.get(1).title, movieList.get(2).title,
                movieList.get(3).title);

        int randomAnswer = (int) (0 + (Math.random() * ((NB_CHOICES - 1) - 0)));
        MovieInfos answer = movieList.get(randomAnswer);
        MCQQuestion mcq = new MCQQuestion("", answer.overview, MovieQuestion.WHICH_BY_DESCRIPTION.getQuestion(),
                choicesObject,
                answer.title);

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }

}