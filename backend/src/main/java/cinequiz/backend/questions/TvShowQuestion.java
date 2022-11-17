package cinequiz.backend.questions;

public class TvShowQuestion extends Question {
    public TvShowQuestion(String question) {
        super(question);
    }

    public static final MovieQuestion WHICH_BY_IMAGE = new MovieQuestion("Quel est cette série?");
    public static final MovieQuestion WHICH_BY_DESCRIPTION = new MovieQuestion(
            "A quel film correspond a cette description?");
    public static final MovieQuestion TAKE_PART = new MovieQuestion("Quelle personne a participé à cette série?");
    public static final MovieQuestion DOESNT_TAKE_PART = new MovieQuestion(
            "Quelle personne n'a pas participé à cette série?");
    public static final MovieQuestion RELEASE_DATE = new MovieQuestion(
            "Quelle a été la date de première diffusion de cette série?");
    public static final MovieQuestion HOW_MANY_EPISODES = new MovieQuestion("Combien d'épisodes compte cette série?");
}