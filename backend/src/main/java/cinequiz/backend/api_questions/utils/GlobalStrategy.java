package cinequiz.backend.api_questions.utils;

import java.util.Collections;
import java.util.List;

import cinequiz.backend.api_questions.schemas.Choices;
import cinequiz.backend.api_questions.schemas.MCQQuestion;
import cinequiz.backend.api_questions.utils.exceptions.BadInfosTypeException;
import cinequiz.backend.api_questions.utils.exceptions.ImpossibleToFetchTmdbException;
import cinequiz.backend.api_questions.utils.questions.Question;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosType;
import cinequiz.backend.api_questions.utils.tmdb.fetching.TmdbFetching;
import cinequiz.backend.api_questions.utils.tmdb.model.InfosInterface;

public abstract class GlobalStrategy implements MCQStrategy {
    protected final int NB_CHOICES_IN_MCQ = 4;

    @Override
    public MCQQuestion whichByImage(Language language) throws ImpossibleToFetchTmdbException, BadInfosTypeException {

        InfosType type = this.getInfosType();
        List<InfosInterface> list = this.getInfosListForMCQ(language, type);

        InfosInterface answer = list.get(0);
        Collections.shuffle(list);
        String[] choices = { list.get(0).getName(), list.get(1).getName(), list.get(2).getName(),
                list.get(3).getName() };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = null;
        try {
            mcq = new MCQQuestion(TmdbFetching.IMG_URL_BASE + answer.getImage(), "",
                    ((Question) Question.getByInfosType(type).getDeclaredField("WHICH_BY_IMAGE").get(null))
                            .getQuestion(language),
                    choicesObject,
                    answer.getName());
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return mcq;
    }

    protected abstract InfosType getInfosType();

    protected abstract List<InfosInterface> getInfosListForMCQ(Language language, InfosType type)
            throws ImpossibleToFetchTmdbException, BadInfosTypeException;
}
