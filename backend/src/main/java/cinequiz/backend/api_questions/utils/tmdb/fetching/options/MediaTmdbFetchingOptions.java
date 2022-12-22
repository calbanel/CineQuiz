package cinequiz.backend.api_questions.utils.tmdb.fetching.options;

import java.util.List;

import cinequiz.backend.api_questions.utils.tmdb.model.media.MediaInterface;

public class MediaTmdbFetchingOptions {
    private boolean title, poster, backdrop, description, releaseDate;

    public MediaTmdbFetchingOptions(boolean title, boolean poster, boolean backdrop, boolean description,
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

    public boolean checkDuplicate(MediaInterface current, List<MediaInterface> dontWantDuplicata) {
        return dontWantDuplicata.stream()
                .filter(show -> (this.isTitle() && current.getTitle().equals(show.getTitle()))
                        || (this.isPoster() && current.getPosterPath().equals(show.getPosterPath()))
                        || (this.isBackdrop() && current.getBackdropPath().equals(show.getBackdropPath()))
                        || (this.isDescription() && current.getOverview().equals(show.getOverview()))
                        || (this.isReleaseDate() && current.getReleaseDate().equals(show.getReleaseDate())))
                .findFirst().isPresent();
    }

}
