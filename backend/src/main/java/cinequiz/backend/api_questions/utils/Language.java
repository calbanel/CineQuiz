package cinequiz.backend.api_questions.utils;

import cinequiz.backend.api_questions.exceptions.LanguageNotSupportedException;

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

    public static Language languageCheck(String language) throws LanguageNotSupportedException {
        Language lang;
        if (language.equals("fr"))
            lang = Language.FR;
        else if (language.equals("en"))
            lang = Language.EN;
        else
            throw new LanguageNotSupportedException();

        return lang;
    }
}
