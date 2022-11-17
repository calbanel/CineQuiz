package cinequiz.backend.api_questions.controllers;

public class CantFindIdException extends Exception {
    public CantFindIdException(String where) {
        super("Failure to find an id in" + where);
    }
}
