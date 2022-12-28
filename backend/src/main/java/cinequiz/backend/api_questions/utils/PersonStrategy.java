package cinequiz.backend.api_questions.utils;

import java.util.ArrayList;
import java.util.List;

import cinequiz.backend.api_questions.utils.exceptions.BadInfosTypeException;
import cinequiz.backend.api_questions.utils.exceptions.ImpossibleToFetchTmdbException;
import cinequiz.backend.api_questions.utils.exceptions.NotEnoughItemsInCreditPageException;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosTmdbFetchingOptions;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosType;
import cinequiz.backend.api_questions.utils.tmdb.fetching.PeopleTmdbFetching;
import cinequiz.backend.api_questions.utils.tmdb.fetching.TmdbFetching;
import cinequiz.backend.api_questions.utils.tmdb.model.InfosInterface;

public class PersonStrategy extends GlobalStrategy {

    @Override
    protected InfosType getInfosType() {
        return InfosType.PERSON;
    }

    @Override
    protected List<InfosInterface> getInfosListForMCQ(Language language, InfosType type,
            InfosTmdbFetchingOptions options, InfosTmdbFetchingOptions similaryOptions)
            throws ImpossibleToFetchTmdbException, BadInfosTypeException {

        if (type != InfosType.PERSON) {
            throw new BadInfosTypeException(type, "getMediaListForMCQ");
        }
        try {
            return TmdbFetching.getRandomValidInfos(language, options, InfosType.PERSON, NB_CHOICES_IN_MCQ);
        } catch (Exception e) {
            throw new ImpossibleToFetchTmdbException();
        }
    }

    @Override
    protected TakePartList getTakePartListForMCQ(Language language, InfosType type, int numberTakingPart,
            int numberNotTakingPart, InfosTmdbFetchingOptions options)
            throws BadInfosTypeException, NotEnoughItemsInCreditPageException {
        if (type != InfosType.PERSON) {
            throw new BadInfosTypeException(type, "getTakePartListForMCQ");
        }

        List<InfosInterface> movieList = null;
        InfosInterface mainPerson = null;

        InfosTmdbFetchingOptions itemOptions = new InfosTmdbFetchingOptions(true, true, false, false, false);
        while (movieList == null) {
            List<InfosInterface> items = TmdbFetching.getRandomValidInfos(language, itemOptions, type, 2);

            mainPerson = items.get(0);
            InfosInterface otherPerson = items.get(1);

            try {
                List<InfosInterface> itemsMovie = PeopleTmdbFetching.getRandomCoherentMediasWhereIsThisPerson(
                        mainPerson.getId(),
                        numberTakingPart, language, options);

                List<InfosInterface> otherMovies = PeopleTmdbFetching.getRandomCoherentMediasWhereIsThisPerson(
                        otherPerson.getId(), numberNotTakingPart, language, options, mainPerson.getId());

                movieList = new ArrayList<InfosInterface>(itemsMovie);
                movieList.addAll(otherMovies);
            } catch (NotEnoughItemsInCreditPageException e) {
                System.err.println(e.getMessage());
            }
        }

        return new TakePartList(mainPerson, movieList);
    }

}
