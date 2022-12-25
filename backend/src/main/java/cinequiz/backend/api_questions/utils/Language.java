package cinequiz.backend.api_questions.utils;

import cinequiz.backend.api_questions.utils.exceptions.LanguageNotSupportedException;

public enum Language {
    FR("fr"),
    EN("en");

    Language(String tmdbLanguage) {
        this.tmdbLanguage = tmdbLanguage;
    }

    public String getTmdbLanguage() {
        return tmdbLanguage;
    }

    private final String tmdbLanguage;

    public static Language languageCheck(String value) throws LanguageNotSupportedException {

        for (Language lan : Language.values()) {
            if (value.equals(lan.getTmdbLanguage()))
                return lan;
        }

        throw new LanguageNotSupportedException(value);

    }
}
