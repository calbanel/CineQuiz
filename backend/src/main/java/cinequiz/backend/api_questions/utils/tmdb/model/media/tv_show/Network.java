package cinequiz.backend.api_questions.utils.tmdb.model.media.tv_show;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import cinequiz.backend.api_questions.utils.tmdb.model.Item;

@JsonRootName(value = "network")
public class Network extends Item {

    @JsonProperty("name")
    private String name;

    @JsonProperty("logo_path")
    private String logoPath;

    @JsonProperty("origin_country")
    private String originCountry;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoOath) {
        this.logoPath = logoOath;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

}