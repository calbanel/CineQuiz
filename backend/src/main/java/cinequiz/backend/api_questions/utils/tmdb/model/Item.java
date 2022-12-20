package cinequiz.backend.api_questions.utils.tmdb.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item implements Serializable {

    @JsonProperty("id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Item idElement = (Item) o;

        if (id != idElement.id)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
