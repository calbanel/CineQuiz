package cinequiz.backend.api_questions.utils.tmdb.fetching;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cinequiz.backend.api_questions.utils.Language;
import cinequiz.backend.api_questions.utils.tmdb.fetching.options.PeopleTmdbFetchingOptions;
import cinequiz.backend.api_questions.utils.tmdb.model.media.MediaCredits;
import cinequiz.backend.api_questions.utils.tmdb.model.media.MediaPersonCredits;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonCast;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonCredits;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonCrew;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonMediaCredits;

public class PeopleTmdbFetching extends TmdbFetching {

    public static boolean isCastIsInThisShow(int personId, int movieId, Language language, String mediaType) {
        boolean isIn = false;

        // get all the movies have participated the person
        PersonCredits creditPage = PeopleTmdbFetching.getPeopleCredits(personId, language);
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

    public static List<PersonMediaCredits> getFiltredPersonMediaCredits(MediaCredits page,
            PeopleTmdbFetchingOptions options, int tmdbgenre) {
        List<PersonCast> cast = (ArrayList<PersonCast>) page.getCast().stream()
                .filter((c) -> (!options.isProfile_path()
                        || (c.getProfilePath() != null && !c.getProfilePath().equals("")))
                        && (!options.isName() || (c.getName() != null && !c.getName().equals("")))
                        && (!options.isGender() || c.getGender() == tmdbgenre))
                .collect(Collectors.toList());
        List<PersonCrew> crew = (ArrayList<PersonCrew>) page.getCrew().stream()
                .filter((c) -> (!options.isProfile_path()
                        || (c.getProfilePath() != null && !c.getProfilePath().equals("")))
                        && (!options.isName() || (c.getName() != null && !c.getName().equals("")))
                        && (!options.isGender() || c.getGender() == tmdbgenre))
                .collect(Collectors.toList());
        List<PersonMediaCredits> members = new ArrayList<PersonMediaCredits>();
        members.addAll(cast);
        members.addAll(crew);
        return members;
    }

}
