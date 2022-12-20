package cinequiz.backend.api_questions.utils.tmdb.fetching;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import cinequiz.backend.api_questions.utils.tmdb.fetching.options.PeopleTmdbFetchingOptions;
import cinequiz.backend.api_questions.utils.tmdb.model.media.cast.Cast;
import cinequiz.backend.api_questions.utils.tmdb.model.media.cast.CastMember;
import cinequiz.backend.api_questions.utils.tmdb.model.media.cast.CastPage;
import cinequiz.backend.api_questions.utils.tmdb.model.media.cast.Crew;
import cinequiz.backend.api_questions.utils.tmdb.model.people.credit.ShowCredit;
import cinequiz.backend.api_questions.utils.tmdb.model.people.credit.ShowCreditPage;

public class PeopleTmdbFetching extends TmdbFetching {

    public static boolean isCastIsInThisShow(int personId, int movieId, String tmdbLanguage, String mediaType) {
        boolean isIn = false;

        // get all the movies have participated the person
        ShowCreditPage creditPage = PeopleTmdbFetching.getPeopleShowCreditPage(personId, tmdbLanguage);
        if (creditPage != null) {
            ArrayList<ShowCredit> list = new ArrayList<ShowCredit>();
            list.addAll(creditPage.cast);
            list.addAll(creditPage.crew);
            isIn = list.stream().filter(m -> m.id == movieId && m.media_type.equals(mediaType))
                    .findFirst().isPresent();
        }

        return isIn;
    }

    private static ShowCreditPage getPeopleShowCreditPage(int personId, String tmdbLanguage) {
        ShowCreditPage page = null;
        RestTemplate rt = new RestTemplate();
        String url = "https://api.themoviedb.org/3/person/" + personId + "/combined_credits?api_key="
                + TmdbFetching.API_KEY
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

    public static ArrayList<CastMember> getFiltredCastListInPage(CastPage page,
            PeopleTmdbFetchingOptions options, int tmdbgenre) {
        ArrayList<Cast> cast = (ArrayList<Cast>) page.cast.stream()
                .filter((c) -> (!options.isProfile_path() || (c.profile_path != null && !c.profile_path.equals("")))
                        && (!options.isName() || (c.name != null && !c.name.equals("")))
                        && (!options.isGender() || c.gender == tmdbgenre))
                .collect(Collectors.toList());
        ArrayList<Crew> crew = (ArrayList<Crew>) page.crew.stream()
                .filter((c) -> (!options.isProfile_path() || (c.profile_path != null && !c.profile_path.equals("")))
                        && (!options.isName() || (c.name != null && !c.name.equals("")))
                        && (!options.isGender() || c.gender == tmdbgenre))
                .collect(Collectors.toList());
        ArrayList<CastMember> members = new ArrayList<CastMember>();
        members.addAll(cast);
        members.addAll(crew);
        return members;
    }

}
