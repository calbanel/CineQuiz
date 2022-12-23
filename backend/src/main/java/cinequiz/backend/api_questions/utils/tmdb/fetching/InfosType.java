package cinequiz.backend.api_questions.utils.tmdb.fetching;

public enum InfosType {
    MOVIE("movie"),
    TV("tv"),
    PERSON("person");

    InfosType(String tmdbValue) {
        this.tmdbValue = tmdbValue;
    }

    public String getTmdbValue() {
        return tmdbValue;
    }

    private final String tmdbValue;
}
