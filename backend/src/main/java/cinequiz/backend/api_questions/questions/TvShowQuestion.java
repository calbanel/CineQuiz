package cinequiz.backend.api_questions.questions;

public class TvShowQuestion extends Question {
        public TvShowQuestion(String fr, String en) {
                super(fr, en);
        }

        public static final MovieQuestion WHICH_BY_IMAGE = new MovieQuestion("Quel est cette série?",
                        "What is this series?");
        public static final MovieQuestion WHICH_BY_DESCRIPTION = new MovieQuestion(
                        "A quel film correspond a cette description?",
                        "Which film fits this description?");
        public static final MovieQuestion TAKE_PART = new MovieQuestion("Quelle personne a participé à cette série?",
                        "Who participated in this series?");
        public static final MovieQuestion DOESNT_TAKE_PART = new MovieQuestion(
                        "Quelle personne n'a pas participé à cette série?",
                        "Who hasn't participated in this series?");
        public static final MovieQuestion RELEASE_DATE = new MovieQuestion(
                        "Quelle a été la date de première diffusion de cette série?",
                        "When did this series first air?");
        public static final MovieQuestion HOW_MANY_EPISODES = new MovieQuestion(
                        "Combien d'épisodes compte cette série?",
                        "How many episodes does this series have?");
}