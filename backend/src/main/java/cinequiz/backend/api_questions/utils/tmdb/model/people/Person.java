package cinequiz.backend.api_questions.utils.tmdb.model.people;

import com.fasterxml.jackson.annotation.JsonProperty;

import cinequiz.backend.api_questions.utils.tmdb.model.Item;

public class Person extends Item {

    @JsonProperty("adult")
    private boolean adult;

    @JsonProperty("gender")
    private int gender;

    @JsonProperty("known_for_department")
    private String knownForDepartment;

    @JsonProperty("name")
    private String name;

    @JsonProperty("popularity")
    private double popularity;

    @JsonProperty("profile_path")
    private String profilePath;

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getKnownForDepartment() {
        return knownForDepartment;
    }

    public void setKnownForDepartment(String knownForDepartment) {
        this.knownForDepartment = knownForDepartment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

}
