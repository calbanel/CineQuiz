package cinequiz.backend.api_questions.utils;

import java.util.ArrayList;
import java.util.List;

import cinequiz.backend.api_questions.schemas.MCQQuestion;
import cinequiz.backend.api_questions.utils.exceptions.BadInfosTypeException;
import cinequiz.backend.api_questions.utils.exceptions.ImpossibleToFetchTmdbException;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosTmdbFetchingOptions;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosType;
import cinequiz.backend.api_questions.utils.tmdb.fetching.TmdbFetching;
import cinequiz.backend.api_questions.utils.tmdb.model.InfosInterface;

public class PersonStrategy extends GlobalStrategy {

    @Override
    public MCQQuestion date(Language language) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MCQQuestion takePart(Language language) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MCQQuestion doesntTakePart(Language language) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected InfosType getInfosType() {
        return InfosType.PERSON;
    }

    @Override
    protected List<InfosInterface> getInfosListForMCQ(Language language, InfosType type,
            InfosTmdbFetchingOptions options)
            throws ImpossibleToFetchTmdbException, BadInfosTypeException {
        try {
            InfosInterface answer = TmdbFetching.getRandomValidInfos(language, options, InfosType.PERSON, 1).get(0);
            InfosTmdbFetchingOptions otherOptions = new InfosTmdbFetchingOptions(true, false, false, false, true);
            List<InfosInterface> others = TmdbFetching.getRandomValidInfos(language, otherOptions, InfosType.PERSON,
                    NB_CHOICES_IN_MCQ - 1);

            List<InfosInterface> list = new ArrayList<InfosInterface>();
            list.add(answer);
            list.addAll(others);
            return list;
        } catch (Exception e) {
            throw new ImpossibleToFetchTmdbException();
        }
    }

}
