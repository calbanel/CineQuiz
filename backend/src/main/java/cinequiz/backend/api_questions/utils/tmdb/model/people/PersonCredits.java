package cinequiz.backend.api_questions.utils.tmdb.model.people;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import cinequiz.backend.api_questions.utils.tmdb.model.Item;
import cinequiz.backend.api_questions.utils.tmdb.model.media.MediaPersonCredits;

public class PersonCredits extends Item {

    @JsonProperty("cast")
    private List<MediaPersonCredits> cast;

    @JsonProperty("crew")
    private List<MediaPersonCredits> crew;

    public List<MediaPersonCredits> getCast() {
        return cast;
    }

    public void setCast(List<MediaPersonCredits> cast) {
        this.cast = cast;
    }

    public List<MediaPersonCredits> getCrew() {
        return crew;
    }

    public void setCrew(List<MediaPersonCredits> crew) {
        this.crew = crew;
    }

}
