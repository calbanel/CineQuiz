package cinequiz.backend.api_questions.utils;

public class TvShowTmdbFetchOptions {
    private boolean name, image, description, nb_episodes, first_air_date;

    public TvShowTmdbFetchOptions(boolean name, boolean image, boolean description, boolean nb_episodes,
            boolean first_air_date) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.nb_episodes = nb_episodes;
        this.first_air_date = first_air_date;
    }

    public boolean isName() {
        return name;
    }

    public void setName(boolean name) {
        this.name = name;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public boolean isDescription() {
        return description;
    }

    public void setDescription(boolean description) {
        this.description = description;
    }

    public boolean isNb_episodes() {
        return nb_episodes;
    }

    public void setNb_episodes(boolean nb_episodes) {
        this.nb_episodes = nb_episodes;
    }

    public boolean isFirst_air_date() {
        return first_air_date;
    }

    public void setFirst_air_date(boolean first_air_date) {
        this.first_air_date = first_air_date;
    }

}
