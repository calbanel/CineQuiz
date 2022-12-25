package cinequiz.backend.api_questions.utils;

import cinequiz.backend.api_questions.schemas.MCQQuestion;
import cinequiz.backend.api_questions.utils.exceptions.BadInfosTypeException;
import cinequiz.backend.api_questions.utils.exceptions.ImpossibleToFetchTmdbException;

public interface MCQStrategy {
    public MCQQuestion whichByImage(Language language) throws ImpossibleToFetchTmdbException, BadInfosTypeException;

    public MCQQuestion whichByDescription(Language language);

    public MCQQuestion date(Language language);

    public MCQQuestion takePart(Language language);

    public MCQQuestion doesntTakePart(Language language);
}
