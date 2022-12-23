package cinequiz.backend.api_questions.utils.tmdb.fetching;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class TmdbFetching {

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

}
