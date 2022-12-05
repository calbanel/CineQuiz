package cinequiz.backend.api_questions.exceptions;

public class NotEnoughSimilarShowsInTMDBException extends Exception {
    public NotEnoughSimilarShowsInTMDBException() {
        super("Not enough similar shows in TMDB!");
    }
}
