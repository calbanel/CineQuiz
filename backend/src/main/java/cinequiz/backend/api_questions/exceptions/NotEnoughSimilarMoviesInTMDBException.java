package cinequiz.backend.api_questions.exceptions;

public class NotEnoughSimilarMoviesInTMDBException extends Exception {
    public NotEnoughSimilarMoviesInTMDBException() {
        super("Not enough similar movies in TMDB!");
    }
}
