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
    public MCQQuestion whichByDescription(Language language) {
        // TODO Auto-generated method stub
        return null;
    }

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
    protected List<InfosInterface> getInfosListForMCQ(Language language, InfosType type)
            throws ImpossibleToFetchTmdbException, BadInfosTypeException {
        List<InfosInterface> personList = new ArrayList<InfosInterface>();
        try {
            InfosTmdbFetchingOptions options = new InfosTmdbFetchingOptions(true, true, false, false, false);
            personList = TmdbFetching.getRandomValidInfos(language, options, InfosType.PERSON, NB_CHOICES_IN_MCQ);
        } catch (Exception e) {
            throw new ImpossibleToFetchTmdbException();
        }
        return personList;
    }

}
