package cinequiz.backend.api_questions.utils.tmdb.fetching.options;

public abstract class ShowTmdbFetchOptions {
    private boolean title, poster, backdrop, description, releaseDate;

    public ShowTmdbFetchOptions(boolean title, boolean poster, boolean backdrop, boolean description,
            boolean releaseDate) {
        this.title = title;
        this.poster = poster;
        this.backdrop = backdrop;
        this.description = description;
        this.releaseDate = releaseDate;
    }

    public boolean isTitle() {
        return title;
    }

    public boolean isPoster() {
        return poster;
    }

    public boolean isBackdrop() {
        return backdrop;
    }

    public boolean isDescription() {
        return description;
    }

    public boolean isReleaseDate() {
        return releaseDate;
    }

}
