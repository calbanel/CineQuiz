package cinequiz.backend.questions;

public abstract class Question {
    private String question;

    public Question(String question) {
        this.question = question;
    }

    // ici on pourrait rajouter un attribut "langage" ce qui permettrait d'avoir une
    // version FR et Anglais
    public String getQuestion() {
        return question;
    }
}
