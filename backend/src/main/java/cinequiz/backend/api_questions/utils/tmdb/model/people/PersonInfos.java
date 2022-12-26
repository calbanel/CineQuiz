package cinequiz.backend.api_questions.utils.tmdb.model.people;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import cinequiz.backend.api_questions.utils.Language;
import cinequiz.backend.api_questions.utils.tmdb.model.InfosInterface;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosType;

public class PersonInfos extends Person implements InfosInterface {

    @JsonProperty("also_know_as")
    private List<String> alsoKnownAs;

    @JsonProperty("biography")
    private String biography;

    @JsonProperty("birthday")
    private String birthday;

    @JsonProperty("deathday")
    private String deathday;

    @JsonProperty("homepage")
    private String homepage;

    @JsonProperty("imdb_id")
    private String imdbId;

    @JsonProperty("place_of_birth")
    private String placeOfBirth;

    public List<String> getAlsoKnownAs() {
        return alsoKnownAs;
    }

    public void setAlsoKnownAs(List<String> alsoKnownAs) {
        this.alsoKnownAs = alsoKnownAs;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    @Override
    public String getDescription() {
        return biography;
    }

    @Override
    public String getImage() {
        return getProfilePath();
    }

    @Override
    public String getDate() {
        return birthday;
    }

    @Override
    public String getOriginalLanguage() {
        return Language.EN.getTmdbLanguage();
    }

    @Override
    public int getGenre() {
        return getGender();
    }

    @Override
    public InfosType getInfosType() {
        return InfosType.PERSON;
    }

}
