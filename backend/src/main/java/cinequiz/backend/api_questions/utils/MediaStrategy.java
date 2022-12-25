package cinequiz.backend.api_questions.utils;

import java.util.ArrayList;
import java.util.List;

import cinequiz.backend.api_questions.utils.exceptions.BadInfosTypeException;
import cinequiz.backend.api_questions.utils.exceptions.ImpossibleToFetchTmdbException;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosTmdbFetchingOptions;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosType;
import cinequiz.backend.api_questions.utils.tmdb.fetching.MediaTmdbFetching;
import cinequiz.backend.api_questions.utils.tmdb.model.InfosInterface;

public abstract class MediaStrategy extends GlobalStrategy {
    @Override
    protected List<InfosInterface> getInfosListForMCQ(Language language, InfosType type)
            throws ImpossibleToFetchTmdbException, BadInfosTypeException {

        if (type == InfosType.PERSON) {
            throw new BadInfosTypeException(type, "getMediaListForMCQ");
        }
        List<InfosInterface> mediaList = new ArrayList<InfosInterface>();
        try {
            InfosTmdbFetchingOptions answerOptions = new InfosTmdbFetchingOptions(true, true, false, false, false);
            InfosTmdbFetchingOptions similaryOptions = new InfosTmdbFetchingOptions(true, false, false, false, false);
            mediaList = MediaTmdbFetching.getRandomCoherentMedias(language, NB_CHOICES_IN_MCQ,
                    answerOptions,
                    similaryOptions, InfosType.TV);
        } catch (Exception e) {
            throw new ImpossibleToFetchTmdbException();
        }

        return mediaList;
    }
}
