package cinequiz.backend.api_questions.utils.tmdb.fetching;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import cinequiz.backend.BackendApplication;
import cinequiz.backend.api_questions.utils.Language;
import cinequiz.backend.api_questions.utils.exceptions.CastUnavailableInTMDBException;
import cinequiz.backend.api_questions.utils.exceptions.NotEnoughPeoplesInCastException;
import cinequiz.backend.api_questions.utils.tmdb.fetching.options.PeopleTmdbFetchingOptions;
import cinequiz.backend.api_questions.utils.tmdb.model.media.MediaCredits;
import cinequiz.backend.api_questions.utils.tmdb.model.media.MediaPersonCredits;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonCast;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonCredits;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonCrew;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonMediaCredits;

public class PeopleTmdbFetching extends TmdbFetching {

        private static final int MIN_GENDER_TMDB_CODE = 1;
        private static final int MAX_GENDER_TMDB_CODE = 2;

        public static List<PersonMediaCredits> getRandomCoherentPeopleListInTheseMedias(int movieId,
                        int numberOfPeoplesInMedia, int similarMediaId, int numberOfPeoplesInSimilarMedia,
                        Language language,
                        InfosType mediaType) {
                List<PersonMediaCredits> cast = null;
                int randomGender = BackendApplication.random(MIN_GENDER_TMDB_CODE, MAX_GENDER_TMDB_CODE);
                try {
                        PeopleTmdbFetchingOptions panswerOptions = new PeopleTmdbFetchingOptions(true, true, true);
                        List<PersonMediaCredits> answer = getRandomCoherentPeoplesInvolvedInThisMedia(movieId,
                                        language, numberOfPeoplesInMedia, panswerOptions, randomGender, mediaType);
                        PeopleTmdbFetchingOptions psimilaryOptions = new PeopleTmdbFetchingOptions(true, false, true);

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

        private static List<PersonMediaCredits> getRandomCoherentPeoplesInvolvedInThisMedia(int movieId,
                        Language language, int number, PeopleTmdbFetchingOptions options, int tmdbgenre,
                        InfosType mediaType)
                        throws CastUnavailableInTMDBException, NotEnoughPeoplesInCastException {
                return getRandomCoherentPeoplesInvolvedInThisMedia(movieId, language, number, options,
                                tmdbgenre, mediaType,
                                NO_MOVIE_ID);
        }

        private static List<PersonMediaCredits> getRandomCoherentPeoplesInvolvedInThisMedia(int movieId,
                        Language language,
                        int number, PeopleTmdbFetchingOptions options, int tmdbgenre, InfosType mediaType,
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

        private static MediaCredits getMediaCastPage(int movieId, Language language, InfosType mediaType) {
                MediaCredits page = null;

                ApiURL url = new ApiURL(mediaType, RessourceType.CREDITS, movieId);
                url.addLanguage(language);

                page = fetchTmdbApi(url, MediaCredits.class);

                // null if the target cast page isn't valid
                return page;
        }

        public static List<PersonMediaCredits> getFiltredPersonMediaCredits(MediaCredits page,
                        PeopleTmdbFetchingOptions options, int tmdbgenre) {
                List<PersonCast> cast = (ArrayList<PersonCast>) page.getCast().stream()
                                .filter((c) -> (!options.isProfile_path()
                                                || (c.getProfilePath() != null && !c.getProfilePath().equals("")))
                                                && (!options.isName()
                                                                || (c.getName() != null && !c.getName().equals("")))
                                                && (!options.isGender() || c.getGender() == tmdbgenre))
                                .collect(Collectors.toList());
                List<PersonCrew> crew = (ArrayList<PersonCrew>) page.getCrew().stream()
                                .filter((c) -> (!options.isProfile_path()
                                                || (c.getProfilePath() != null && !c.getProfilePath().equals("")))
                                                && (!options.isName()
                                                                || (c.getName() != null && !c.getName().equals("")))
                                                && (!options.isGender() || c.getGender() == tmdbgenre))
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

        private static PersonCredits getPeopleCredits(int personId, Language language) {
                PersonCredits page = null;

                ApiURL url = new ApiURL(InfosType.PERSON, RessourceType.COMBINED_CREDITS, personId);
                url.addLanguage(language);

                page = fetchTmdbApi(url, PersonCredits.class);

                // null if the target cast page isn't valid
                return page;
        }

}
