package cinequiz.backend.api_questions.controllers.exceptions;

public class NotEnoughPeoplesInMovieCast extends Exception {
    public NotEnoughPeoplesInMovieCast() {
        super("Not enough cast for this movie in TMDB!");
    }
}
