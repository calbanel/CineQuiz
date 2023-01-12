package cinequiz.backend.model;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.GenerationType;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Getter
@Setter
@ApiModel(description = "A user having an account")
@Document(collection = "User")
public class User {
    @Id
    @ApiModelProperty("The user id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ApiModelProperty("The user pseudo in the app")
    private String pseudo;

    @ApiModelProperty("The user email")
    private String email;

    @ApiModelProperty("The user password")
    private String password;

    private static final int NB_GAMES_MAX = 10;
    @ApiModelProperty("The user played games")
    private List<Game> games;

    public void addGame(Game game) {
        games.add(game);

        if (this.games.size() > NB_GAMES_MAX) {
            this.games.remove(0);

            List<Game> newList = new ArrayList<>();
            for (Game copy : this.games) {
                newList.add(copy);
            }

            this.games = newList;
        }
    }
}