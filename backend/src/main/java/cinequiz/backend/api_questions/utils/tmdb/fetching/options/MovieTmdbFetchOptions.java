package cinequiz.backend.api_questions.utils.tmdb.fetching.options;

public class MovieTmdbFetchOptions extends ShowTmdbFetchOptions {
    private boolean budget, revenue;

    public MovieTmdbFetchOptions(boolean title, boolean poster, boolean backdrop, boolean description, boolean release_date,
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
}
