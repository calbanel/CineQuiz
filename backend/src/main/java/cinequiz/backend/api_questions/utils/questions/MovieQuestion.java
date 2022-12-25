package cinequiz.backend.api_questions.utils.questions;

public class MovieQuestion extends Question {
        public MovieQuestion(String fr, String en) {
                super(fr, en);
        }

        public static final MovieQuestion WHICH_BY_IMAGE = new MovieQuestion("Quel est ce film ?",
                        "What is this movie?");

        public static final MovieQuestion WHICH_BY_DESCRIPTION = new MovieQuestion(
                        "A quel film correspond a cette description ?", "Which film fits this description?");

        public static final MovieQuestion TAKE_PART = new MovieQuestion("Quelle personne a participé à ce film ?",
                        "Who was involved in this film?");
        public static final MovieQuestion DOESNT_TAKE_PART = new MovieQuestion(
                        "Quelle personne n'a pas participé à ce film ?", "Who hasn't participated in this film?");
        public static final MovieQuestion RELEASE_DATE = new MovieQuestion("A quelle date est sortie ce film ?",
                        "When was this film released?");
}
