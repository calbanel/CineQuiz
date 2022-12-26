package cinequiz.backend.api_questions.utils;

import java.util.Collections;
import java.util.List;

import cinequiz.backend.api_questions.schemas.Choices;
import cinequiz.backend.api_questions.schemas.MCQQuestion;
import cinequiz.backend.api_questions.utils.exceptions.BadInfosTypeException;
import cinequiz.backend.api_questions.utils.exceptions.ImpossibleToFetchTmdbException;
import cinequiz.backend.api_questions.utils.exceptions.NotEnoughItemsInCreditPageException;
import cinequiz.backend.api_questions.utils.questions.Question;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosTmdbFetchingOptions;
import cinequiz.backend.api_questions.utils.tmdb.fetching.InfosType;
import cinequiz.backend.api_questions.utils.tmdb.fetching.TmdbFetching;
import cinequiz.backend.api_questions.utils.tmdb.model.InfosInterface;

public abstract class GlobalStrategy implements MCQStrategy {
    protected final int NB_CHOICES_IN_MCQ = 4;
    protected final int NB_TAKING_PART = 1;
    protected final int NB_NOT_TAKING_PART = 3;

    @Override
    public MCQQuestion whichByImage(Language language) throws ImpossibleToFetchTmdbException {

        try {

            InfosType type = this.getInfosType();
            InfosTmdbFetchingOptions options = new InfosTmdbFetchingOptions(true, true, false, false, false);
            InfosTmdbFetchingOptions similaryOptions = new InfosTmdbFetchingOptions(true, false, false, false, false);
            List<InfosInterface> list = this.getInfosListForMCQ(language, type, options, similaryOptions);

            InfosInterface answer = list.get(0);

            Collections.shuffle(list);
            String[] choices = { list.get(0).getName(), list.get(1).getName(), list.get(2).getName(),
                    list.get(3).getName() };
            Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);

            String question = ((Question) Question.getByInfosType(type).getDeclaredField("WHICH_BY_IMAGE").get(null))
                    .getQuestion(language);
            MCQQuestion mcq = new MCQQuestion(TmdbFetching.IMG_URL_BASE + answer.getImage(), "",
                    question,
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
            InfosTmdbFetchingOptions similaryOptions = new InfosTmdbFetchingOptions(true, false, false, false, false);
            List<InfosInterface> list = this.getInfosListForMCQ(language, type, options, similaryOptions);

            InfosInterface answer = list.get(0);

            Collections.shuffle(list);
            String[] choices = { list.get(0).getName(), list.get(1).getName(), list.get(2).getName(),
                    list.get(3).getName() };
            Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);

            String[] names = answer.getName().split(" ");
            String cleanDescription = answer.getDescription();
            for (String name : names)
                cleanDescription = cleanDescription.replace(name, "-");

            String question = ((Question) Question.getByInfosType(type).getDeclaredField("WHICH_BY_DESCRIPTION")
                    .get(null))
                    .getQuestion(language);
            MCQQuestion mcq = new MCQQuestion("", cleanDescription,
                    question,
                    choicesObject,
                    answer.getName());

            return mcq;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ImpossibleToFetchTmdbException();
        }
    }

    @Override
    public MCQQuestion date(Language language) throws ImpossibleToFetchTmdbException {

        try {

            InfosType type = this.getInfosType();
            InfosTmdbFetchingOptions options = new InfosTmdbFetchingOptions(true, true, false, true, false);
            InfosTmdbFetchingOptions similaryOptions = new InfosTmdbFetchingOptions(true, false, false, true, false);
            List<InfosInterface> list = this.getInfosListForMCQ(language, type, options, similaryOptions);

            InfosInterface answer = list.get(0);

            Collections.shuffle(list);
            String[] choices = { list.get(0).getDate(), list.get(1).getDate(), list.get(2).getDate(),
                    list.get(3).getDate() };
            Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);

            String question = ((Question) Question.getByInfosType(type).getDeclaredField("DATE").get(null))
                    .getQuestion(language);
            MCQQuestion mcq = new MCQQuestion(TmdbFetching.IMG_URL_BASE + answer.getImage(), answer.getName(),
                    question,
                    choicesObject,
                    answer.getDate());

            return mcq;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ImpossibleToFetchTmdbException();
        }
    }

    @Override
    public MCQQuestion takePart(Language language) throws ImpossibleToFetchTmdbException {
        try {
            InfosType type = this.getInfosType();

            InfosTmdbFetchingOptions options = new InfosTmdbFetchingOptions(true, false, false, false, false);
            TakePartList takePartList = this.getTakePartListForMCQ(language, type, NB_TAKING_PART,
                    NB_NOT_TAKING_PART, options);

            InfosInterface mainItem = takePartList.getItem();
            List<InfosInterface> credits = takePartList.getList();

            InfosInterface answer = credits.get(0);

            Collections.shuffle(credits);
            String[] choices = { credits.get(0).getName(), credits.get(1).getName(), credits.get(2).getName(),
                    credits.get(3).getName() };
            Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);

            String question = ((Question) Question.getByInfosType(type).getDeclaredField("TAKE_PART").get(null))
                    .getQuestion(language);
            MCQQuestion mcq = new MCQQuestion(TmdbFetching.IMG_URL_BASE + mainItem.getImage(), mainItem.getName(),
                    question,
                    choicesObject,
                    answer.getName());

            return mcq;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ImpossibleToFetchTmdbException();
        }
    }

    @Override
    public MCQQuestion doesntTakePart(Language language) throws ImpossibleToFetchTmdbException {
        try {
            InfosType type = this.getInfosType();

            InfosTmdbFetchingOptions options = new InfosTmdbFetchingOptions(true, false, false, false, false);
            TakePartList takePartList = this.getTakePartListForMCQ(language, type, NB_NOT_TAKING_PART,
                    NB_TAKING_PART, options);

            InfosInterface mainItem = takePartList.getItem();
            List<InfosInterface> credits = takePartList.getList();

            InfosInterface answer = credits.get(NB_CHOICES_IN_MCQ - 1);

            Collections.shuffle(credits);
            String[] choices = { credits.get(0).getName(), credits.get(1).getName(), credits.get(2).getName(),
                    credits.get(3).getName() };
            Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);

            String question = ((Question) Question.getByInfosType(type).getDeclaredField("DOESNT_TAKE_PART").get(null))
                    .getQuestion(language);
            MCQQuestion mcq = new MCQQuestion(TmdbFetching.IMG_URL_BASE + mainItem.getImage(), mainItem.getName(),
                    question,
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
            InfosTmdbFetchingOptions options, InfosTmdbFetchingOptions similaryOptions)
            throws ImpossibleToFetchTmdbException, BadInfosTypeException;

    protected class TakePartList {
        private InfosInterface item;
        private List<InfosInterface> list;

        public TakePartList(InfosInterface item, List<InfosInterface> list) {
            this.item = item;
            this.list = list;
        }

        public InfosInterface getItem() {
            return item;
        }

        public List<InfosInterface> getList() {
            return list;
        }
    }

    protected abstract TakePartList getTakePartListForMCQ(Language language, InfosType type,
            int numberTakingPart, int numberNotTakingPart, InfosTmdbFetchingOptions options)
            throws BadInfosTypeException, NotEnoughItemsInCreditPageException;
}
