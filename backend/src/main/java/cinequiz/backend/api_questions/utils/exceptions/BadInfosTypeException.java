package cinequiz.backend.api_questions.utils.exceptions;

import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosType;

public class BadInfosTypeException extends Exception {
    public BadInfosTypeException(InfosType type, String methodName) {
        super("Bad type (" + type.getTmdbValue() + ") for method \"" + methodName + "\"!");
    }
}
