package cinequiz.backend.api_questions.utils;

import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosType;

public class TvStrategy extends MediaStrategy {

    @Override
    protected InfosType getInfosType() {
        return InfosType.TV;
    }

}
