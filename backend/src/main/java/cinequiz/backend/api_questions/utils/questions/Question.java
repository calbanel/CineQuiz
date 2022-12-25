package cinequiz.backend.api_questions.utils.questions;

import cinequiz.backend.api_questions.utils.Language;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosType;

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

    public static Class<? extends Question> getByInfosType(InfosType type) {
        if (type == InfosType.MOVIE) {
            return MovieQuestion.class;
        } else if (type == InfosType.TV) {
            return TvShowQuestion.class;
        } else if (type == InfosType.PERSON) {
            return PeopleQuestion.class;
        }
        return null;
    }

    public static final MovieQuestion WHICH_BY_IMAGE = null;

    public static final MovieQuestion WHICH_BY_DESCRIPTION = null;

    public static final MovieQuestion TAKE_PART = null;

    public static final MovieQuestion DOESNT_TAKE_PART = null;

    public static final MovieQuestion RELEASE_DATE = null;
}
