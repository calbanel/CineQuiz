package cinequiz.backend.api_questions.utils.exceptions;

public class ImpossibleToFetchTmdbException extends Exception {
    public ImpossibleToFetchTmdbException() {
        super("TMDB is unavailable!");
    }
}
