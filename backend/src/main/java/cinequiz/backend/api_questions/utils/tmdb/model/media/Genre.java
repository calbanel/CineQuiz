package cinequiz.backend.api_questions.utils.tmdb.model.media;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import cinequiz.backend.api_questions.utils.tmdb.model.Item;

@JsonRootName(value = "genre")
public class Genre extends Item {

    @JsonProperty("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
