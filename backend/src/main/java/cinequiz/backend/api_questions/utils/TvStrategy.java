package cinequiz.backend.api_questions.utils;

import cinequiz.backend.api_questions.schemas.MCQQuestion;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosType;

public class TvStrategy extends MediaStrategy {

    @Override
    public MCQQuestion whichByDescription(Language language) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MCQQuestion date(Language language) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MCQQuestion takePart(Language language) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MCQQuestion doesntTakePart(Language language) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected InfosType getInfosType() {
        return InfosType.TV;
    }

}
