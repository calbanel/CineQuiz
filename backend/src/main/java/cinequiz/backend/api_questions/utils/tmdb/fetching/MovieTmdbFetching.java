package cinequiz.backend.api_questions.utils.tmdb.fetching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import cinequiz.backend.BackendApplication;
import cinequiz.backend.api_questions.exceptions.CastUnavailableInTMDBException;
import cinequiz.backend.api_questions.exceptions.NotEnoughPeoplesInCast;
import cinequiz.backend.api_questions.exceptions.NotEnoughSimilarShowsInTMDBException;
import cinequiz.backend.api_questions.exceptions.NotaValidShowException;
import cinequiz.backend.api_questions.utils.tmdb.fetching.options.MovieTmdbFetchOptions;
import cinequiz.backend.api_questions.utils.tmdb.fetching.options.PeopleTmdbFetchOptions;
import cinequiz.backend.api_questions.utils.tmdb.objects.people.credit.ShowCredit;
import cinequiz.backend.api_questions.utils.tmdb.objects.people.credit.ShowCreditPage;
import cinequiz.backend.api_questions.utils.tmdb.objects.show.cast.Cast;
import cinequiz.backend.api_questions.utils.tmdb.objects.show.cast.CastMember;
import cinequiz.backend.api_questions.utils.tmdb.objects.show.cast.CastPage;
import cinequiz.backend.api_questions.utils.tmdb.objects.show.cast.Crew;
import cinequiz.backend.api_questions.utils.tmdb.objects.show.movie.MovieInfos;
import cinequiz.backend.api_questions.utils.tmdb.objects.show.movie.list.MoviePage;
import cinequiz.backend.api_questions.utils.tmdb.objects.show.movie.list.MovieResult;

public class MovieTmdbFetching {
    public static ArrayList<MovieInfos> getRandomCoherentMovies(String tmdbLanguage, int number,
            MovieTmdbFetchOptions answerOptions, MovieTmdbFetchOptions similaryOptions) {
        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();

        ArrayList<MovieInfos> similarMovieList = null;
        MovieInfos movie = null;

        // try to find a movie with an similar movie list valid in tmdb
        while (similarMovieList == null) {

            movie = getOneRandomValidMovie(tmdbLanguage, answerOptions);

            try {
                similarMovieList = getSimilarValidMovies(movie, number - 1, tmdbLanguage, similaryOptions);
            } catch (NotEnoughSimilarShowsInTMDBException e) {
                System.err.println(e.getMessage());
                movieList.clear();
            }
        }

        // original movie at index 0
        movieList.add(movie);
        movieList.addAll(similarMovieList);

        return movieList;
    }

    private static ArrayList<MovieInfos> getSimilarValidMovies(MovieInfos movie, int number, String tmdbLanguage,
            MovieTmdbFetchOptions options) throws NotEnoughSimilarShowsInTMDBException {
        ArrayList<MovieInfos> movieList = new ArrayList<MovieInfos>();

        // it can have several pages for similar movies in tmdb, we start at the first
        // page
        int page_number = 1;
        // we need to fill the list
        while (movieList.size() < number) {

            // try to get the similar movie page
            MoviePage page = getSimilarMoviesPage(movie.id, tmdbLanguage, page_number);
            // we failed to get a similar movie page then we throw an exception
            if (page == null)
                throw new NotEnoughSimilarShowsInTMDBException();

            // only keeps the movies of the same language
            ArrayList<MovieResult> filtredResults = (ArrayList<MovieResult>) page.results.stream()
                    .filter(m -> m.original_language == movie.original_language).collect(Collectors.toList());

            // remove unvalid similar movies
            filtredResults = getFiltredMovieResultListInPage(page, options);

            Collections.shuffle(filtredResults);

            // browse valid similar movies
            for (MovieResult result : filtredResults) {
                MovieInfos similar = null;
                try {
                    // check the movie and get all of his infos
                    similar = getMovieIfValid(result, tmdbLanguage, options);
                } catch (NotaValidShowException e) {
                    System.err.println(e.getMessage());
                }

                // if similar isn't valid or if the target value is an duplicata we go to the
                // next value
                ArrayList<MovieInfos> dontWantDuplicata = new ArrayList<MovieInfos>(movieList);
                dontWantDuplicata.add(movie);
                if (similar == null || options.checkDuplicate(similar, dontWantDuplicata))
                    continue;

                movieList.add(similar);

                // if we have enough movies we stop browsing the page.
                if (movieList.size() >= number)
                    break;
            }

            // go to the next similar movie page
            page_number++;
        }

        return movieList;
    }

    private static MovieInfos getOneRandomValidMovie(String tmdbLanguage, MovieTmdbFetchOptions options) {
        MovieInfos movie = null;

        // as long as we don't have a valid movie, we go through the pages of popular
        // movies randomly
        while (movie == null) {
            MoviePage page = getRandomPopularMoviesPage(tmdbLanguage);

            // remove unvalid movies of the popular movie page
            ArrayList<MovieResult> filtredResults = getFiltredMovieResultListInPage(page, options);
            Collections.shuffle(filtredResults);

            for (MovieResult result : filtredResults) {
                try {
                    // if the movie is valid we stop the research
                    movie = getMovieIfValid(result, tmdbLanguage, options);
                    break;
                } catch (NotaValidShowException e) {
                    System.err.println(e.getMessage());
                }

            }
        }
        return movie;
    }

    private static MovieInfos getMovieIfValid(MovieResult result, String tmdbLanguage,
            MovieTmdbFetchOptions options)
            throws NotaValidShowException {
        MovieInfos movie = null;

        // fetch the detailed informations on a movie
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

        // check if it's a valid movie according to the target values
        if (tmp != null && (!options.isBudget() || tmp.budget > 0) && (!options.isRevenue() || tmp.revenue > 0)
                && (!options.isReleaseDate() || (tmp.release_date != null
                        && tmp.release_date != "")))
            movie = tmp;

        // throw exception if we failed to fetch the movie informations or if the target
        // values aren't available
        if (movie == null)
            throw new NotaValidShowException();

        return movie;
    }

    private static final int RANDOM_PAGE_MIN = 1;
    private static final int RANDOM_PAGE_MAX = 100; // I want one of the 100 first pages (the 2000 actual most popular
                                                    // // films)

    private static MoviePage getRandomPopularMoviesPage(String tmdbLanguage) {
        MoviePage page = null;
        RestTemplate rt = new RestTemplate();

        // as long as we don't have a valid page, we go through the pages of popular
        // randomly
        while (page == null) {
            int randomPage = BackendApplication.random(RANDOM_PAGE_MIN, RANDOM_PAGE_MAX);
            String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + BackendApplication.API_KEY
                    + "&language="
                    + tmdbLanguage + "&page="
                    + randomPage;

            try {
                page = rt.getForObject(url, MoviePage.class);
            } catch (final HttpClientErrorException e) {
                System.out.println(e.getStatusCode());
                System.out.println(e.getResponseBodyAsString());
            }

        }
        return page;
    }

    private static MoviePage getSimilarMoviesPage(int movieId, String tmdbLanguage, int num_page) {
        MoviePage page = null;
        RestTemplate rt = new RestTemplate();
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/similar?api_key=" + BackendApplication.API_KEY
                + "&language=" + tmdbLanguage + "&page=" + num_page;
        try {
            page = rt.getForObject(url, MoviePage.class);
        } catch (final HttpClientErrorException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
        }

        // null if the target similar movie page isn't valid
        return page;
    }

    private static ArrayList<MovieResult> getFiltredMovieResultListInPage(MoviePage page,
            MovieTmdbFetchOptions options) {
        return (ArrayList<MovieResult>) page.results
                .stream()
                .filter(
                        (m) -> (!options.isTitle() || (m.title != null && m.title != ""))
                                && (!options.isPoster() || (m.poster_path != null && m.poster_path != ""))
                                && (!options.isBackdrop() || (m.backdrop_path != null && m.backdrop_path != ""))
                                && (!options.isDescription() || (m.overview != null && m.overview != "")))
                .collect(Collectors.toList());
    }

    private static ArrayList<CastMember> getFiltredCastListInPage(CastPage page,
            PeopleTmdbFetchOptions options, int tmdbgenre) {
        ArrayList<Cast> cast = (ArrayList<Cast>) page.cast.stream()
                .filter((c) -> (!options.isProfile_path() || (c.profile_path != null && c.profile_path != ""))
                        && (!options.isName() || (c.name != null && c.name != ""))
                        && (!options.isGender() || c.gender == tmdbgenre))
                .collect(Collectors.toList());
        ArrayList<Crew> crew = (ArrayList<Crew>) page.crew.stream()
                .filter((c) -> (!options.isProfile_path() || (c.profile_path != null && c.profile_path != ""))
                        && (!options.isName() || (c.name != null && c.name != ""))
                        && (!options.isGender() || c.gender == tmdbgenre))
                .collect(Collectors.toList());
        ArrayList<CastMember> members = new ArrayList<CastMember>();
        members.addAll(cast);
        members.addAll(crew);
        return members;
    }

    private static ArrayList<CastMember> getRandomCoherentPeoplesInvolvedInThisMovie(int movieId,
            String tmdbLanguage,
            int number, PeopleTmdbFetchOptions options, int tmdbgenre, int similarMovieId)
            throws CastUnavailableInTMDBException, NotEnoughPeoplesInCast {
        ArrayList<CastMember> peoples = new ArrayList<CastMember>();

        CastPage castPage = getMovieCastPage(movieId, tmdbLanguage);
        // if target cast page isn't valid, throw exception
        if (castPage == null)
            throw new CastUnavailableInTMDBException();

        // only keeps peoples where we have the target values
        ArrayList<CastMember> castFiltered = getFiltredCastListInPage(castPage, options, tmdbgenre);

        // we firt want the most popular casts, we want known names
        castFiltered.sort((a, b) -> new Comparator<CastMember>() {
            @Override
            public int compare(CastMember o1, CastMember o2) {
                if (o1.popularity == o2.popularity)
                    return 0;

                return o1.popularity < o2.popularity ? 1 : -1;
            }

        }.compare(a, b));

        // browse the clean cast list
        for (CastMember c : castFiltered) {
            // add cast to the final list if he isn't in the similar movie
            if (!isCastIsInThisMovie(c.id, similarMovieId, tmdbLanguage)) {
                if (peoples.stream().filter(p -> p.name.equals(c.name)).findFirst().isEmpty())
                    peoples.add(c);
            }

            // stop the research when we have the number of peoples we want
            if (peoples.size() >= number)
                break;
        }

        // if there not enough peoples in the final list, throw exception
        if (peoples.size() < number)
            throw new NotEnoughPeoplesInCast();

        return peoples;
    }

    private static CastPage getMovieCastPage(int movieId, String tmdbLanguage) {
        CastPage page = null;
        RestTemplate rt = new RestTemplate();
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=" + BackendApplication.API_KEY
                + "&language=" + tmdbLanguage;
        try {
            page = rt.getForObject(url, CastPage.class);
        } catch (final HttpClientErrorException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
        }

        // null if the target cast page isn't valid
        return page;
    }

    private static boolean isCastIsInThisMovie(int personId, int movieId, String tmdbLanguage) {
        boolean isIn = false;

        // get all the movies have participated the person
        ShowCreditPage creditPage = getPeopleMovieCreditPage(personId, tmdbLanguage);
        if (creditPage != null) {
            ArrayList<ShowCredit> list = new ArrayList<ShowCredit>();
            list.addAll(creditPage.cast);
            list.addAll(creditPage.crew);
            list = (ArrayList<ShowCredit>) list.stream().filter(m -> m.id == movieId || m.media_type == "movie")
                    .collect(Collectors.toList());
            if (list.size() > 0)
                isIn = true;
        }

        return isIn;
    }

    private static ShowCreditPage getPeopleMovieCreditPage(int personId, String tmdbLanguage) {
        ShowCreditPage page = null;
        RestTemplate rt = new RestTemplate();
        String url = "https://api.themoviedb.org/3/person/" + personId + "/combined_credits?api_key="
                + BackendApplication.API_KEY
                + "&language=" + tmdbLanguage;
        try {
            page = rt.getForObject(url, ShowCreditPage.class);
        } catch (final HttpClientErrorException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
        }
        // null if the target cast page isn't valid
        return page;
    }

    public static ArrayList<CastMember> getRandomCoherentPeopleListInTheseMovies(int movieId,
            int numberOfPeoplesInMovie,
            int similarMovieId, int numberOfPeoplesInSimilarMovie, String tmdbLanguage) {
        ArrayList<CastMember> cast = null;
        int randomGender = BackendApplication.random(1, 2);
        try {
            PeopleTmdbFetchOptions panswerOptions = new PeopleTmdbFetchOptions(true, true, true);
            ArrayList<CastMember> answer = getRandomCoherentPeoplesInvolvedInThisMovie(movieId,
                    tmdbLanguage, numberOfPeoplesInMovie, panswerOptions, randomGender, -1);
            PeopleTmdbFetchOptions psimilaryOptions = new PeopleTmdbFetchOptions(true, false, true);

            ArrayList<CastMember> similaryCast = getRandomCoherentPeoplesInvolvedInThisMovie(similarMovieId,
                    tmdbLanguage, numberOfPeoplesInSimilarMovie, psimilaryOptions, randomGender,
                    movieId);
            cast = new ArrayList<CastMember>();
            cast.addAll(answer);
            cast.addAll(similaryCast);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // null if we failed to get a valid cast for these movies
        return cast;
    }
}
