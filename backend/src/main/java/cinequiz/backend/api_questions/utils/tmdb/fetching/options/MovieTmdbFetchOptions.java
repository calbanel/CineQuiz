package cinequiz.backend.api_questions.utils.tmdb.fetching.options;

import java.util.ArrayList;

import cinequiz.backend.api_questions.utils.tmdb.model.show.movie.MovieInfos;

public class MovieTmdbFetchOptions extends ShowTmdbFetchOptions {

    public MovieTmdbFetchOptions(boolean title, boolean poster, boolean backdrop, boolean description,
            boolean release_date) {
        super(title, poster, backdrop, description, release_date);
    }

    public boolean checkDuplicate(MovieInfos current, ArrayList<MovieInfos> dontWantDuplicata) {
        return dontWantDuplicata.stream()
                .filter(show -> (this.isTitle() && current.getTitle().equals(show.getTitle()))
                        || (this.isPoster() && current.getPosterPath().equals(show.getPosterPath()))
                        || (this.isBackdrop() && current.getBackdropPath().equals(show.getBackdropPath()))
                        || (this.isDescription() && current.getOverview().equals(show.getOverview()))
                        || (this.isReleaseDate() && current.getReleaseDate().equals(show.getReleaseDate())))
                .findFirst().isPresent();
    }
}
