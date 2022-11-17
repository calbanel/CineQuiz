package cinequiz.backend.api_questions.questions;

public class MovieQuestion extends Question {
    public MovieQuestion(String question) {
        super(question);
    }

    public static final MovieQuestion WHICH_BY_IMAGE = new MovieQuestion("Quel est ce film?");
    public static final MovieQuestion BUDGET = new MovieQuestion("Quel est le budget pour ce film?");
    public static final MovieQuestion REVENUE = new MovieQuestion("Quel a été le revenue généré par ce film?");
    public static final MovieQuestion WHICH_BY_DESCRIPTION = new MovieQuestion(
            "A quel film correspond a cette description?");
    public static final MovieQuestion TAKE_PART = new MovieQuestion("Quelle personne a participé à ce film?");
    public static final MovieQuestion DOESNT_TAKE_PART = new MovieQuestion(
            "Quelle personne n'a pas participé à ce film?");
    public static final MovieQuestion RELEASE_DATE = new MovieQuestion("A quelle date est sortie ce film?");
}
