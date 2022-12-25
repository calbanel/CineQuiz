package cinequiz.backend.api_questions.utils.tmdb.fetching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import cinequiz.backend.BackendApplication;
import cinequiz.backend.api_questions.utils.Language;
import cinequiz.backend.api_questions.utils.tmdb.model.InfosInterface;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonInfos;
import cinequiz.backend.api_questions.utils.tmdb.model.results_pages.MovieResultsPage;
import cinequiz.backend.api_questions.utils.tmdb.model.results_pages.PersonResultsPage;
import cinequiz.backend.api_questions.utils.tmdb.model.results_pages.ResultsPage;
import cinequiz.backend.api_questions.utils.tmdb.model.results_pages.TvResultsPage;

public class TmdbFetching {

    static final int MIN_GENDER_TMDB_CODE = 1;
    static final int MAX_GENDER_TMDB_CODE = 2;
    public static final String IMG_URL_BASE = "https://image.tmdb.org/t/p/w500";

    public static <T> T fetchTmdbApi(ApiURL apiUrl, Class<T> pojoClass) {
        RestTemplate rt = new RestTemplate();

        T object = null;

        try {
            object = rt.getForObject(apiUrl.getUrl(), pojoClass);
        } catch (final HttpClientErrorException e) {
            System.err.println(e.getStatusCode());
            System.err.println(e.getResponseBodyAsString());
        }

        return object;
    }

    public static List<InfosInterface> getRandomValidInfos(Language language, InfosTmdbFetchingOptions options,
            InfosType type, int number) {
        List<InfosInterface> infosList = new ArrayList<InfosInterface>();

        // as long as we don't have valid infos, we go through the pages of popular
        // randomly
        while (infosList.size() < number) {
            List<? extends InfosInterface> page = getRandomPopularInfosList(language, type);

            // remove unvalid infos of the popular pages
            List<? extends InfosInterface> filtredResults;

            if (type.equals(InfosType.PERSON)) {
                InfosTmdbFetchingOptions adaptedOptions = new InfosTmdbFetchingOptions(options.isName(),
                        options.isImage(), false, false, options.isGenre());
                filtredResults = filterInfosList(page, adaptedOptions);

                int randomGenre = BackendApplication.random(MIN_GENDER_TMDB_CODE, MAX_GENDER_TMDB_CODE);
                filtredResults = (List<? extends InfosInterface>) page.stream()
                        .filter(p -> p.getGenre() == randomGenre).collect(Collectors.toList());
            } else {
                filtredResults = filterInfosList(page, options);
            }

            Collections.shuffle(filtredResults);

            for (InfosInterface info : filtredResults) {
                if (type.equals(InfosType.PERSON) && (options.isDescription() || options.isDate())) {
                    ApiURL url = new ApiURL(type, RessourceType.INFOS, info.getId());
                    url.addLanguage(language);
                    PersonInfos moreInfos = fetchTmdbApi(url, PersonInfos.class);
                    if ((!options.isDescription()
                            || (moreInfos.getDescription() != null && !moreInfos.getDescription().equals("")))
                            && (!options.isDate() || (moreInfos.getDate() != null && !moreInfos.getDate().equals(""))))
                        if (!infosList.contains(moreInfos))
                            infosList.add(moreInfos);
                } else {
                    if (!infosList.contains(info))
                        infosList.add(info);
                }

                if (infosList.size() >= number)
                    break;
            }
        }

        return infosList;
    }

    private static final int RANDOM_PAGE_MIN = 1;
    private static final int RANDOM_PAGE_MAX = 50; // I want one of the 100 first pages (the 1000 actual most popular
                                                   // items)

    public static List<? extends InfosInterface> getRandomPopularInfosList(Language language, InfosType type) {
        List<? extends InfosInterface> list = null;

        // as long as we don't have a valid page, we go through the pages of popular
        // randomly
        while (list == null) {
            int randomPage = BackendApplication.random(RANDOM_PAGE_MIN, RANDOM_PAGE_MAX);
            ApiURL url = new ApiURL(type, RessourceType.POPULAR);
            url.addLanguage(language);
            url.addPage(randomPage);

            list = getInfosListFromAnResultPage(url, type);
        }
        return list;
    }

    public static List<? extends InfosInterface> getInfosListFromAnResultPage(ApiURL url, InfosType type) {
        List<? extends InfosInterface> list = null;
        ResultsPage<? extends InfosInterface> page = null;

        if (type == InfosType.MOVIE) {
            page = fetchTmdbApi(url, MovieResultsPage.class);
        } else if (type == InfosType.TV) {
            page = fetchTmdbApi(url, TvResultsPage.class);
        } else if (type == InfosType.PERSON) {
            page = fetchTmdbApi(url, PersonResultsPage.class);
        }

        if (page != null)
            list = page.getResults();

        return list;
    }

    public static List<? extends InfosInterface> filterInfosList(List<? extends InfosInterface> list,
            InfosTmdbFetchingOptions options) {
        return (List<? extends InfosInterface>) list
                .stream()
                .filter(
                        (m) -> (!options.isName() || (m.getName() != null && !m.getName().equals("")))
                                && (!options.isImage()
                                        || (m.getImage() != null && !m.getImage().equals("")))
                                && (!options.isDescription()
                                        || (m.getDescription() != null && !m.getDescription().equals("")))
                                && (!options.isGenre()
                                        || (m.getGenre() > 0))
                                && (!options.isDate()
                                        || (m.getDate() != null && !m.getDate().equals(""))))
                .collect(Collectors.toList());
    }

}
