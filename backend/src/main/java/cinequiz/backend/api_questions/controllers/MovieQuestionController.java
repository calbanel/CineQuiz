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
import edu.emory.mathcs.backport.java.util.Collections;

@RestController
@RequestMapping("/questions/movie")
public class MovieQuestionController {

    private final int NB_CHOICES = 4;
    private final int[] RESCUE_MOVIE_ID = { 829280, 675, 299534, 260514 };
    // Enola Holmes 2, Harry Potter and the OP, Avengers: Endgame, Cars 3

    private final int RANDOM_PAGE_MIN = 1;
    private final int RANDOM_PAGE_MAX = 100; // I want one of the 100 first pages (the 2000 actual most popular films)
    private final int NB_RESULT_PER_PAGES_ON_TMDB = 20;

    private int getRandomPopularMovieID(String langage) throws CantFindIdException {
        PageResult list = null;
        RestTemplate rt = new RestTemplate();
        while (list == null) {
            int randomPage = (int) (RANDOM_PAGE_MIN + (Math.random() * (RANDOM_PAGE_MAX - RANDOM_PAGE_MIN)));
            String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + BackendApplication.API_KEY
                    + "&language="
                    + langage + "&page="
                    + randomPage;
            list = (PageResult) rt.getForObject(url, PageResult.class);
        }

        int id = -10; // If we don't find an ID in 10 try, it's certainly a bugged page
        while (id < 0) {
            id++;
            int randomFilmInPage = (int) (0 + (Math.random() * ((NB_RESULT_PER_PAGES_ON_TMDB - 1) - 0)));
            MovieListResult result = list.results.get(randomFilmInPage);
            id = result.id;
        }

        if (id <= 0)
            throw new CantFindIdException("getRandomPopularMovie()");

        System.out.println(id);
        return id;
    }

    @GetMapping("/")
    public String random_question() {
        return "movie";
    }

    @GetMapping(value = "/which-by-image", produces = { "application/json" })
    public ResponseEntity<MCQQuestion> which_by_image() {
        String langage = "fr-FR";

        HashSet<Integer> movieIDlist = new HashSet<Integer>();
        for (int i = 0; i < NB_CHOICES; i++) {
            try {
                while (true) {
                    int id = getRandomPopularMovieID(langage);
                    if (!movieIDlist.contains(id)) {
                        movieIDlist.add(id);
                        break;
                    }
                }
            } catch (CantFindIdException e) {
                System.err.println(e.getMessage());
                movieIDlist.add(RESCUE_MOVIE_ID[i]);
            }
        }

        RestTemplate rt = new RestTemplate();
        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();
        // cant find movie...
        for (Integer movieId : movieIDlist) {
            String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + BackendApplication.API_KEY
                    + "&language="
                    + langage;
            MovieInfos movie = rt.getForObject(url, MovieInfos.class);
            movieList.add(movie);
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

}