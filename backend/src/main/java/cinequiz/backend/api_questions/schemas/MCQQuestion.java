package cinequiz.backend.api_questions.schemas;

public class MCQQuestion {
    private String optionalImage;
    private String optionalText;
    private String question;
    private Choices choices;
    private String answer;

    public MCQQuestion() {
    }

    public MCQQuestion(String optionalImage, String optionalName, String question, Choices choices, String answer) {
        this.optionalImage = optionalImage;
        this.optionalText = optionalName;
        this.question = question;
        this.choices = choices;
        this.answer = answer;
    }

    public String getOptionalImage() {
        return optionalImage;
    }

    public void setOptionalImage(String optionalImage) {
        this.optionalImage = optionalImage;
    }

    public String getOptionalText() {
        return optionalText;
    }

    public void setOptionalText(String optionalText) {
        this.optionalText = optionalText;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Choices getChoices() {
        return choices;
    }

    public void setChoices(Choices choices) {
        this.choices = choices;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}