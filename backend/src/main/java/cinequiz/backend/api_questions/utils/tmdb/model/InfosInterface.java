package cinequiz.backend.api_questions.utils.tmdb.model;

import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosType;

public interface InfosInterface {
    public int getId();

    public String getName();

    public String getDescription();

    public String getImage();

    public String getDate();

    public String getOriginalLanguage();

    public int getGenre();

    public InfosType getInfosType();
}
