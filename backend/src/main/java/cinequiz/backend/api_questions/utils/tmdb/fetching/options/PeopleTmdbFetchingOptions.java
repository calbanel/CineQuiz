package cinequiz.backend.api_questions.utils.tmdb.fetching.options;

public class PeopleTmdbFetchingOptions extends InfosTmdbFetchingOptions {
    private boolean gender;

    public PeopleTmdbFetchingOptions(boolean name, boolean image, boolean description, boolean date, boolean gender) {
        super(name, image, description, date);
        this.gender = gender;
    }

    public boolean isGender() {
        return gender;
    }

}
