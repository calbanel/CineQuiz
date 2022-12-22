package cinequiz.backend.api_questions.utils.tmdb.model.people;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PersonCast extends PersonMovieCredits {

    @JsonProperty("character")
    private String character;

    @JsonProperty("order")
    private int order;

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}