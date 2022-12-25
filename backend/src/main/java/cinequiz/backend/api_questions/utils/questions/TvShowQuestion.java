package cinequiz.backend.api_questions.utils.questions;

public class TvShowQuestion extends Question {
        public TvShowQuestion(String fr, String en) {
                super(fr, en);
        }

        public static final TvShowQuestion WHICH_BY_IMAGE = new TvShowQuestion("Quelle est cette série ?",
                        "What is this series?");
        public static final TvShowQuestion WHICH_BY_DESCRIPTION = new TvShowQuestion(
                        "A quelle série correspond a cette description ?",
                        "Which tv show fits this description?");
        public static final TvShowQuestion TAKE_PART = new TvShowQuestion("Quelle personne a participé à cette série ?",
                        "Who participated in this tv shows?");
        public static final TvShowQuestion DOESNT_TAKE_PART = new TvShowQuestion(
                        "Quelle personne n'a pas participé à cette série ?",
                        "Who hasn't participated in this tv show?");
        public static final TvShowQuestion DATE = new TvShowQuestion(
                        "Quelle a été la date de première diffusion de cette série ?",
                        "When did this tv show first air?");
}