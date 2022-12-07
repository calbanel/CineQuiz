package cinequiz.backend.api_questions.utils.tmdb.fetching.options;

public class TvShowTmdbFetchOptions extends ShowTmdbFetchOptions {
    private boolean nb_episodes;

    public TvShowTmdbFetchOptions(boolean name, boolean poster, boolean backdrop, boolean description,
            boolean first_air_date,
            boolean nb_episodes) {
        super(name, poster, backdrop, description, first_air_date);
        this.nb_episodes = nb_episodes;
    }

    public boolean isNb_episodes() {
        return nb_episodes;
    }

}
