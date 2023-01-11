package cinequiz.backend.api_questions.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import cinequiz.backend.api_questions.utils.exceptions.LanguageNotSupportedException;

public enum Language {
    FR("fr", DateTimeFormatter.ofPattern("dd/MM/yyyy")),
    EN("en", DateTimeFormatter.ofPattern("MM/dd/yyyy"));

    Language(String tmdbLanguage, DateTimeFormatter dateFormat) {
        this.tmdbLanguage = tmdbLanguage;
        this.dateFormat = dateFormat;
    }

    public String getTmdbLanguage() {
        return tmdbLanguage;
    }

    private final String tmdbLanguage;
    private final DateTimeFormatter dateFormat;

    public static Language languageCheck(String value) throws LanguageNotSupportedException {

        for (Language lan : Language.values()) {
            if (value.equals(lan.getTmdbLanguage()))
                return lan;
        }

        throw new LanguageNotSupportedException(value);

    }

    public String getFormattedDate(String nonFormated) {
        String formated = nonFormated;

        LocalDate date = LocalDate.parse(formated, DateTimeFormatter.ISO_DATE);
        formated = date.format(this.dateFormat);

        return formated;
    }
}
