package cinequiz.backend.api_questions.utils.tmdb.fetching.options;

import java.util.ArrayList;

import cinequiz.backend.api_questions.utils.tmdb.model.show.tv_show.TvShowInfos;

public class TvShowTmdbFetchOptions extends MediaTmdbFetchingOptions {
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

    public boolean checkDuplicate(TvShowInfos current, ArrayList<TvShowInfos> dontWantDuplicata) {
        return dontWantDuplicata.stream()
                .filter(show -> (this.isTitle() && current.name.equals(show.name))
                        || (this.isPoster() && current.poster_path.equals(show.poster_path))
                        || (this.isBackdrop() && current.backdrop_path.equals(show.backdrop_path))
                        || (this.isDescription() && current.overview.equals(show.overview))
                        || (this.isReleaseDate() && current.first_air_date.equals(show.first_air_date))
                        || (this.isNb_episodes() && current.number_of_episodes == show.number_of_episodes))
                .findFirst().isPresent();
    }

}
