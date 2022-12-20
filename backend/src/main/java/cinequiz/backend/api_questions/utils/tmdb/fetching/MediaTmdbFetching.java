package cinequiz.backend.api_questions.utils.tmdb.fetching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import cinequiz.backend.BackendApplication;
import cinequiz.backend.api_questions.exceptions.CastUnavailableInTMDBException;
import cinequiz.backend.api_questions.exceptions.NotEnoughPeoplesInCast;
import cinequiz.backend.api_questions.exceptions.NotEnoughSimilarShowsInTMDBException;
import cinequiz.backend.api_questions.utils.tmdb.fetching.options.MediaTmdbFetchingOptions;
import cinequiz.backend.api_questions.utils.tmdb.fetching.options.PeopleTmdbFetchingOptions;
import cinequiz.backend.api_questions.utils.tmdb.model.MediaInfos;
import cinequiz.backend.api_questions.utils.tmdb.model.MediaType;
import cinequiz.backend.api_questions.utils.tmdb.model.show.cast.CastMember;
import cinequiz.backend.api_questions.utils.tmdb.model.show.cast.CastPage;
import cinequiz.backend.api_questions.utils.tmdb.model.show.list.MovieResultsPage;
import cinequiz.backend.api_questions.utils.tmdb.model.show.list.ResultsPage;

public class MediaTmdbFetching extends TmdbFetching {
    public static ArrayList<MediaInfos> getRandomCoherentMovies(String tmdbLanguage, int number,
            MediaTmdbFetchingOptions answerOptions, MediaTmdbFetchingOptions similaryOptions, MediaType mediaType) {
        ArrayList<MediaInfos> movieList = new ArrayList<MediaInfos>();

        ArrayList<MediaInfos> similarMovieList = null;
        MediaInfos movie = null;

        // try to find a movie with an similar movie list valid in tmdb
        while (similarMovieList == null) {

            movie = getOneRandomValidMovie(tmdbLanguage, answerOptions, mediaType);

            try {
                similarMovieList = getSimilarValidMovies(movie, number - 1, tmdbLanguage, similaryOptions, mediaType);
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

    private static ArrayList<MediaInfos> getSimilarValidMovies(MediaInfos movie, int number, String tmdbLanguage,
            MediaTmdbFetchingOptions options, MediaType mediaType) throws NotEnoughSimilarShowsInTMDBException {
        ArrayList<MediaInfos> movieList = new ArrayList<MediaInfos>();

        // it can have several pages for similar movies in tmdb, we start at the first
        // page
        int page_number = 1;
        // we need to fill the list
        while (movieList.size() < number) {

            // try to get the similar movie page
            List<? extends MediaInfos> results = getSimilarMoviesPage(movie.getId(), tmdbLanguage, page_number,
                    mediaType);
            // we failed to get a similar movie page then we throw an exception
            if (results == null)
                throw new NotEnoughSimilarShowsInTMDBException();

            // only keeps the movies of the same language
            List<? extends MediaInfos> filtredResults = (ArrayList<? extends MediaInfos>) results.stream()
                    .filter(m -> m.getOriginalLanguage().equals(movie.getOriginalLanguage()))
                    .collect(Collectors.toList());

            // remove unvalid similar movies
            filtredResults = getFiltredMovieInfosListInPage(filtredResults, options);

            Collections.shuffle(filtredResults);

            // browse valid similar movies
            for (MediaInfos result : filtredResults) {
                // if the target value is an duplicata we go next
                List<MediaInfos> dontWantDuplicata = new ArrayList<MediaInfos>(movieList);
                dontWantDuplicata.add(movie);
                if (options.checkDuplicate(result, dontWantDuplicata))
                    continue;

                movieList.add(result);

                // if we have enough movies we stop browsing the page.
                if (movieList.size() >= number)
                    break;
            }

            // go to the next similar movie page
            page_number++;
        }

        return movieList;
    }

    private static MediaInfos getOneRandomValidMovie(String tmdbLanguage, MediaTmdbFetchingOptions options,
            MediaType mediaType) {
        MediaInfos movie = null;

        // as long as we don't have a valid movie, we go through the pages of popular
        // movies randomly
        while (movie == null) {
            List<? extends MediaInfos> page = getRandomPopularMoviesPage(tmdbLanguage, mediaType);

            // remove unvalid movies of the popular movie page
            List<? extends MediaInfos> filtredResults = getFiltredMovieInfosListInPage(page, options);
            Collections.shuffle(filtredResults);

            if (filtredResults.size() > 0)
                movie = filtredResults.get(0);
        }
        return movie;
    }

    private static final int RANDOM_PAGE_MIN = 1;
    private static final int RANDOM_PAGE_MAX = 100; // I want one of the 100 first pages (the 2000 actual most popular
                                                    // // films)

    private static List<? extends MediaInfos> getRandomPopularMoviesPage(String tmdbLanguage, MediaType mediaType) {
        List<? extends MediaInfos> list = null;

        // as long as we don't have a valid page, we go through the pages of popular
        // randomly
        while (list == null) {
            int randomPage = BackendApplication.random(RANDOM_PAGE_MIN, RANDOM_PAGE_MAX);
            String url = "https://api.themoviedb.org/3/" + mediaType.getTmdbPrefix() + "/popular?api_key="
                    + TmdbFetching.API_KEY
                    + "&language="
                    + tmdbLanguage + "&page="
                    + randomPage;

            list = getMediaInfosListFromAnResultPage(url, mediaType);
        }
        return list;
    }

    private static List<? extends MediaInfos> getSimilarMoviesPage(int movieId, String tmdbLanguage, int num_page,
            MediaType mediaType) {
        List<? extends MediaInfos> list = null;

        String url = "https://api.themoviedb.org/3/" + mediaType.getTmdbPrefix() + "/" + movieId + "/similar?api_key="
                + TmdbFetching.API_KEY
                + "&language=" + tmdbLanguage + "&page=" + num_page;
        list = getMediaInfosListFromAnResultPage(url, mediaType);

        // null if the target similar movie page isn't valid
        return list;
    }

    private static List<? extends MediaInfos> getFiltredMovieInfosListInPage(List<? extends MediaInfos> list,
            MediaTmdbFetchingOptions options) {
        return (List<? extends MediaInfos>) list
                .stream()
                .filter(
                        (m) -> (!options.isTitle() || (m.getTitle() != null && !m.getTitle().equals("")))
                                && (!options.isPoster() || (m.getPosterPath() != null && !m.getPosterPath().equals("")))
                                && (!options.isBackdrop()
                                        || (m.getBackdropPath() != null && !m.getBackdropPath().equals("")))
                                && (!options.isDescription()
                                        || (m.getOverview() != null && !m.getOverview().equals("")))
                                && (!options.isReleaseDate()
                                        || (m.getReleaseDate() != null && !m.getReleaseDate().equals(""))))
                .collect(Collectors.toList());
    }

    private static ArrayList<CastMember> getRandomCoherentPeoplesInvolvedInThisMovie(int movieId,
            String tmdbLanguage, int number, PeopleTmdbFetchingOptions options, int tmdbgenre, MediaType mediaType)
            throws CastUnavailableInTMDBException, NotEnoughPeoplesInCast {
        return getRandomCoherentPeoplesInvolvedInThisMovie(movieId, tmdbLanguage, number, options, tmdbgenre, mediaType,
                -1);
    }

    private static ArrayList<CastMember> getRandomCoherentPeoplesInvolvedInThisMovie(int movieId,
            String tmdbLanguage,
            int number, PeopleTmdbFetchingOptions options, int tmdbgenre, MediaType mediaType, int similarMovieId)
            throws CastUnavailableInTMDBException, NotEnoughPeoplesInCast {
        ArrayList<CastMember> peoples = new ArrayList<CastMember>();

        CastPage castPage = getMovieCastPage(movieId, tmdbLanguage, mediaType);
        // if target cast page isn't valid, throw exception
        if (castPage == null)
            throw new CastUnavailableInTMDBException();

        // only keeps peoples where we have the target values
        ArrayList<CastMember> castFiltered = PeopleTmdbFetching.getFiltredCastListInPage(castPage, options, tmdbgenre);

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
            if (!PeopleTmdbFetching.isCastIsInThisShow(c.id, similarMovieId, tmdbLanguage, mediaType.getTmdbPrefix())) {
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

    private static CastPage getMovieCastPage(int movieId, String tmdbLanguage, MediaType mediaType) {
        CastPage page = null;
        RestTemplate rt = new RestTemplate();
        String url = "https://api.themoviedb.org/3/" + mediaType.getTmdbPrefix() + "/" + movieId + "/credits?api_key="
                + TmdbFetching.API_KEY
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

    public static ArrayList<CastMember> getRandomCoherentPeopleListInTheseMovies(int movieId,
            int numberOfPeoplesInMovie, int similarMovieId, int numberOfPeoplesInSimilarMovie, String tmdbLanguage,
            MediaType mediaType) {
        ArrayList<CastMember> cast = null;
        int randomGender = BackendApplication.random(1, 2);
        try {
            PeopleTmdbFetchingOptions panswerOptions = new PeopleTmdbFetchingOptions(true, true, true);
            ArrayList<CastMember> answer = getRandomCoherentPeoplesInvolvedInThisMovie(movieId,
                    tmdbLanguage, numberOfPeoplesInMovie, panswerOptions, randomGender, mediaType);
            PeopleTmdbFetchingOptions psimilaryOptions = new PeopleTmdbFetchingOptions(true, false, true);

            ArrayList<CastMember> similaryCast = getRandomCoherentPeoplesInvolvedInThisMovie(similarMovieId,
                    tmdbLanguage, numberOfPeoplesInSimilarMovie, psimilaryOptions, randomGender, mediaType,
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

    private static List<? extends MediaInfos> getMediaInfosListFromAnResultPage(String url, MediaType mediaType) {
        List<? extends MediaInfos> list = null;
        ResultsPage<? extends MediaInfos> page = null;
        RestTemplate rt = new RestTemplate();
        try {
            if (mediaType == MediaType.MOVIE) {
                page = rt.getForObject(url, MovieResultsPage.class);
            } else if (mediaType == MediaType.TV) {
                // TvResultsPage page = rt.getForObject(url, TvResultsPage.class);
                // list = page.getResults();
            }
        } catch (final HttpClientErrorException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
        }

        if (page != null)
            list = page.getResults();

        return list;
    }
}
