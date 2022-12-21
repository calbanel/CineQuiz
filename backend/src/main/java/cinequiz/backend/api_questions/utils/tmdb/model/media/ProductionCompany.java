package cinequiz.backend.api_questions.utils.tmdb.model.media;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import cinequiz.backend.api_questions.utils.tmdb.model.Item;

@JsonRootName(value = "production_company")
public class ProductionCompany extends Item {

    @JsonProperty("logo_path")
    private String logoPath;

    @JsonProperty("name")
    private String name;

    @JsonProperty("origin_country")
    private String originCountry;

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

}
