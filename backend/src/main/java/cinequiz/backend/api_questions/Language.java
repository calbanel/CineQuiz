package cinequiz.backend.api_questions;

public enum Language {
    FR("fr-FR"),
    EN("en-US");

    Language(String tmdbLanguage) {
        this.tmdbLanguage = tmdbLanguage;
    }

    public String getTmdbLanguage() {
        return tmdbLanguage;
    }

    private final String tmdbLanguage;

}
