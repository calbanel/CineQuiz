package cinequiz.backend.api_questions.utils.tmdb.fetching;

public enum RessourceType {
    POPULAR("popular"),
    SIMILAR("similar"),
    CREDITS("credits"),
    COMBINED_CREDITS("combined_credits"),
    INFOS("");

    RessourceType(String tmdbValue) {
        this.tmdbValue = tmdbValue;
    }

    public String getTmdbValue() {
        return tmdbValue;
    }

    private final String tmdbValue;
}
