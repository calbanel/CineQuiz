package cinequiz.backend.api_questions.utils.tmdb.fetching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import cinequiz.backend.BackendApplication;
import cinequiz.backend.api_questions.utils.Language;
import cinequiz.backend.api_questions.utils.exceptions.CastUnavailableInTMDBException;
import cinequiz.backend.api_questions.utils.exceptions.NotEnoughPeoplesInCastException;
import cinequiz.backend.api_questions.utils.exceptions.NotEnoughSimilarMediasInTMDBException;
import cinequiz.backend.api_questions.utils.tmdb.model.InfosInterface;
import cinequiz.backend.api_questions.utils.tmdb.model.media.MediaCredits;
import cinequiz.backend.api_questions.utils.tmdb.model.media.MediaPersonCredits;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonCast;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonCredits;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonCrew;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonMediaCredits;

public class MediaTmdbFetching extends TmdbFetching {
    public static List<InfosInterface> getRandomCoherentMedias(Language language, int number,
            InfosTmdbFetchingOptions answerOptions, InfosTmdbFetchingOptions similaryOptions, InfosType mediaType) {
        List<InfosInterface> mediaList = new ArrayList<InfosInterface>();

        List<InfosInterface> similarMediaList = null;
        InfosInterface media = null;

        // try to find a media with an similar media list valid in tmdb
        while (similarMediaList == null) {

            media = getRandomValidInfos(language, answerOptions, mediaType, 1).get(0);

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

    private static final int SIMILAR_START_PAGE = 1;
    private static final int NB_MAX_BROWSING_SIMILAR_PAGE = 3;

    public static List<InfosInterface> getSimilarValidMedias(InfosInterface media, int number,
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
            filtredResults = filterInfosList(filtredResults, options);

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

    public static List<? extends InfosInterface> getSimilarMediasList(int movieId, Language language, int numPage,
            InfosType mediaType) {
        List<? extends InfosInterface> list = null;

        ApiURL url = new ApiURL(mediaType, RessourceType.SIMILAR, movieId);
        url.addLanguage(language);
        url.addPage(numPage);

        list = getInfosListFromAnResultPage(url, mediaType);

        // null if the target similar media page isn't valid
        return list;
    }

    public static List<PersonMediaCredits> getRandomCoherentPeopleListInTheseMedias(int movieId,
            int numberOfPeoplesInMedia, int similarMediaId, int numberOfPeoplesInSimilarMedia,
            Language language,
            InfosType mediaType) {
        List<PersonMediaCredits> cast = null;
        int randomGender = BackendApplication.random(TmdbFetching.MIN_GENDER_TMDB_CODE,
                TmdbFetching.MAX_GENDER_TMDB_CODE);
        try {
            InfosTmdbFetchingOptions panswerOptions = new InfosTmdbFetchingOptions(true, true, false,
                    false, true);
            List<PersonMediaCredits> answer = getRandomCoherentPeoplesInvolvedInThisMedia(movieId,
                    language, numberOfPeoplesInMedia, panswerOptions, randomGender, mediaType);
            InfosTmdbFetchingOptions psimilaryOptions = new InfosTmdbFetchingOptions(true, false, false,
                    false, true);

            List<PersonMediaCredits> similaryCast = getRandomCoherentPeoplesInvolvedInThisMedia(
                    similarMediaId,
                    language, numberOfPeoplesInSimilarMedia, psimilaryOptions, randomGender,
                    mediaType,
                    movieId);
            cast = new ArrayList<PersonMediaCredits>();
            cast.addAll(answer);
            cast.addAll(similaryCast);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // null if we failed to get a valid cast for these movies
        return cast;
    }

    private static final int NO_MOVIE_ID = -1;

    public static List<PersonMediaCredits> getRandomCoherentPeoplesInvolvedInThisMedia(int movieId,
            Language language, int number, InfosTmdbFetchingOptions options, int tmdbgenre,
            InfosType mediaType)
            throws CastUnavailableInTMDBException, NotEnoughPeoplesInCastException {
        return getRandomCoherentPeoplesInvolvedInThisMedia(movieId, language, number, options,
                tmdbgenre, mediaType,
                NO_MOVIE_ID);
    }

    public static List<PersonMediaCredits> getRandomCoherentPeoplesInvolvedInThisMedia(int movieId,
            Language language,
            int number, InfosTmdbFetchingOptions options, int tmdbgenre, InfosType mediaType,
            int similarMediaId)
            throws CastUnavailableInTMDBException, NotEnoughPeoplesInCastException {
        List<PersonMediaCredits> peoples = new ArrayList<PersonMediaCredits>();

        MediaCredits castPage = getMediaCastPage(movieId, language, mediaType);
        // if target cast page isn't valid, throw exception
        if (castPage == null)
            throw new CastUnavailableInTMDBException(movieId);

        // only keeps peoples where we have the target values
        List<PersonMediaCredits> castFiltered = getFiltredPersonMediaCredits(castPage,
                options,
                tmdbgenre);

        // we firt want the most popular casts, we want known names
        castFiltered.sort((a, b) -> new Comparator<PersonMediaCredits>() {
            @Override
            public int compare(PersonMediaCredits o1, PersonMediaCredits o2) {
                if (o1.getPopularity() == o2.getPopularity())
                    return 0;

                return o1.getPopularity() < o2.getPopularity() ? 1 : -1;
            }

        }.compare(a, b));

        // browse the clean cast list
        for (PersonMediaCredits c : castFiltered) {
            // add cast to the final list if he isn't in the similar media
            if (!isCastIsInThisMedia(c.getId(), similarMediaId, language,
                    mediaType.getTmdbValue())) {
                if (peoples.stream().filter(p -> p.getName().equals(c.getName())).findFirst().isEmpty())
                    peoples.add(c);
            }

            // stop the research when we have the number of peoples we want
            if (peoples.size() >= number)
                break;
        }

        // if there not enough peoples in the final list, throw exception
        if (peoples.size() < number)
            throw new NotEnoughPeoplesInCastException(movieId);

        return peoples;
    }

    public static MediaCredits getMediaCastPage(int movieId, Language language, InfosType mediaType) {
        MediaCredits page = null;

        ApiURL url = new ApiURL(mediaType, RessourceType.CREDITS, movieId);
        url.addLanguage(language);

        page = fetchTmdbApi(url, MediaCredits.class);

        // null if the target cast page isn't valid
        return page;
    }

    public static List<PersonMediaCredits> getFiltredPersonMediaCredits(MediaCredits page,
            InfosTmdbFetchingOptions options, int tmdbgenre) {
        List<PersonCast> cast = (ArrayList<PersonCast>) page.getCast().stream()
                .filter((c) -> (!options.isImage()
                        || (c.getProfilePath() != null && !c.getProfilePath().equals("")))
                        && (!options.isName()
                                || (c.getName() != null && !c.getName().equals("")))
                        && (!options.isGenre() || c.getGender() == tmdbgenre))
                .collect(Collectors.toList());
        List<PersonCrew> crew = (ArrayList<PersonCrew>) page.getCrew().stream()
                .filter((c) -> (!options.isImage()
                        || (c.getProfilePath() != null && !c.getProfilePath().equals("")))
                        && (!options.isName()
                                || (c.getName() != null && !c.getName().equals("")))
                        && (!options.isGenre() || c.getGender() == tmdbgenre))
                .collect(Collectors.toList());
        List<PersonMediaCredits> members = new ArrayList<PersonMediaCredits>();
        members.addAll(cast);
        members.addAll(crew);
        return members;
    }

    public static boolean isCastIsInThisMedia(int personId, int movieId, Language language, String mediaType) {
        boolean isIn = false;

        // get all the movies have participated the person
        PersonCredits creditPage = getPeopleCredits(personId, language);
        if (creditPage != null) {
            List<MediaPersonCredits> list = new ArrayList<MediaPersonCredits>();
            list.addAll(creditPage.getCast());
            list.addAll(creditPage.getCrew());
            isIn = list.stream().filter(m -> m.getId() == movieId && m.getMediaType().equals(mediaType))
                    .findFirst().isPresent();
        }

        return isIn;
    }

    public static PersonCredits getPeopleCredits(int personId, Language language) {
        PersonCredits page = null;

        ApiURL url = new ApiURL(InfosType.PERSON, RessourceType.COMBINED_CREDITS, personId);
        url.addLanguage(language);

        page = fetchTmdbApi(url, PersonCredits.class);

        // null if the target cast page isn't valid
        return page;
    }
}
