package cinequiz.backend.api_questions.utils.tmdb.model.media;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import cinequiz.backend.api_questions.utils.tmdb.model.Item;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonCast;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonCrew;

public class MediaCredits extends Item {

    @JsonProperty("cast")
    private List<PersonCast> cast;

    @JsonProperty("crew")
    private List<PersonCrew> crew;

    public List<PersonCast> getCast() {
        return cast;
    }

    public void setCast(List<PersonCast> cast) {
        this.cast = cast;
    }

    public List<PersonCrew> getCrew() {
        return crew;
    }

    public void setCrew(List<PersonCrew> crew) {
        this.crew = crew;
    }

}
