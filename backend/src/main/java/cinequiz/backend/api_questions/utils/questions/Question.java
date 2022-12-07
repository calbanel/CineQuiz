package cinequiz.backend.api_questions.utils.questions;

import cinequiz.backend.api_questions.utils.Language;

public abstract class Question {
    private String fr;
    private String en;

    public Question(String fr, String en) {
        this.fr = fr;
        this.en = en;
    }

    // ici on pourrait rajouter un attribut "langage" ce qui permettrait d'avoir une
    // version FR et Anglais
    public String getQuestion(Language language) {
        String question = this.fr;
        if (language == Language.EN)
            question = this.en;
        return question;
    }
}
