package cinequiz.backend.api_questions.utils.tmdb.model.results_pages;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import cinequiz.backend.api_questions.utils.tmdb.model.Item;

public class CreditsPage<T> extends Item {

    @JsonProperty("cast")
    private List<T> cast;

    @JsonProperty("crew")
    private List<T> crew;

    public List<T> getCast() {
        return cast;
    }

    public void setCast(List<T> cast) {
        this.cast = cast;
    }

    public List<T> getCrew() {
        return crew;
    }

    public void setCrew(List<T> crew) {
        this.crew = crew;
    }
}
