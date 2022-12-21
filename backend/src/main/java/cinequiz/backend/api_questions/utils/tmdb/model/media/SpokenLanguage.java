package cinequiz.backend.api_questions.utils.tmdb.model.media;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "spoken_language")
public class SpokenLanguage {

    @JsonProperty("english_name")
    private String englishName;

    @JsonProperty("iso_639_1")
    private String iso;

    @JsonProperty("name")
    private String name;

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

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