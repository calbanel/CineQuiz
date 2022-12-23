package cinequiz.backend.api_questions.utils.exceptions;

public class NotEnoughPeoplesInCastException extends Exception {
    public NotEnoughPeoplesInCastException(int id) {
        super("Not enough cast in TMDB for " + id + "!");
    }
}
