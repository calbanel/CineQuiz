package cinequiz.backend.model;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "A cinequiz game")
public class Game {
    @ApiModelProperty("The date when the game played")
    private String date;

    @ApiModelProperty("Final score of the user during this game")
    private int score;

    @ApiModelProperty("The questions of this game")
    private List<GameQuestion> questions;
}