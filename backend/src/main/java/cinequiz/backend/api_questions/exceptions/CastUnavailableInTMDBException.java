package cinequiz.backend.api_questions.exceptions;

public class CastUnavailableInTMDBException extends Exception {
    public CastUnavailableInTMDBException() {
        super("Cast isn't available for this show!");
    }
}
