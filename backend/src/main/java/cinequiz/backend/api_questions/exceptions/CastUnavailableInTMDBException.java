package cinequiz.backend.api_questions.exceptions;

public class CastUnavailableInTMDBException extends Exception {
    public CastUnavailableInTMDBException(int id) {
        super("Cast isn't available for " + id + "!");
    }
}
