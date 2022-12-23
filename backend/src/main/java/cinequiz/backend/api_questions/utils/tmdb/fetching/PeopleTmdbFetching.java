package cinequiz.backend.api_questions.utils.tmdb.fetching;

import java.util.ArrayList;
import java.util.stream.Collectors;

import cinequiz.backend.api_questions.utils.Language;
import cinequiz.backend.api_questions.utils.tmdb.fetching.options.PeopleTmdbFetchingOptions;
import cinequiz.backend.api_questions.utils.tmdb.model.media.MediaCredits;
import cinequiz.backend.api_questions.utils.tmdb.model.media.MediaPersonCredits;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonCast;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonCredits;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonCrew;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonMovieCredits;

public class PeopleTmdbFetching extends TmdbFetching {

    public static boolean isCastIsInThisShow(int personId, int movieId, Language language, String mediaType) {
        boolean isIn = false;

        // get all the movies have participated the person
        PersonCredits creditPage = PeopleTmdbFetching.getPeopleShowCreditPage(personId, language);
        if (creditPage != null) {
            ArrayList<MediaPersonCredits> list = new ArrayList<MediaPersonCredits>();
            list.addAll(creditPage.getCast());
            list.addAll(creditPage.getCrew());
            isIn = list.stream().filter(m -> m.getId() == movieId && m.getMediaType().equals(mediaType))
                    .findFirst().isPresent();
        }

        return isIn;
    }

    private static PersonCredits getPeopleShowCreditPage(int personId, Language language) {
        PersonCredits page = null;

        ApiURL url = new ApiURL(MediaType.PERSON, RessourceType.COMBINED_CREDITS, personId);
        url.addLanguage(language);

        page = fetchTmdbApi(url, PersonCredits.class);

        // null if the target cast page isn't valid
        return page;
    }

    public static ArrayList<PersonMovieCredits> getFiltredCastListInPage(MediaCredits page,
            PeopleTmdbFetchingOptions options, int tmdbgenre) {
        ArrayList<PersonCast> cast = (ArrayList<PersonCast>) page.getCast().stream()
                .filter((c) -> (!options.isProfile_path()
                        || (c.getProfilePath() != null && !c.getProfilePath().equals("")))
                        && (!options.isName() || (c.getName() != null && !c.getName().equals("")))
                        && (!options.isGender() || c.getGender() == tmdbgenre))
                .collect(Collectors.toList());
        ArrayList<PersonCrew> crew = (ArrayList<PersonCrew>) page.getCrew().stream()
                .filter((c) -> (!options.isProfile_path()
                        || (c.getProfilePath() != null && !c.getProfilePath().equals("")))
                        && (!options.isName() || (c.getName() != null && !c.getName().equals("")))
                        && (!options.isGender() || c.getGender() == tmdbgenre))
                .collect(Collectors.toList());
        ArrayList<PersonMovieCredits> members = new ArrayList<PersonMovieCredits>();
        members.addAll(cast);
        members.addAll(crew);
        return members;
    }

}
