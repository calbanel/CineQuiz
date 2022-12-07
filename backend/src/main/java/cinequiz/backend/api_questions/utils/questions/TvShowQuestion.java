package cinequiz.backend.api_questions.utils.questions;

public class TvShowQuestion extends Question {
        public TvShowQuestion(String fr, String en) {
                super(fr, en);
        }

        public static final MovieQuestion WHICH_BY_IMAGE = new MovieQuestion("Quelle est cette série ?",
                        "What is this series?");
        public static final MovieQuestion WHICH_BY_DESCRIPTION = new MovieQuestion(
                        "A quelle série correspond a cette description ?",
                        "Which tv show fits this description?");
        public static final MovieQuestion TAKE_PART = new MovieQuestion("Quelle personne a participé à cette série ?",
                        "Who participated in this tv shows?");
        public static final MovieQuestion DOESNT_TAKE_PART = new MovieQuestion(
                        "Quelle personne n'a pas participé à cette série ?",
                        "Who hasn't participated in this tv show?");
        public static final MovieQuestion RELEASE_DATE = new MovieQuestion(
                        "Quelle a été la date de première diffusion de cette série ?",
                        "When did this tv show first air?");
        public static final MovieQuestion HOW_MANY_EPISODES = new MovieQuestion(
                        "Combien d'épisodes compte cette série ?",
                        "How many episodes does this tv show have?");
}