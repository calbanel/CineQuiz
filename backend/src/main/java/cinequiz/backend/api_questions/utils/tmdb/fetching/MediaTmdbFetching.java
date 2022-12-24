package cinequiz.backend.api_questions.utils.tmdb.fetching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import cinequiz.backend.BackendApplication;
import cinequiz.backend.api_questions.utils.Language;
import cinequiz.backend.api_questions.utils.exceptions.NotEnoughSimilarMediasInTMDBException;
import cinequiz.backend.api_questions.utils.tmdb.fetching.options.MediaTmdbFetchingOptions;
import cinequiz.backend.api_questions.utils.tmdb.model.media.MediaInterface;
import cinequiz.backend.api_questions.utils.tmdb.model.media.list.MovieResultsPage;
import cinequiz.backend.api_questions.utils.tmdb.model.media.list.ResultsPage;
import cinequiz.backend.api_questions.utils.tmdb.model.media.list.TvResultsPage;

public class MediaTmdbFetching extends TmdbFetching {
    public static List<MediaInterface> getRandomCoherentMedias(Language language, int number,
            MediaTmdbFetchingOptions answerOptions, MediaTmdbFetchingOptions similaryOptions, InfosType mediaType) {
        List<MediaInterface> mediaList = new ArrayList<MediaInterface>();

        List<MediaInterface> similarMediaList = null;
        MediaInterface media = null;

        // try to find a media with an similar media list valid in tmdb
        while (similarMediaList == null) {

            media = getOneRandomValidMedia(language, answerOptions, mediaType);

            try {
                similarMediaList = getSimilarValidMedias(media, number - 1, language, similaryOptions,
                        mediaType);
            } catch (NotEnoughSimilarMediasInTMDBException e) {
                System.err.println(e.getMessage());
            }
        }

        // original media at index 0
        mediaList.add(media);
        mediaList.addAll(similarMediaList);

        return mediaList;
    }

    private static MediaInterface getOneRandomValidMedia(Language language, MediaTmdbFetchingOptions options,
            InfosType mediaType) {
        MediaInterface media = null;

        // as long as we don't have a valid media, we go through the pages of popular
        // movies randomly
        while (media == null) {
            List<? extends MediaInterface> page = getRandomPopularMediasList(language, mediaType);

            // remove unvalid movies of the popular media page
            List<? extends MediaInterface> filtredResults = filterMediaInfosList(page, options);
            Collections.shuffle(filtredResults);

            if (filtredResults.size() > 0)
                media = filtredResults.stream().findAny().get();
        }
        return media;
    }

    private static final int RANDOM_PAGE_MIN = 1;
    private static final int RANDOM_PAGE_MAX = 100; // I want one of the 100 first pages (the 2000 actual most popular)

    private static List<? extends MediaInterface> getRandomPopularMediasList(Language language, InfosType mediaType) {
        List<? extends MediaInterface> list = null;

        // as long as we don't have a valid page, we go through the pages of popular
        // randomly
        while (list == null) {
            int randomPage = BackendApplication.random(RANDOM_PAGE_MIN, RANDOM_PAGE_MAX);
            ApiURL url = new ApiURL(mediaType, RessourceType.POPULAR);
            url.addLanguage(language);
            url.addPage(randomPage);

            list = getMediaInfosListFromAnResultPage(url, mediaType);
        }
        return list;
    }

    private static List<? extends MediaInterface> getMediaInfosListFromAnResultPage(ApiURL url, InfosType mediaType) {
        List<? extends MediaInterface> list = null;
        ResultsPage<? extends MediaInterface> page = null;

        if (mediaType == InfosType.MOVIE) {
            page = fetchTmdbApi(url, MovieResultsPage.class);
        } else if (mediaType == InfosType.TV) {
            page = fetchTmdbApi(url, TvResultsPage.class);
        }

        if (page != null)
            list = page.getResults();

        return list;
    }

    private static List<? extends MediaInterface> filterMediaInfosList(List<? extends MediaInterface> list,
            MediaTmdbFetchingOptions options) {
        return (List<? extends MediaInterface>) list
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

    private static List<MediaInterface> getSimilarValidMedias(MediaInterface media, int number,
            Language language,
            MediaTmdbFetchingOptions options, InfosType mediaType) throws NotEnoughSimilarMediasInTMDBException {
        List<MediaInterface> mediaList = new ArrayList<MediaInterface>();

        // it can have several pages for similar movies in tmdb, we start at the first
        // page
        int page_number = 1;
        // we need to fill the list
        while (page_number < 3) {

            // try to get the similar media page
            List<? extends MediaInterface> results = getSimilarMediasList(media.getId(), language,
                    page_number,
                    mediaType);
            // we failed to get a similar media page then we throw an exception
            if (results == null)
                throw new NotEnoughSimilarMediasInTMDBException(media.getId());

            // only keeps the movies of the same language
            List<? extends MediaInterface> filtredResults = (ArrayList<? extends MediaInterface>) results.stream()
                    .filter(m -> m.getOriginalLanguage().equals(media.getOriginalLanguage()))
                    .collect(Collectors.toList());

            // remove unvalid similar movies
            filtredResults = filterMediaInfosList(filtredResults, options);

            Collections.shuffle(filtredResults);

            // browse valid similar movies
            for (MediaInterface result : filtredResults) {
                // if the target value is an duplicata we go next
                List<MediaInterface> dontWantDuplicata = new ArrayList<MediaInterface>(mediaList);
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
            throw new NotEnoughSimilarMediasInTMDBException(media.getId());

        return mediaList;
    }

    private static List<? extends MediaInterface> getSimilarMediasList(int movieId, Language language, int numPage,
            InfosType mediaType) {
        List<? extends MediaInterface> list = null;

        ApiURL url = new ApiURL(mediaType, RessourceType.SIMILAR, movieId);
        url.addLanguage(language);
        url.addPage(numPage);

        list = getMediaInfosListFromAnResultPage(url, mediaType);

        // null if the target similar media page isn't valid
        return list;
    }
}
