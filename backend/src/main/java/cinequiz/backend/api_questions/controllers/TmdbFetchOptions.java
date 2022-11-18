package cinequiz.backend.api_questions.controllers;

public class TmdbFetchOptions {
    private boolean title, image, description, budget, revenue, release_date;

    public TmdbFetchOptions(boolean title, boolean image, boolean description, boolean budget, boolean revenue,
            boolean release_date) {
        this.title = title;
        this.image = image;
        this.description = description;
        this.budget = budget;
        this.revenue = revenue;
        this.release_date = release_date;
    }

    public boolean isTitle() {
        return title;
    }

    public boolean isImage() {
        return image;
    }

    public boolean isDescription() {
        return description;
    }

    public boolean isBudget() {
        return budget;
    }

    public boolean isRevenue() {
        return revenue;
    }

    public boolean isRelease_date() {
        return release_date;
    }

}
