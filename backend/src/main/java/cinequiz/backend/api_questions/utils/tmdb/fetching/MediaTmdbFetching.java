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
import cinequiz.backend.api_questions.utils.tmdb.model.media.MediaInfos;
import cinequiz.backend.api_questions.utils.tmdb.model.media.MediaType;
import cinequiz.backend.api_questions.utils.tmdb.model.media.cast.CastMember;
import cinequiz.backend.api_questions.utils.tmdb.model.media.cast.CastPage;
import cinequiz.backend.api_questions.utils.tmdb.model.media.list.MovieResultsPage;
import cinequiz.backend.api_questions.utils.tmdb.model.media.list.ResultsPage;
import cinequiz.backend.api_questions.utils.tmdb.model.media.list.TvResultsPage;

public class MediaTmdbFetching extends TmdbFetching {
    public static ArrayList<MediaInfos> getRandomCoherentMedias(String tmdbLanguage, int number,
            MediaTmdbFetchingOptions answerOptions, MediaTmdbFetchingOptions similaryOptions, MediaType mediaType) {
        ArrayList<MediaInfos> mediaList = new ArrayList<MediaInfos>();

        ArrayList<MediaInfos> similarMediaList = null;
        MediaInfos media = null;

        // try to find a media with an similar media list valid in tmdb
        while (similarMediaList == null) {

            media = getOneRandomValidMedia(tmdbLanguage, answerOptions, mediaType);

            try {
                similarMediaList = getSimilarValidMedias(media, number - 1, tmdbLanguage, similaryOptions, mediaType);
            } catch (NotEnoughSimilarShowsInTMDBException e) {
                System.err.println(e.getMessage());
                mediaList.clear();
            }
        }

        // original media at index 0
        mediaList.add(media);
        mediaList.addAll(similarMediaList);

        return mediaList;
    }

    private static ArrayList<MediaInfos> getSimilarValidMedias(MediaInfos media, int number, String tmdbLanguage,
            MediaTmdbFetchingOptions options, MediaType mediaType) throws NotEnoughSimilarShowsInTMDBException {
        ArrayList<MediaInfos> mediaList = new ArrayList<MediaInfos>();

        // it can have several pages for similar movies in tmdb, we start at the first
        // page
        int page_number = 1;
        // we need to fill the list
        while (page_number < 3) {

            // try to get the similar media page
            List<? extends MediaInfos> results = getSimilarMediasPage(media.getId(), tmdbLanguage, page_number,
                    mediaType);
            // we failed to get a similar media page then we throw an exception
            if (results == null)
                throw new NotEnoughSimilarShowsInTMDBException();

            // only keeps the movies of the same language
            List<? extends MediaInfos> filtredResults = (ArrayList<? extends MediaInfos>) results.stream()
                    .filter(m -> m.getOriginalLanguage().equals(media.getOriginalLanguage()))
                    .collect(Collectors.toList());

            // remove unvalid similar movies
            filtredResults = getFiltredMediaInfosListInPage(filtredResults, options);

            Collections.shuffle(filtredResults);

            // browse valid similar movies
            for (MediaInfos result : filtredResults) {
                // if the target value is an duplicata we go next
                List<MediaInfos> dontWantDuplicata = new ArrayList<MediaInfos>(mediaList);
                dontWantDuplicata.add(media);
                if (options.checkDuplicate(result, dontWantDuplicata))
                    continue;

                mediaList.add(result);

                // if we have enough movies we stop browsing the page.
                if (mediaList.size() >= number)
                    break;
            }

            // go to the next similar media page
            page_number++;
        }

        if (mediaList.size() < number)
            throw new NotEnoughSimilarShowsInTMDBException();

        return mediaList;
    }

    private static MediaInfos getOneRandomValidMedia(String tmdbLanguage, MediaTmdbFetchingOptions options,
            MediaType mediaType) {
        MediaInfos media = null;

        // as long as we don't have a valid media, we go through the pages of popular
        // movies randomly
        while (media == null) {
            List<? extends MediaInfos> page = getRandomPopularMediasPage(tmdbLanguage, mediaType);

            // remove unvalid movies of the popular media page
            List<? extends MediaInfos> filtredResults = getFiltredMediaInfosListInPage(page, options);
            Collections.shuffle(filtredResults);

            if (filtredResults.size() > 0)
                media = filtredResults.stream().findAny().get();
        }
        return media;
    }

    private static final int RANDOM_PAGE_MIN = 1;
    private static final int RANDOM_PAGE_MAX = 100; // I want one of the 100 first pages (the 2000 actual most popular
                                                    // // films)

    private static List<? extends MediaInfos> getRandomPopularMediasPage(String tmdbLanguage, MediaType mediaType) {
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

    private static List<? extends MediaInfos> getSimilarMediasPage(int movieId, String tmdbLanguage, int num_page,
            MediaType mediaType) {
        List<? extends MediaInfos> list = null;

        String url = "https://api.themoviedb.org/3/" + mediaType.getTmdbPrefix() + "/" + movieId + "/similar?api_key="
                + TmdbFetching.API_KEY
                + "&language=" + tmdbLanguage + "&page=" + num_page;
        list = getMediaInfosListFromAnResultPage(url, mediaType);

        // null if the target similar media page isn't valid
        return list;
    }

    private static List<? extends MediaInfos> getFiltredMediaInfosListInPage(List<? extends MediaInfos> list,
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

    private static ArrayList<CastMember> getRandomCoherentPeoplesInvolvedInThisMedia(int movieId,
            String tmdbLanguage, int number, PeopleTmdbFetchingOptions options, int tmdbgenre, MediaType mediaType)
            throws CastUnavailableInTMDBException, NotEnoughPeoplesInCast {
        return getRandomCoherentPeoplesInvolvedInThisMedia(movieId, tmdbLanguage, number, options, tmdbgenre, mediaType,
                -1);
    }

    private static ArrayList<CastMember> getRandomCoherentPeoplesInvolvedInThisMedia(int movieId,
            String tmdbLanguage,
            int number, PeopleTmdbFetchingOptions options, int tmdbgenre, MediaType mediaType, int similarMediaId)
            throws CastUnavailableInTMDBException, NotEnoughPeoplesInCast {
        ArrayList<CastMember> peoples = new ArrayList<CastMember>();

        CastPage castPage = getMediaCastPage(movieId, tmdbLanguage, mediaType);
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
            // add cast to the final list if he isn't in the similar media
            if (!PeopleTmdbFetching.isCastIsInThisShow(c.id, similarMediaId, tmdbLanguage, mediaType.getTmdbPrefix())) {
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

    private static CastPage getMediaCastPage(int movieId, String tmdbLanguage, MediaType mediaType) {
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

    public static ArrayList<CastMember> getRandomCoherentPeopleListInTheseMedias(int movieId,
            int numberOfPeoplesInMedia, int similarMediaId, int numberOfPeoplesInSimilarMedia, String tmdbLanguage,
            MediaType mediaType) {
        ArrayList<CastMember> cast = null;
        int randomGender = BackendApplication.random(1, 2);
        try {
            PeopleTmdbFetchingOptions panswerOptions = new PeopleTmdbFetchingOptions(true, true, true);
            ArrayList<CastMember> answer = getRandomCoherentPeoplesInvolvedInThisMedia(movieId,
                    tmdbLanguage, numberOfPeoplesInMedia, panswerOptions, randomGender, mediaType);
            PeopleTmdbFetchingOptions psimilaryOptions = new PeopleTmdbFetchingOptions(true, false, true);

            ArrayList<CastMember> similaryCast = getRandomCoherentPeoplesInvolvedInThisMedia(similarMediaId,
                    tmdbLanguage, numberOfPeoplesInSimilarMedia, psimilaryOptions, randomGender, mediaType,
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
                page = rt.getForObject(url, TvResultsPage.class);
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
