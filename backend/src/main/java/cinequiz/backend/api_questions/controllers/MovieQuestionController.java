package cinequiz.backend.api_questions.controllers;

import java.util.ArrayList;
import java.util.Comparator;
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
import cinequiz.backend.api_questions.exceptions.LanguageNotSupportedException;
import cinequiz.backend.api_questions.exceptions.MovieCastUnavailableInTMDBException;
import cinequiz.backend.api_questions.exceptions.NotEnoughPeoplesInMovieCast;
import cinequiz.backend.api_questions.exceptions.NotEnoughSimilarMoviesInTMDBException;
import cinequiz.backend.api_questions.exceptions.NotaValidMovieException;
import cinequiz.backend.api_questions.mcq.Choices;
import cinequiz.backend.api_questions.mcq.MCQQuestion;
import cinequiz.backend.api_questions.questions.MovieQuestion;
import cinequiz.backend.api_questions.tmdb_objects.MovieCast;
import cinequiz.backend.api_questions.tmdb_objects.MovieCastPage;
import cinequiz.backend.api_questions.tmdb_objects.MovieCredit;
import cinequiz.backend.api_questions.tmdb_objects.MovieCreditPage;
import cinequiz.backend.api_questions.tmdb_objects.MovieInfos;
import cinequiz.backend.api_questions.tmdb_objects.MovieListResult;
import cinequiz.backend.api_questions.utils.Language;
import cinequiz.backend.api_questions.utils.MovieTmdbFetchOptions;
import cinequiz.backend.api_questions.utils.PeopleTmdbFetchOptions;
import cinequiz.backend.api_questions.tmdb_objects.MovieListPage;
import edu.emory.mathcs.backport.java.util.Collections;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/cinequiz/questions/movie", method = RequestMethod.GET)
public class MovieQuestionController {

    private final int NB_CHOICES_IN_MCQ = 4;
    private final int NB_DEFINED_QUESTIONS = 7;

    @ApiOperation(value = "Gets a random mcq about a movie")
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
            case 4:
                return takePart(language);
            case 5:
                return doesntTakePart(language);
            case 6:
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
        ArrayList<MovieCast> cast = null;
        try {
            while (cast == null) {
                MovieTmdbFetchOptions manswerOptions = new MovieTmdbFetchOptions(true, true, false, false, false,
                        false);
                MovieTmdbFetchOptions msimilaryOptions = new MovieTmdbFetchOptions(false, false, false, false, false,
                        false);
                movieList = getRandomCoherentMovies(internLanguage.getTmdbLanguage(), 2, manswerOptions,
                        msimilaryOptions);

                int randomGender = (int) (1 + (Math.random() * (3 - 1)));
                try {
                    PeopleTmdbFetchOptions panswerOptions = new PeopleTmdbFetchOptions(true, true, true);
                    ArrayList<MovieCast> answer = getRandomCoherentPeoplesInvolvedInThisMovie(movieList.get(0).id,
                            internLanguage.getTmdbLanguage(), 1, panswerOptions, randomGender, -1);
                    PeopleTmdbFetchOptions psimilaryOptions = new PeopleTmdbFetchOptions(true, false, true);

                    ArrayList<MovieCast> similaryCast = getRandomCoherentPeoplesInvolvedInThisMovie(movieList.get(1).id,
                            internLanguage.getTmdbLanguage(), 3, psimilaryOptions, randomGender,
                            movieList.get(0).id);

                    cast = new ArrayList<MovieCast>();
                    cast.addAll(answer);
                    cast.addAll(similaryCast);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MovieInfos movieOfAnswer = movieList.get(0);
        MovieCast answer = cast.get(0);
        Collections.shuffle(cast);
        String[] choices = { cast.get(0).name, cast.get(1).name, cast.get(2).name, cast.get(3).name };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(movieOfAnswer.backdrop_path, movieOfAnswer.title,
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
        ArrayList<MovieCast> cast = null;
        try {
            while (cast == null) {
                MovieTmdbFetchOptions manswerOptions = new MovieTmdbFetchOptions(true, true, false, false, false,
                        false);
                MovieTmdbFetchOptions msimilaryOptions = new MovieTmdbFetchOptions(false, false, false, false, false,
                        false);
                movieList = getRandomCoherentMovies(internLanguage.getTmdbLanguage(), 2, manswerOptions,
                        msimilaryOptions);

                int randomGender = (int) (1 + (Math.random() * (3 - 1)));
                try {
                    PeopleTmdbFetchOptions panswerOptions = new PeopleTmdbFetchOptions(true, true, true);
                    ArrayList<MovieCast> answer = getRandomCoherentPeoplesInvolvedInThisMovie(movieList.get(1).id,
                            internLanguage.getTmdbLanguage(), 1, panswerOptions, randomGender, movieList.get(0).id);
                    PeopleTmdbFetchOptions psimilaryOptions = new PeopleTmdbFetchOptions(true, false, true);
                    ArrayList<MovieCast> similaryCast = getRandomCoherentPeoplesInvolvedInThisMovie(movieList.get(0).id,
                            internLanguage.getTmdbLanguage(), 3, psimilaryOptions, randomGender, -1);

                    cast = new ArrayList<MovieCast>(answer);
                    cast.addAll(similaryCast);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MovieInfos movieOfAnswer = movieList.get(0);
        MovieCast answer = cast.get(0);
        Collections.shuffle(cast);
        String[] choices = { cast.get(0).name, cast.get(1).name, cast.get(2).name, cast.get(3).name };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(movieOfAnswer.backdrop_path, movieOfAnswer.title,
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
            movieList = getRandomCoherentMovies(internLanguage.getTmdbLanguage(), NB_CHOICES_IN_MCQ, answerOptions,
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

    /*
     *
     * TMDB fetching functions
     *
     */

    private final int RANDOM_PAGE_MIN = 1;
    private final int RANDOM_PAGE_MAX = 100; // I want one of the 100 first pages (the 2000 actual most popular films)

    private ArrayList<MovieInfos> getRandomCoherentMovies(String tmdbLanguage, int number,
            MovieTmdbFetchOptions answerOptions, MovieTmdbFetchOptions similaryOptions) {
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
            MovieTmdbFetchOptions options) throws NotEnoughSimilarMoviesInTMDBException {
        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();
        int page_number = 1;
        while (movieList.size() < number) {
            MovieListPage page = getSimilarMoviesPage(movie.id, tmdbLanguage, page_number);
            if (page == null)
                throw new NotEnoughSimilarMoviesInTMDBException();

            // keeps the movies of the same language
            ArrayList<MovieListResult> filtredResults = (ArrayList<MovieListResult>) page.results.stream()
                    .filter(m -> m.original_language == movie.original_language).collect(Collectors.toList());

            // remove movies where we don't have title, description or image
            filtredResults = getFiltredMovieResultListInPage(page, options);

            for (MovieListResult result : filtredResults) {
                MovieInfos similar;
                try {
                    similar = isaValidMovie(result, tmdbLanguage, options);
                    if (checkDuplicate(similar, movie, movieList, options))
                        continue;
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

    private boolean checkDuplicate(MovieInfos result, MovieInfos original, ArrayList<MovieInfos> similarList,
            MovieTmdbFetchOptions options) {
        boolean isDuplicate = false;

        if (options.isBudget()) {
            if (result.budget == original.budget)
                isDuplicate = true;

            for (MovieInfos similar : similarList) {
                if (result.budget == similar.budget)
                    isDuplicate = true;
            }
        }

        if (options.isRevenue() && !isDuplicate) {
            if (result.revenue == original.revenue)
                isDuplicate = true;

            for (MovieInfos similar : similarList) {
                if (result.revenue == similar.revenue)
                    isDuplicate = true;
            }
        }

        if (options.isRelease_date() && !isDuplicate) {
            if (result.release_date == original.release_date)
                isDuplicate = true;

            for (MovieInfos similar : similarList) {
                if (result.release_date == similar.release_date)
                    isDuplicate = true;
            }
        }

        return isDuplicate;
    }

    private MovieInfos getOneRandomValidMovie(String tmdbLanguage, MovieTmdbFetchOptions options) {
        MovieInfos movie = null;
        while (movie == null) {
            MovieListPage page = getRandomPopularMoviesPage(tmdbLanguage);

            // remove movies where we don't have title, description or image
            ArrayList<MovieListResult> filtredResults = getFiltredMovieResultListInPage(page, options);
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

    private MovieInfos isaValidMovie(MovieListResult result, String tmdbLanguage, MovieTmdbFetchOptions options)
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

    private MovieListPage getRandomPopularMoviesPage(String tmdbLanguage) {
        MovieListPage page = null;
        RestTemplate rt = new RestTemplate();
        while (page == null) {
            int randomPage = (int) (RANDOM_PAGE_MIN + (Math.random() * (RANDOM_PAGE_MAX - RANDOM_PAGE_MIN)));
            String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + BackendApplication.API_KEY
                    + "&language="
                    + tmdbLanguage + "&page="
                    + randomPage;

            try {
                page = rt.getForObject(url, MovieListPage.class);
            } catch (final HttpClientErrorException e) {
                System.out.println(e.getStatusCode());
                System.out.println(e.getResponseBodyAsString());
            }

        }
        return page;
    }

    private MovieListPage getSimilarMoviesPage(int movieId, String tmdbLanguage, int num_page) {
        MovieListPage page = null;
        RestTemplate rt = new RestTemplate();
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/similar?api_key=" + BackendApplication.API_KEY
                + "&language=" + tmdbLanguage + "&page=" + num_page;
        try {
            page = rt.getForObject(url, MovieListPage.class);
        } catch (final HttpClientErrorException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
        }

        return page;
    }

    private ArrayList<MovieListResult> getFiltredMovieResultListInPage(MovieListPage page,
            MovieTmdbFetchOptions options) {
        return (ArrayList<MovieListResult>) page.results
                .stream()
                .filter(
                        (m) -> (!options.isTitle() || (m.title != null && m.title != ""))
                                && (!options.isImage() || (m.backdrop_path != null && m.backdrop_path != ""))
                                && (!options.isDescription() || (m.overview != null && m.overview != "")))
                .collect(Collectors.toList());
    }

    private ArrayList<MovieCast> getRandomCoherentPeoplesInvolvedInThisMovie(int movieId, String tmdbLanguage,
            int number, PeopleTmdbFetchOptions options, int tmdbgenre, int similarMovieId)
            throws MovieCastUnavailableInTMDBException, NotEnoughPeoplesInMovieCast {
        ArrayList<MovieCast> peoples = new ArrayList<MovieCast>();

        MovieCastPage castPage = getMovieCastPage(movieId, tmdbLanguage);
        if (castPage == null)
            throw new MovieCastUnavailableInTMDBException();

        ArrayList<MovieCast> castFiltered = (ArrayList<MovieCast>) castPage.cast.stream()
                .filter((c) -> (!options.isProfile_path() || (c.profile_path != null && c.profile_path != ""))
                        && (!options.isName() || (c.name != null && c.name != ""))
                        && (!options.isGender() || c.gender == tmdbgenre))
                .collect(Collectors.toList());

        if (castFiltered.size() < number)
            throw new NotEnoughPeoplesInMovieCast();

        castFiltered.sort((a, b) -> new Comparator<MovieCast>() {
            @Override
            public int compare(MovieCast o1, MovieCast o2) {
                if (o1.popularity == o2.popularity)
                    return 0;

                return o1.popularity < o2.popularity ? 1 : -1;
            }

        }.compare(a, b));

        int i = 0;
        while (peoples.size() < number) {
            MovieCast c = castFiltered.get(i);
            if (!isCastIsInThisMovie(c.id, similarMovieId, tmdbLanguage))
                peoples.add(castFiltered.get(i));

            i++;
            if (i >= castFiltered.size())
                throw new NotEnoughPeoplesInMovieCast();
        }

        return peoples;
    }

    private MovieCastPage getMovieCastPage(int movieId, String tmdbLanguage) {
        MovieCastPage page = null;
        RestTemplate rt = new RestTemplate();
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=" + BackendApplication.API_KEY
                + "&language=" + tmdbLanguage;
        try {
            page = rt.getForObject(url, MovieCastPage.class);
        } catch (final HttpClientErrorException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
        }

        return page;
    }

    private boolean isCastIsInThisMovie(int personId, int movieId, String tmdbLanguage) {
        boolean isIn = false;
        MovieCreditPage creditPage = getPeopleMovieCreditPage(personId, tmdbLanguage);
        if (creditPage != null) {
            ArrayList<MovieCredit> list = new ArrayList<MovieCredit>();
            list.addAll(creditPage.cast);
            list.addAll(creditPage.crew);
            for (MovieCredit credit : list) {
                if (credit.id == movieId)
                    isIn = true;
            }
        }

        return isIn;
    }

    private MovieCreditPage getPeopleMovieCreditPage(int personId, String tmdbLanguage) {
        MovieCreditPage page = null;
        RestTemplate rt = new RestTemplate();
        String url = "https://api.themoviedb.org/3/person/" + personId + "/movie_credits?api_key="
                + BackendApplication.API_KEY
                + "&language=" + tmdbLanguage;
        try {
            page = rt.getForObject(url, MovieCreditPage.class);
        } catch (final HttpClientErrorException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
        }

        return page;
    }

}