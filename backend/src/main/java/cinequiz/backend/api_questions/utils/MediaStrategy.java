package cinequiz.backend.api_questions.utils;

import java.util.ArrayList;
import java.util.List;

import cinequiz.backend.BackendApplication;
import cinequiz.backend.api_questions.utils.exceptions.BadInfosTypeException;
import cinequiz.backend.api_questions.utils.exceptions.ImpossibleToFetchTmdbException;
import cinequiz.backend.api_questions.utils.exceptions.NotEnoughItemsInCreditPageException;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosTmdbFetchingOptions;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosType;
import cinequiz.backend.api_questions.utils.tmdb.fetching.MediaTmdbFetching;
import cinequiz.backend.api_questions.utils.tmdb.fetching.TmdbFetching;
import cinequiz.backend.api_questions.utils.tmdb.model.InfosInterface;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonMediaCredits;

public abstract class MediaStrategy extends GlobalStrategy {

    @Override
    protected List<InfosInterface> getInfosListForMCQ(Language language, InfosType type,
            InfosTmdbFetchingOptions options, InfosTmdbFetchingOptions similaryOptions)
            throws ImpossibleToFetchTmdbException, BadInfosTypeException {

        if (type == InfosType.PERSON) {
            throw new BadInfosTypeException(type, "getMediaListForMCQ");
        }
        List<InfosInterface> mediaList = new ArrayList<InfosInterface>();
        try {
            mediaList = MediaTmdbFetching.getRandomCoherentMedias(language, NB_CHOICES_IN_MCQ,
                    options,
                    similaryOptions, InfosType.TV);
        } catch (Exception e) {
            throw new ImpossibleToFetchTmdbException();
        }

        return mediaList;
    }

    @Override
    protected TakePartList getTakePartListForMCQ(Language language, InfosType type, int numberTakingPart,
            int numberNotTakingPart, InfosTmdbFetchingOptions options)
            throws BadInfosTypeException, NotEnoughItemsInCreditPageException {
        if (type == InfosType.PERSON) {
            throw new BadInfosTypeException(type, "getTakePartListForMCQ");
        }

        int randomGender = BackendApplication.random(TmdbFetching.MIN_GENDER_TMDB_CODE,
                TmdbFetching.MAX_GENDER_TMDB_CODE);

        List<InfosInterface> credits = null;
        InfosInterface mainItem = null;

        InfosTmdbFetchingOptions mediasOptions = new InfosTmdbFetchingOptions(true, true, false, false, false);
        while (credits == null) {
            List<InfosInterface> medias = TmdbFetching.getRandomValidInfos(language, mediasOptions, type, 2);

            mainItem = medias.get(0);
            InfosInterface otherItem = medias.get(1);

            try {
                List<PersonMediaCredits> answer = MediaTmdbFetching.getRandomCoherentPeoplesInvolvedInThisMedia(
                        mainItem.getId(),
                        language, numberTakingPart, options, randomGender, type);

                List<PersonMediaCredits> similaryCast = MediaTmdbFetching.getRandomCoherentPeoplesInvolvedInThisMedia(
                        otherItem.getId(),
                        language, numberNotTakingPart, options, randomGender,
                        type,
                        mainItem.getId());

                credits = new ArrayList<InfosInterface>(answer);
                credits.addAll(similaryCast);
            } catch (NotEnoughItemsInCreditPageException e) {
                System.err.println(e.getMessage());
            }
        }

        return new TakePartList(mainItem, credits);
    }
}
