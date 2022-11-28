package cinequiz.backend.api_questions.exceptions;

public class NotaValidMovieException extends Exception {
    public NotaValidMovieException() {
        super("This movie is not valid!");
    }
}
