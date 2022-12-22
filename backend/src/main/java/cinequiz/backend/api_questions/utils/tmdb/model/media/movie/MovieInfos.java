package cinequiz.backend.api_questions.utils.tmdb.model.media.movie;

import com.fasterxml.jackson.annotation.JsonProperty;

import cinequiz.backend.api_questions.utils.tmdb.model.media.MediaInfos;

public class MovieInfos extends MediaInfos {

    @JsonProperty("budget")
    private long budget;

    @JsonProperty("imdb_id")
    private String imdbId;

    @JsonProperty("original_title")
    private String originalTitle;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("revenue")
    private long revenue;

    @JsonProperty("runtime")
    private int runtime;

    @JsonProperty("title")
    private String title;

    @JsonProperty("video")
    private boolean video;

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

}
