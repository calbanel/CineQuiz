package cinequiz.backend.api_questions.utils;

import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosType;

public class MovieStrategy extends MediaStrategy {

    @Override
    protected InfosType getInfosType() {
        return InfosType.MOVIE;
    }

}
