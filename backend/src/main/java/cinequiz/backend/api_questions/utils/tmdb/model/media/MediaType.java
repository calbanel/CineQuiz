package cinequiz.backend.api_questions.utils.tmdb.model.media;

public enum MediaType {
    MOVIE("movie"),
    TV("tv");

    MediaType(String tmdbPrefix) {
        this.tmdbPrefix = tmdbPrefix;
    }

    public String getTmdbPrefix() {
        return tmdbPrefix;
    }

    private final String tmdbPrefix;
}
