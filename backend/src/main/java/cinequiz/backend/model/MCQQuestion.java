package cinequiz.backend.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "A multiple choice question")
public class MCQQuestion {
    @ApiModelProperty("An image of the movie/show/someone to recognise")
    private String optionalImage;

    @ApiModelProperty("A description or the movie/show title")
    private String optionalText;

    @ApiModelProperty("The question")
    private String question;

    @ApiModelProperty("The choices presented")
    private Choices choices;

    @ApiModelProperty("The correct answer")
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
