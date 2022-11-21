package cinequiz.backend.api_questions.controllers.utils;

public class PeopleTmdbFetchOptions {
    private boolean name, profile_path, gender;

    public PeopleTmdbFetchOptions(boolean name, boolean profile_path, boolean gender) {
        this.name = name;
        this.profile_path = profile_path;
        this.gender = gender;
    }

    public boolean isName() {
        return name;
    }

    public void setName(boolean name) {
        this.name = name;
    }

    public boolean isProfile_path() {
        return profile_path;
    }

    public void setProfile_path(boolean profile_path) {
        this.profile_path = profile_path;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

}
