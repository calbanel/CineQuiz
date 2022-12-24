package cinequiz.backend.api_questions.utils.tmdb.fetching.options;

import java.util.List;

import cinequiz.backend.api_questions.utils.tmdb.model.InfosInterface;

public class InfosTmdbFetchingOptions {
    private boolean name, image, description, date;

    public InfosTmdbFetchingOptions(boolean name, boolean image, boolean description,
            boolean date) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.date = date;
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

    public boolean checkDuplicate(InfosInterface current, List<InfosInterface> dontWantDuplicata) {
        return dontWantDuplicata.stream()
                .filter(show -> (this.isName() && current.getName().equals(show.getName()))
                        || (this.isImage() && current.getImage().equals(show.getImage()))
                        || (this.isDescription() && current.getDescription().equals(show.getDescription()))
                        || (this.isDate() && current.getDate().equals(show.getDate())))
                .findFirst().isPresent();
    }

}
