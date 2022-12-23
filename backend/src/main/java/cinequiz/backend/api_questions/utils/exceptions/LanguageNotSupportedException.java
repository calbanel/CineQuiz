package cinequiz.backend.api_questions.utils.exceptions;

public class LanguageNotSupportedException extends Exception {
    public LanguageNotSupportedException(String lan) {
        super("This language isn't supported! (" + lan + ")");
    }
}
