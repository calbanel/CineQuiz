package cinequiz.backend.api_questions.utils;

import cinequiz.backend.api_questions.schemas.MCQQuestion;

public class MCQAssembler {
    MCQStrategy strategy;

    public void setStrategy(MCQStrategy strategy) {
        this.strategy = strategy;
    }

    public MCQQuestion getWhichByImageMCQ() {
        return strategy.whichByImage();
    }
}
