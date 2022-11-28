package cinequiz.backend.api_questions.exceptions;

public class LanguageNotSupportedException extends Exception {
    public LanguageNotSupportedException() {
        super("This language isn't supported!");
    }
}
