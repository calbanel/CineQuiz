package cinequiz.backend.api_questions.utils.tmdb.model.media;

public interface MediaInterface {
    public int getId();

    public String getTitle();

    public String getOverview();

    public String getPosterPath();

    public String getBackdropPath();

    public String getReleaseDate();

    public String getOriginalLanguage();
}
