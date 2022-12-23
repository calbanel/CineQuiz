package cinequiz.backend.api_questions.utils.tmdb.fetching;

import cinequiz.backend.api_questions.utils.Language;

public class ApiURL {

    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "c7d238dde9b0efbe8deb61921ee13f06";

    private StringBuilder urlBuilder;

    public ApiURL(MediaType media, RessourceType ressource, int id) {
        urlBuilder = new StringBuilder(TMDB_BASE_URL);
        urlBuilder.append(media.getTmdbValue());
        urlBuilder.append("/");
        urlBuilder.append(Integer.toString(id));
        urlBuilder.append("/");
        urlBuilder.append(ressource.getTmdbValue());
        urlBuilder.append("?api_key=");
        urlBuilder.append(API_KEY);
    }

    public ApiURL(MediaType media, RessourceType ressource) {
        urlBuilder = new StringBuilder(TMDB_BASE_URL);
        urlBuilder.append(media.getTmdbValue());
        urlBuilder.append("/");
        urlBuilder.append(ressource.getTmdbValue());
        urlBuilder.append("?api_key=");
        urlBuilder.append(API_KEY);
    }

    public void addLanguage(Language language) {
        urlBuilder.append("&language=");
        urlBuilder.append(language.getTmdbLanguage());
    }

    public void addPage(int pageNumber) {
        urlBuilder.append("&page=");
        urlBuilder.append(Integer.toString(pageNumber));
    }

    public String getUrl() {
        return urlBuilder.toString();
    }
}
