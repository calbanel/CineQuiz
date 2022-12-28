package cinequiz.backend.api_questions.utils;

import cinequiz.backend.api_questions.utils.exceptions.ImpossibleToFetchTmdbException;
import cinequiz.backend.model.MCQQuestion;

public interface MCQStrategy {
    public MCQQuestion whichByImage(Language language) throws ImpossibleToFetchTmdbException;

    public MCQQuestion whichByDescription(Language language) throws ImpossibleToFetchTmdbException;

    public MCQQuestion date(Language language) throws ImpossibleToFetchTmdbException;

    public MCQQuestion takePart(Language language) throws ImpossibleToFetchTmdbException;

    public MCQQuestion doesntTakePart(Language language) throws ImpossibleToFetchTmdbException;
}
