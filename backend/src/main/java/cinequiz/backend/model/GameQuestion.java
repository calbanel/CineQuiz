package cinequiz.backend.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "A game question")
public class GameQuestion {
    @ApiModelProperty("Player score for this question")
    int score;

    @ApiModelProperty("The question's details")
    MCQQuestion question;

    @ApiModelProperty("Player response")
    String playerAnswer;

    @ApiModelProperty("Time the player take to answer")
    float answerTime;
}