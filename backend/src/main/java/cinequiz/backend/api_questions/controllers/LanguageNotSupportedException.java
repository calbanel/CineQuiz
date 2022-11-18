package cinequiz.backend.api_questions.controllers;

public class LanguageNotSupportedException extends Exception {
    public LanguageNotSupportedException() {
        super("This language isn't supported!");
    }
}
