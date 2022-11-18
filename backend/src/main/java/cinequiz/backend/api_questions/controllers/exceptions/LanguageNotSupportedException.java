package cinequiz.backend.api_questions.controllers.exceptions;

public class LanguageNotSupportedException extends Exception {
    public LanguageNotSupportedException() {
        super("This language isn't supported!");
    }
}
