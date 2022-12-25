package cinequiz.backend.api_questions.utils.tmdb.fetching;

import cinequiz.backend.BackendApplication;
import cinequiz.backend.api_questions.utils.exceptions.TypeNotSupportedException;

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

    public static InfosType typeCheck(String value) throws TypeNotSupportedException {
        for (InfosType type : InfosType.values()) {
            if (value.equals(type.getTmdbValue()))
                return type;
        }

        throw new TypeNotSupportedException(value);
    }

    public static InfosType getRandomType() {
        int randomType = BackendApplication.random(0, InfosType.values().length - 1);
        return InfosType.values()[randomType];
    }
}
