package cinequiz.backend.api_questions.utils.questions;

public class PeopleQuestion extends Question {
        public PeopleQuestion(String fr, String en) {
                super(fr, en);
        }

        public static final PeopleQuestion WHICH_BY_IMAGE = new PeopleQuestion("Qui est cette personne ?",
                        "Who is this person?");
        public static final PeopleQuestion BIRTHDATE = new PeopleQuestion(
                        "Quelle est la date de naissance de cette personne ?",
                        "What is the date of birth of this person?");
        public static final PeopleQuestion TAKE_PART = new PeopleQuestion("A quel show a participé cette personne ?",
                        "In which show did this person participate?");
        public static final PeopleQuestion DOESNT_TAKE_PART = new PeopleQuestion(
                        "A quel show n'a pas participé cette personne ?",
                        "In which show did this person not participate?");
}