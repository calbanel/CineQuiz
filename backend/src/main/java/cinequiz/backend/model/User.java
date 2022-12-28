package cinequiz.backend.model;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import io.swagger.annotations.ApiModel;

@Getter
@Setter
@ApiModel(description = "A user")
@Document(collection="User")
public class User{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String id;
    private String pseudo;
    private String email;
    private String password;
    private int score;
}