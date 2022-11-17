package cinequiz.backend;

public class MCQQuestion {
    private String image;
    private String question;
    private Choices choices;
    private String answer;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public MCQQuestion() {
    }

    public MCQQuestion(String image, String question, Choices choices, String answer) {
        this.image = image;
        this.question = question;
        this.choices = choices;
        this.answer = answer;
    }
}
