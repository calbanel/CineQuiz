package cinequiz.backend.api_questions.exceptions;

public class MovieCastUnavailableInTMDBException extends Exception {
    public MovieCastUnavailableInTMDBException() {
        super("Cast isn't available for this movie!");
    }
}