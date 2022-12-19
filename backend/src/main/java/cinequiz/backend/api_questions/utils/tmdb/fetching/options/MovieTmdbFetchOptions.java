package cinequiz.backend.api_questions.utils.tmdb.fetching.options;

import java.util.ArrayList;
import cinequiz.backend.api_questions.utils.tmdb.objects.show.movie.MovieInfos;

public class MovieTmdbFetchOptions extends ShowTmdbFetchOptions {
    private boolean budget, revenue;

    public MovieTmdbFetchOptions(boolean title, boolean poster, boolean backdrop, boolean description,
            boolean release_date,
            boolean budget, boolean revenue) {
        super(title, poster, backdrop, description, release_date);
        this.budget = budget;
        this.revenue = revenue;
    }

    public boolean isBudget() {
        return budget;
    }

    public boolean isRevenue() {
        return revenue;
    }

    public boolean checkDuplicate(MovieInfos current, ArrayList<MovieInfos> dontWantDuplicata) {
        return !dontWantDuplicata.stream()
                .filter(show -> (this.isTitle() && current.title.equals(show.title))
                        || (this.isPoster() && current.poster_path.equals(show.poster_path))
                        || (this.isBackdrop() && current.backdrop_path.equals(show.backdrop_path))
                        || (this.isDescription() && current.overview.equals(show.overview))
                        || (this.isReleaseDate() && current.release_date.equals(show.release_date))
                        || (this.isBudget() && current.budget == show.budget)
                        || (this.isRevenue() && current.revenue == show.revenue))
                .findFirst().isEmpty();
    }
}
