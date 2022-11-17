package cinequiz.backend;

public class MovieQuestion extends Question {
    public MovieQuestion(String question) {
        super(question);
    }

    public static final MovieQuestion WHICH_BY_IMAGE = new MovieQuestion("Quel est ce film?");
}
