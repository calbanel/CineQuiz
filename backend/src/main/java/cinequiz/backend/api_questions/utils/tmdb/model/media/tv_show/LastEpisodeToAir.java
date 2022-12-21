package cinequiz.backend.api_questions.utils.tmdb.model.media.tv_show;

import com.fasterxml.jackson.annotation.JsonProperty;

import cinequiz.backend.api_questions.utils.tmdb.model.Item;

public class LastEpisodeToAir extends Item {

    @JsonProperty("air_date")
    private String airDate;

    @JsonProperty("episode_number")
    private int episodeNumber;

    @JsonProperty("name")
    private String name;

    @JsonProperty("overview")
    private String overview;

    @JsonProperty("production_code")
    private String productionCode;

    @JsonProperty("runtime")
    private int runtime;

    @JsonProperty("season_number")
    private int seasonNumber;

    @JsonProperty("show_id")
    private int showId;

    @JsonProperty("still_path")
    private String stillPath;

    @JsonProperty("vote_average")
    private double voteAverage;

    @JsonProperty("vote_count")
    private int voteCount;

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getProductionCode() {
        return productionCode;
    }

    public void setProductionCode(String productionCode) {
        this.productionCode = productionCode;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public String getStillPath() {
        return stillPath;
    }

    public void setStillPath(String stillPath) {
        this.stillPath = stillPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

}
