package cinequiz.backend.api_questions.exceptions;

public class NotaValidShowException extends Exception {
    public NotaValidShowException() {
        super("This show is not valid!");
    }
}
