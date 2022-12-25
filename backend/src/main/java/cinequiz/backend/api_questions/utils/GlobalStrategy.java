package cinequiz.backend.api_questions.utils;

import java.util.Collections;
import java.util.List;

import cinequiz.backend.api_questions.schemas.Choices;
import cinequiz.backend.api_questions.schemas.MCQQuestion;
import cinequiz.backend.api_questions.utils.exceptions.BadInfosTypeException;
import cinequiz.backend.api_questions.utils.exceptions.ImpossibleToFetchTmdbException;
import cinequiz.backend.api_questions.utils.questions.Question;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosTmdbFetchingOptions;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosType;
import cinequiz.backend.api_questions.utils.tmdb.fetching.TmdbFetching;
import cinequiz.backend.api_questions.utils.tmdb.model.InfosInterface;

public abstract class GlobalStrategy implements MCQStrategy {
    protected final int NB_CHOICES_IN_MCQ = 4;

    @Override
    public MCQQuestion whichByImage(Language language) throws ImpossibleToFetchTmdbException {

        try {

            InfosType type = this.getInfosType();
            InfosTmdbFetchingOptions options = new InfosTmdbFetchingOptions(true, true, false, false, false);
            List<InfosInterface> list = this.getInfosListForMCQ(language, type, options);

            InfosInterface answer = list.get(0);

            Collections.shuffle(list);
            String[] choices = { list.get(0).getName(), list.get(1).getName(), list.get(2).getName(),
                    list.get(3).getName() };
            Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);

            MCQQuestion mcq = new MCQQuestion(TmdbFetching.IMG_URL_BASE + answer.getImage(), "",
                    ((Question) Question.getByInfosType(type).getDeclaredField("WHICH_BY_IMAGE").get(null))
                            .getQuestion(language),
                    choicesObject,
                    answer.getName());

            return mcq;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ImpossibleToFetchTmdbException();
        }
    }

    @Override
    public MCQQuestion whichByDescription(Language language) throws ImpossibleToFetchTmdbException {

        try {

            InfosType type = this.getInfosType();
            InfosTmdbFetchingOptions options = new InfosTmdbFetchingOptions(true, false, true, false, false);
            List<InfosInterface> list = this.getInfosListForMCQ(language, type, options);

            InfosInterface answer = list.get(0);

            Collections.shuffle(list);
            String[] choices = { list.get(0).getName(), list.get(1).getName(), list.get(2).getName(),
                    list.get(3).getName() };
            Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);

            String[] names = answer.getName().split(" ");
            String cleanDescription = answer.getDescription();
            for (String name : names)
                cleanDescription = cleanDescription.replace(name, "-");

            MCQQuestion mcq = new MCQQuestion("", cleanDescription,
                    ((Question) Question.getByInfosType(type).getDeclaredField("WHICH_BY_DESCRIPTION").get(null))
                            .getQuestion(language),
                    choicesObject,
                    answer.getName());

            return mcq;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ImpossibleToFetchTmdbException();
        }
    }

    protected abstract InfosType getInfosType();

    protected abstract List<InfosInterface> getInfosListForMCQ(Language language, InfosType type,
            InfosTmdbFetchingOptions options)
            throws ImpossibleToFetchTmdbException, BadInfosTypeException;
}
