package cinequiz.backend.api_questions.utils.tmdb.fetching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import cinequiz.backend.BackendApplication;
import cinequiz.backend.api_questions.utils.Language;
import cinequiz.backend.api_questions.utils.exceptions.NotEnoughSimilarMediasInTMDBException;
import cinequiz.backend.api_questions.utils.tmdb.fetching.options.InfosTmdbFetchingOptions;
import cinequiz.backend.api_questions.utils.tmdb.model.InfosInterface;
import cinequiz.backend.api_questions.utils.tmdb.model.media.list.MovieResultsPage;
import cinequiz.backend.api_questions.utils.tmdb.model.media.list.ResultsPage;
import cinequiz.backend.api_questions.utils.tmdb.model.media.list.TvResultsPage;

public class MediaTmdbFetching extends TmdbFetching {
    public static List<InfosInterface> getRandomCoherentMedias(Language language, int number,
            InfosTmdbFetchingOptions answerOptions, InfosTmdbFetchingOptions similaryOptions, InfosType mediaType) {
        List<InfosInterface> mediaList = new ArrayList<InfosInterface>();

        List<InfosInterface> similarMediaList = null;
        InfosInterface media = null;

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

    private static InfosInterface getOneRandomValidMedia(Language language, InfosTmdbFetchingOptions options,
            InfosType mediaType) {
        InfosInterface media = null;

        // as long as we don't have a valid media, we go through the pages of popular
        // movies randomly
        while (media == null) {
            List<? extends InfosInterface> page = getRandomPopularMediasList(language, mediaType);

            // remove unvalid movies of the popular media page
            List<? extends InfosInterface> filtredResults = filterMediaInfosList(page, options);
            Collections.shuffle(filtredResults);

            if (filtredResults.size() > 0)
                media = filtredResults.stream().findAny().get();
        }
        return media;
    }

    private static final int RANDOM_PAGE_MIN = 1;
    private static final int RANDOM_PAGE_MAX = 100; // I want one of the 100 first pages (the 2000 actual most popular)

    private static List<? extends InfosInterface> getRandomPopularMediasList(Language language, InfosType mediaType) {
        List<? extends InfosInterface> list = null;

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

    private static List<? extends InfosInterface> getMediaInfosListFromAnResultPage(ApiURL url, InfosType mediaType) {
        List<? extends InfosInterface> list = null;
        ResultsPage<? extends InfosInterface> page = null;

        if (mediaType == InfosType.MOVIE) {
            page = fetchTmdbApi(url, MovieResultsPage.class);
        } else if (mediaType == InfosType.TV) {
            page = fetchTmdbApi(url, TvResultsPage.class);
        }

        if (page != null)
            list = page.getResults();

        return list;
    }

    private static List<? extends InfosInterface> filterMediaInfosList(List<? extends InfosInterface> list,
            InfosTmdbFetchingOptions options) {
        return (List<? extends InfosInterface>) list
                .stream()
                .filter(
                        (m) -> (!options.isName() || (m.getName() != null && !m.getName().equals("")))
                                && (!options.isImage()
                                        || (m.getImage() != null && !m.getImage().equals("")))
                                && (!options.isDescription()
                                        || (m.getDescription() != null && !m.getDescription().equals("")))
                                && (!options.isDate()
                                        || (m.getDate() != null && !m.getDate().equals(""))))
                .collect(Collectors.toList());
    }

    private static final int SIMILAR_START_PAGE = 1;
    private static final int NB_MAX_BROWSING_SIMILAR_PAGE = 3;

    private static List<InfosInterface> getSimilarValidMedias(InfosInterface media, int number,
            Language language,
            InfosTmdbFetchingOptions options, InfosType mediaType) throws NotEnoughSimilarMediasInTMDBException {
        List<InfosInterface> mediaList = new ArrayList<InfosInterface>();

        // it can have several pages for similar movies in tmdb, we start at the first
        // page
        int page_number = SIMILAR_START_PAGE;
        // we need to fill the list
        while (page_number < NB_MAX_BROWSING_SIMILAR_PAGE) {

            // try to get the similar media page
            List<? extends InfosInterface> results = getSimilarMediasList(media.getId(), language,
                    page_number,
                    mediaType);
            // we failed to get a similar media page then we throw an exception
            if (results == null)
                throw new NotEnoughSimilarMediasInTMDBException(media.getId());

            // only keeps the movies of the same language and different of the initial movie
            List<? extends InfosInterface> filtredResults = (ArrayList<? extends InfosInterface>) results.stream()
                    .filter(m -> m.getOriginalLanguage().equals(media.getOriginalLanguage())
                            && m.getId() != media.getId())
                    .collect(Collectors.toList());

            // remove unvalid similar movies
            filtredResults = filterMediaInfosList(filtredResults, options);

            Collections.shuffle(filtredResults);

            // browse valid similar movies
            for (InfosInterface result : filtredResults) {
                // if the target value is an duplicata we go next
                List<InfosInterface> dontWantDuplicata = new ArrayList<InfosInterface>(mediaList);
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

    private static List<? extends InfosInterface> getSimilarMediasList(int movieId, Language language, int numPage,
            InfosType mediaType) {
        List<? extends InfosInterface> list = null;

        ApiURL url = new ApiURL(mediaType, RessourceType.SIMILAR, movieId);
        url.addLanguage(language);
        url.addPage(numPage);

        list = getMediaInfosListFromAnResultPage(url, mediaType);

        // null if the target similar media page isn't valid
        return list;
    }
}
