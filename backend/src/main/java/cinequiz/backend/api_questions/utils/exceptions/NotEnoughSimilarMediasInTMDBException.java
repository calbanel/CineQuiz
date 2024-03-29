package cinequiz.backend.api_questions.utils.exceptions;

public class NotEnoughSimilarMediasInTMDBException extends Exception {
    public NotEnoughSimilarMediasInTMDBException(int id) {
        super("Not enough similar shows in TMDB for " + id + "!");
    }
}
