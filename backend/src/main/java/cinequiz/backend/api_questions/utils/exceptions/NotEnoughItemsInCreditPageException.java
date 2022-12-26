package cinequiz.backend.api_questions.utils.exceptions;

public class NotEnoughItemsInCreditPageException extends Exception {
    public NotEnoughItemsInCreditPageException(int id) {
        super("Not enough items on this credit page in TMDB for " + id + "!");
    }
}
