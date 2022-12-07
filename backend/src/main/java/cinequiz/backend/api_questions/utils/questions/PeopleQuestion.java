package cinequiz.backend.api_questions.utils.questions;

public class PeopleQuestion extends Question {
    public PeopleQuestion(String fr, String en) {
        super(fr, en);
    }

    public static final MovieQuestion WHICH_BY_IMAGE = new MovieQuestion("Qui est cette personne ?",
            "Who is this person?");
    public static final MovieQuestion BIRTHDATE = new MovieQuestion(
            "Quelle est la date de naissance de cette personne ?", "What is the date of birth of this person?");
    public static final MovieQuestion TAKE_PART = new MovieQuestion("A quel show a participé cette personne ?",
            "In which show did this person participate?");
    public static final MovieQuestion DOESNT_TAKE_PART = new MovieQuestion(
            "A quel show n'a pas participé cette personne ?", "In which show did this person not participate?");
}