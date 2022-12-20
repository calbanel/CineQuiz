package cinequiz.backend.api_questions.utils.tmdb.model.show;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "production_country")
public class ProductionCountry {

    @JsonProperty("iso_3166_1")
    private String iso;

    @JsonProperty("name")
    private String name;

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
