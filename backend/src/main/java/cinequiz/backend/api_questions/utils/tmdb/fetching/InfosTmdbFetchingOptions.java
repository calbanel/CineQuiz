package cinequiz.backend.api_questions.utils.tmdb.fetching;

import java.util.List;

import cinequiz.backend.api_questions.utils.tmdb.model.InfosInterface;

public class InfosTmdbFetchingOptions {
    private boolean name, image, description, date, genre;

    public InfosTmdbFetchingOptions(boolean name, boolean image, boolean description,
            boolean date, boolean genre) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.date = date;
        this.genre = genre;
    }

    public boolean isName() {
        return name;
    }

    public boolean isImage() {
        return image;
    }

    public boolean isDescription() {
        return description;
    }

    public boolean isDate() {
        return date;
    }

    public boolean isGenre() {
        return genre;
    }

    public boolean checkDuplicate(InfosInterface current, List<InfosInterface> dontWantDuplicata) {
        return dontWantDuplicata.stream()
                .filter(show -> (this.isName() && current.getName().equals(show.getName()))
                        || (this.isImage() && current.getImage().equals(show.getImage()))
                        || (this.isDescription() && current.getDescription().equals(show.getDescription()))
                        || (this.isDate() && current.getDate().equals(show.getDate()))
                        || (this.isGenre() && current.getGenre() == show.getGenre()))
                .findFirst().isPresent();
    }

}
