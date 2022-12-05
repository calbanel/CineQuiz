package cinequiz.backend.api_questions.exceptions;

public class NotEnoughPeoplesInCast extends Exception {
    public NotEnoughPeoplesInCast() {
        super("Not enough cast for this show in TMDB!");
    }
}
