package cinequiz.backend.api_questions.questions;

public class PeopleQuestion extends Question {
    public PeopleQuestion(String question) {
        super(question);
    }

    public static final MovieQuestion WHICH_BY_IMAGE = new MovieQuestion("Quel est cette personne?");
    public static final MovieQuestion BIRTHDATE = new MovieQuestion(
            "Quelle est la date de naissance de cette personne?");
    public static final MovieQuestion TAKE_PART = new MovieQuestion("Dans quel show a participé cette personne?");
    public static final MovieQuestion DOESNT_TAKE_PART = new MovieQuestion(
            "Dans quel show n'a pas participé cette personne?");
}