package cinequiz.backend.api_questions.utils.exceptions;

public class TypeNotSupportedException extends Exception {
    public TypeNotSupportedException(String type) {
        super("This type isn't supported! (" + type + ")");
    }
}
