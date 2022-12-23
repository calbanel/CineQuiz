package cinequiz.backend.api_questions.exceptions;

public class NotEnoughPeoplesInCastException extends Exception {
    public NotEnoughPeoplesInCastException(int id) {
        super("Not enough cast in TMDB for " + id + "!");
    }
}
