package cinequiz.backend.api_questions.controllers;

import java.util.ArrayList;
import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinequiz.backend.BackendApplication;
import cinequiz.backend.api_questions.exceptions.LanguageNotSupportedException;
import cinequiz.backend.api_questions.mcq.Choices;
import cinequiz.backend.api_questions.mcq.MCQQuestion;
import cinequiz.backend.api_questions.tmdb_objects.show.tv_show.TvShowInfos;
import cinequiz.backend.api_questions.utils.Language;
import cinequiz.backend.api_questions.utils.TvShowTmdbFetchOptions;
import cinequiz.backend.api_questions.utils.TvShowTmdbFetching;
import cinequiz.backend.api_questions.utils.questions.TvShowQuestion;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/cinequiz/questions/tv-show", method = RequestMethod.GET)
public class TvShowQuestionController {

    private final int NB_CHOICES_IN_MCQ = 4;
    private final int NB_DEFINED_QUESTIONS = 3;

    @ApiOperation(value = "Gets a random mcq about a tv show")
    @GetMapping("/random")
    public ResponseEntity<?> random_question(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        int randomQuestion = BackendApplication.random(1, NB_DEFINED_QUESTIONS);
        switch (randomQuestion) {
            case 1:
                return whichByImage(language);
            case 2:
                return whichByDescription(language);
            case 3:
                return howManyEpisodes(language);
            default:
                return whichByImage(language);

        }
    }

    @ApiOperation(value = "Gets a mcq : [Image] What is this tv show?")
    @GetMapping(value = "/which-by-image", produces = { "application/json" })
    public ResponseEntity<?> whichByImage(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = Language.languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        ArrayList<TvShowInfos> tvList = new ArrayList<TvShowInfos>();
        try {
            TvShowTmdbFetchOptions answerOptions = new TvShowTmdbFetchOptions(true, true, false, false, false);
            TvShowTmdbFetchOptions similaryOptions = new TvShowTmdbFetchOptions(true, false, false, false, false);
            tvList = TvShowTmdbFetching.getRandomCoherentTvShows(internLanguage.getTmdbLanguage(), NB_CHOICES_IN_MCQ,
                    answerOptions,
                    similaryOptions);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        TvShowInfos answer = tvList.get(0);
        Collections.shuffle(tvList);
        String[] choices = { tvList.get(0).name, tvList.get(1).name, tvList.get(2).name,
                tvList.get(3).name };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(answer.backdrop_path, "",
                TvShowQuestion.WHICH_BY_IMAGE.getQuestion(internLanguage),
                choicesObject,
                answer.name);

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }

    @ApiOperation(value = "Gets a mcq : [Description] Which tv show fits this description?")
    @GetMapping(value = "/which-by-description", produces = { "application/json" })
    public ResponseEntity<?> whichByDescription(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = Language.languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        ArrayList<TvShowInfos> tvList = new ArrayList<TvShowInfos>();
        try {
            TvShowTmdbFetchOptions answerOptions = new TvShowTmdbFetchOptions(true, false, true, false, false);
            TvShowTmdbFetchOptions similaryOptions = new TvShowTmdbFetchOptions(true, false, false, false, false);
            tvList = TvShowTmdbFetching.getRandomCoherentTvShows(internLanguage.getTmdbLanguage(), NB_CHOICES_IN_MCQ,
                    answerOptions,
                    similaryOptions);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        TvShowInfos answer = tvList.get(0);
        Collections.shuffle(tvList);
        String[] choices = { tvList.get(0).name, tvList.get(1).name, tvList.get(2).name,
                tvList.get(3).name };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion("", answer.overview,
                TvShowQuestion.WHICH_BY_DESCRIPTION.getQuestion(internLanguage),
                choicesObject,
                answer.name);

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }

    @ApiOperation(value = "Gets a mcq : How many episodes does this tv show have?")
    @GetMapping(value = "/how-many-episodes", produces = { "application/json" })
    public ResponseEntity<?> howManyEpisodes(
            @RequestParam(required = false, value = "language", defaultValue = "fr") String language) {

        Language internLanguage;
        try {
            internLanguage = Language.languageCheck(language);
        } catch (LanguageNotSupportedException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        ArrayList<TvShowInfos> tvList = new ArrayList<TvShowInfos>();
        try {
            TvShowTmdbFetchOptions answerOptions = new TvShowTmdbFetchOptions(true, true, false, true, false);
            TvShowTmdbFetchOptions similaryOptions = new TvShowTmdbFetchOptions(false, false, false, true, false);
            tvList = TvShowTmdbFetching.getRandomCoherentTvShows(internLanguage.getTmdbLanguage(), NB_CHOICES_IN_MCQ,
                    answerOptions,
                    similaryOptions);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        TvShowInfos answer = tvList.get(0);
        Collections.shuffle(tvList);
        String[] choices = { Integer.toString(tvList.get(0).number_of_episodes),
                Integer.toString(tvList.get(1).number_of_episodes),
                Integer.toString(tvList.get(2).number_of_episodes),
                Integer.toString(tvList.get(3).number_of_episodes) };
        Choices choicesObject = new Choices(choices[0], choices[1], choices[2], choices[3]);
        MCQQuestion mcq = new MCQQuestion(answer.backdrop_path, answer.name,
                TvShowQuestion.HOW_MANY_EPISODES.getQuestion(internLanguage),
                choicesObject,
                Integer.toString(answer.number_of_episodes));

        return new ResponseEntity<MCQQuestion>(mcq, HttpStatus.OK);
    }
}