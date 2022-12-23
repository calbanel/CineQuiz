package cinequiz.backend.api_questions.utils.tmdb.fetching;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class TmdbFetching {

    protected static final String API_KEY = "c7d238dde9b0efbe8deb61921ee13f06";
    public static final String IMG_URL_BASE = "https://image.tmdb.org/t/p/w500";

    protected <T> T fetchTmdbApi(ApiURL apiUrl, Class<T> pojoClass) {
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

}
