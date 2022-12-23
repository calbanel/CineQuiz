package cinequiz.backend.api_questions.utils.tmdb.fetching;

public enum MediaType {
    MOVIE("movie"),
    TV("tv"),
    PERSON("person");

    MediaType(String tmdbValue) {
        this.tmdbValue = tmdbValue;
    }

    public String getTmdbValue() {
        return tmdbValue;
    }

    private final String tmdbValue;
}