package cinequiz.backend.api_questions.utils.tmdb.fetching;

import java.util.ArrayList;
import java.util.List;

import cinequiz.backend.api_questions.utils.Language;
import cinequiz.backend.api_questions.utils.exceptions.NotEnoughItemsInCreditPageException;
import cinequiz.backend.api_questions.utils.tmdb.model.InfosInterface;

public class PeopleTmdbFetching extends TmdbFetching {

    public static List<InfosInterface> getRandomCoherentMediasWhereIsThisPerson(int personId, int number,
            Language language, InfosTmdbFetchingOptions options)
            throws NotEnoughItemsInCreditPageException {
        return getRandomCoherentMediasWhereIsThisPerson(personId, number, language,
                options, UNVAILABLE_ID);
    }

    public static List<InfosInterface> getRandomCoherentMediasWhereIsThisPerson(int personId, int number,
            Language language, InfosTmdbFetchingOptions options, int alreadyIn)
            throws NotEnoughItemsInCreditPageException {
        List<InfosInterface> medias = new ArrayList<InfosInterface>();

        List<InfosInterface> credits = getCredits(personId, language, InfosType.PERSON);
        if (credits == null)
            throw new NotEnoughItemsInCreditPageException(personId);

        List<InfosInterface> creditsFiltered = new ArrayList<InfosInterface>(filterInfosList(credits, options));

        // browse the clean cast list
        for (InfosInterface c : creditsFiltered) {
            if (alreadyIn != UNVAILABLE_ID && MediaTmdbFetching.isCastIsInThisMedia(alreadyIn, c.getId(), language,
                    c.getInfosType().getTmdbValue()))
                continue;

            if (medias.stream().filter(p -> p.getName().equals(c.getName())).findFirst().isEmpty())
                medias.add(c);

            // stop the research when we have the number of medias we want
            if (medias.size() >= number)
                break;
        }

        // if there not enough medias in the final list, throw exception
        if (medias.size() < number)
            throw new NotEnoughItemsInCreditPageException(personId);

        return medias;
    }
}
