package cinequiz.backend.api_questions.utils;

import cinequiz.backend.api_questions.schemas.MCQQuestion;

public interface MCQStrategy {
    public MCQQuestion whichByImage();

    public MCQQuestion whichByDescription();

    public MCQQuestion date();

    public MCQQuestion takePart();

    public MCQQuestion doesntTakePart();
}
