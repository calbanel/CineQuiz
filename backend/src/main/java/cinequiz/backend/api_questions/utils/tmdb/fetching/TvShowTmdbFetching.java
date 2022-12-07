package cinequiz.backend.api_questions.utils.tmdb.fetching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import cinequiz.backend.BackendApplication;
import cinequiz.backend.api_questions.exceptions.CastUnavailableInTMDBException;
import cinequiz.backend.api_questions.exceptions.NotEnoughPeoplesInCast;
import cinequiz.backend.api_questions.exceptions.NotEnoughSimilarShowsInTMDBException;
import cinequiz.backend.api_questions.exceptions.NotaValidShowException;
import cinequiz.backend.api_questions.utils.tmdb.fetching.options.PeopleTmdbFetchOptions;
import cinequiz.backend.api_questions.utils.tmdb.fetching.options.TvShowTmdbFetchOptions;
import cinequiz.backend.api_questions.utils.tmdb.objects.people.credit.ShowCredit;
import cinequiz.backend.api_questions.utils.tmdb.objects.people.credit.ShowCreditPage;
import cinequiz.backend.api_questions.utils.tmdb.objects.show.cast.Cast;
import cinequiz.backend.api_questions.utils.tmdb.objects.show.cast.Crew;
import cinequiz.backend.api_questions.utils.tmdb.objects.show.cast.CastMember;
import cinequiz.backend.api_questions.utils.tmdb.objects.show.cast.CastPage;
import cinequiz.backend.api_questions.utils.tmdb.objects.show.tv_show.TvShowInfos;
import cinequiz.backend.api_questions.utils.tmdb.objects.show.tv_show.list.TvShowPage;
import cinequiz.backend.api_questions.utils.tmdb.objects.show.tv_show.list.TvShowResult;

public class TvShowTmdbFetching {
    public static ArrayList<TvShowInfos> getRandomCoherentTvShows(String tmdbLanguage, int number,
            TvShowTmdbFetchOptions answerOptions, TvShowTmdbFetchOptions similaryOptions) {
        ArrayList<TvShowInfos> tvList = new ArrayList<TvShowInfos>();

        ArrayList<TvShowInfos> similarTvShowList = null;
        TvShowInfos tvShow = null;

        // try to find a tvShow with an similar tvShow list valid in tmdb
        while (similarTvShowList == null) {

            tvShow = getOneRandomValidTvShow(tmdbLanguage, answerOptions);

            try {
                similarTvShowList = getSimilarValidTvShows(tvShow, number - 1, tmdbLanguage, similaryOptions);
            } catch (NotEnoughSimilarShowsInTMDBException e) {
                System.err.println(e.getMessage());
                tvList.clear();
            }
        }

        // original tv show at index 0
        tvList.add(tvShow);
        tvList.addAll(similarTvShowList);

        return tvList;
    }

    private static TvShowInfos getOneRandomValidTvShow(String tmdbLanguage, TvShowTmdbFetchOptions options) {
        TvShowInfos tvShow = null;

        // as long as we don't have a valid tv show, we go through the pages of popular
        // tv show randomly
        while (tvShow == null) {
            TvShowPage page = getRandomPopularTvShowPage(tmdbLanguage);

            // remove unvalid tv show of the popular tv show page
            ArrayList<TvShowResult> filtredResults = getFiltredTvShowResultListInPage(page, options);
            Collections.shuffle(filtredResults);

            for (TvShowResult result : filtredResults) {
                try {
                    // if the tv show is valid we stop the research
                    tvShow = getTvShowIfValid(result, tmdbLanguage, options);
                    break;
                } catch (NotaValidShowException e) {
                    System.err.println(e.getMessage());
                }

            }
        }
        return tvShow;
    }

    private static final int RANDOM_PAGE_MIN = 1;
    private static final int RANDOM_PAGE_MAX = 100; // I want one of the 100 first pages (the 2000 actual most popular
                                                    // tv shows)

    private static TvShowPage getRandomPopularTvShowPage(String tmdbLanguage) {
        TvShowPage page = null;
        RestTemplate rt = new RestTemplate();

        // as long as we don't have a valid page, we go through the pages of popular
        // randomly
        while (page == null) {
            int randomPage = BackendApplication.random(RANDOM_PAGE_MIN, RANDOM_PAGE_MAX);
            String url = "https://api.themoviedb.org/3/tv/popular?api_key=" + BackendApplication.API_KEY
                    + "&language="
                    + tmdbLanguage + "&page="
                    + randomPage;

            try {
                page = rt.getForObject(url, TvShowPage.class);
            } catch (final HttpClientErrorException e) {
                System.out.println(e.getStatusCode());
                System.out.println(e.getResponseBodyAsString());
            }

        }
        return page;
    }

    private static ArrayList<TvShowResult> getFiltredTvShowResultListInPage(TvShowPage page,
            TvShowTmdbFetchOptions options) {
        return (ArrayList<TvShowResult>) page.results
                .stream()
                .filter(
                        (m) -> (!options.isTitle() || (m.name != null && m.name != ""))
                                && (!options.isPoster() || (m.poster_path != null && m.poster_path != ""))
                                && (!options.isBackdrop() || (m.backdrop_path != null && m.backdrop_path != ""))
                                && (!options.isDescription() || (m.overview != null && m.overview != "")))
                .collect(Collectors.toList());
    }

    private static TvShowInfos getTvShowIfValid(TvShowResult result, String tmdbLanguage,
            TvShowTmdbFetchOptions options)
            throws NotaValidShowException {
        TvShowInfos tvShow = null;

        // fetch the detailed informations on a tv show
        String url = "https://api.themoviedb.org/3/tv/" + result.id + "?api_key="
                + BackendApplication.API_KEY
                + "&language="
                + tmdbLanguage;

        RestTemplate rt = new RestTemplate();
        TvShowInfos tmp = null;
        try {
            tmp = rt.getForObject(url, TvShowInfos.class);
        } catch (final HttpClientErrorException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
        }

        // check if it's a valid tv show according to the target values
        if (tmp != null && (!options.isNb_episodes() || tmp.number_of_episodes > 0)
                && (!options.isReleaseDate() || (tmp.first_air_date != null && tmp.first_air_date != "")))
            tvShow = tmp;

        // throw exception if we failed to fetch the tv show informations or if the
        // target values aren't available
        if (tvShow == null)
            throw new NotaValidShowException();

        return tvShow;
    }

    private static ArrayList<TvShowInfos> getSimilarValidTvShows(TvShowInfos tvShow, int number, String tmdbLanguage,
            TvShowTmdbFetchOptions options) throws NotEnoughSimilarShowsInTMDBException {
        ArrayList<TvShowInfos> tvShowList = new ArrayList<TvShowInfos>();

        // it can have several pages for similar tv shows in tmdb, we start at the first
        // page
        int page_number = 1;
        // we need to fill the list
        while (tvShowList.size() < number) {

            // try to get the similar tv show page
            TvShowPage page = getSimilarTvShowsPage(tvShow.id, tmdbLanguage, page_number);
            // we failed to get a similar tv show page then we throw an exception
            if (page == null)
                throw new NotEnoughSimilarShowsInTMDBException();

            // only keeps the tv shows of the same language
            ArrayList<TvShowResult> filtredResults = (ArrayList<TvShowResult>) page.results.stream()
                    .filter(m -> m.original_language == tvShow.original_language).collect(Collectors.toList());

            // remove unvalid similar tv shows
            filtredResults = getFiltredTvShowResultListInPage(page, options);

            Collections.shuffle(filtredResults);

            // browse valid similar tv shows
            for (TvShowResult result : filtredResults) {
                TvShowInfos similar = null;
                try {
                    // check the tv show and get all of his infos
                    similar = getTvShowIfValid(result, tmdbLanguage, options);
                } catch (NotaValidShowException e) {
                    System.err.println(e.getMessage());
                }

                // if similar isn't valid or if the target value is an duplicata we go to the
                // next value
                if (similar == null || checkDuplicate(similar, tvShow, tvShowList, options))
                    continue;

                tvShowList.add(similar);

                // if we have enough tv shows we stop browsing the page.
                if (tvShowList.size() >= number)
                    break;
            }

            // go to the next similar tv show page
            page_number++;
        }

        return tvShowList;
    }

    private static TvShowPage getSimilarTvShowsPage(int tvShowId, String tmdbLanguage, int num_page) {
        TvShowPage page = null;
        RestTemplate rt = new RestTemplate();
        String url = "https://api.themoviedb.org/3/tv/" + tvShowId + "/similar?api_key=" + BackendApplication.API_KEY
                + "&language=" + tmdbLanguage + "&page=" + num_page;
        try {
            page = rt.getForObject(url, TvShowPage.class);
        } catch (final HttpClientErrorException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
        }

        // null if the target similar tv show page isn't valid
        return page;
    }

    private static boolean checkDuplicate(TvShowInfos result, TvShowInfos original, ArrayList<TvShowInfos> similarList,
            TvShowTmdbFetchOptions options) {
        boolean isDuplicate = false;

        if (options.isTitle()) {
            if (result.name == original.name)
                isDuplicate = true;

            for (TvShowInfos similar : similarList) {
                if (result.name == similar.name)
                    isDuplicate = true;
            }
        }

        if (options.isNb_episodes()) {
            if (result.number_of_episodes == original.number_of_episodes)
                isDuplicate = true;

            for (TvShowInfos similar : similarList) {
                if (result.number_of_episodes == similar.number_of_episodes)
                    isDuplicate = true;
            }
        }

        if (options.isReleaseDate() && !isDuplicate) {
            if (result.first_air_date == original.first_air_date)
                isDuplicate = true;

            for (TvShowInfos similar : similarList) {
                if (result.first_air_date == similar.first_air_date)
                    isDuplicate = true;
            }
        }

        return isDuplicate;
    }

    public static ArrayList<CastMember> getRandomCoherentPeopleListInTheseTvShows(int tvShowId,
            int numberOfPeoplesInTvShow,
            int similarTvShowId, int numberOfPeoplesInSimilarTvShow, String tmdbLanguage) {
        ArrayList<CastMember> cast = null;
        int randomGender = BackendApplication.random(1, 2);
        try {
            PeopleTmdbFetchOptions panswerOptions = new PeopleTmdbFetchOptions(true, true, true);
            ArrayList<CastMember> answer = getRandomCoherentPeoplesInvolvedInThisTvShow(tvShowId,
                    tmdbLanguage, numberOfPeoplesInTvShow, panswerOptions, randomGender, -1);
            PeopleTmdbFetchOptions psimilaryOptions = new PeopleTmdbFetchOptions(true, false, true);

            ArrayList<CastMember> similaryCast = getRandomCoherentPeoplesInvolvedInThisTvShow(similarTvShowId,
                    tmdbLanguage, numberOfPeoplesInSimilarTvShow, psimilaryOptions, randomGender,
                    tvShowId);
            cast = new ArrayList<CastMember>();
            cast.addAll(answer);
            cast.addAll(similaryCast);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // null if we failed to get a valid cast for these tv shows
        return cast;
    }

    private static ArrayList<CastMember> getRandomCoherentPeoplesInvolvedInThisTvShow(int tvShowId,
            String tmdbLanguage,
            int number, PeopleTmdbFetchOptions options, int tmdbgenre, int similarTvShowId)
            throws CastUnavailableInTMDBException, NotEnoughPeoplesInCast {
        ArrayList<CastMember> peoples = new ArrayList<CastMember>();

        CastPage castPage = getTvShowCastPage(tvShowId, tmdbLanguage);
        // if target cast page isn't valid, throw exception
        if (castPage == null)
            throw new CastUnavailableInTMDBException();

        // only keeps peoples where we have the target values
        ArrayList<CastMember> castFiltered = getFiltredCastListInPage(castPage, options, tmdbgenre);

        // we firt want the most popular casts, we want known names
        castFiltered.sort((a, b) -> new Comparator<CastMember>() {
            @Override
            public int compare(CastMember o1, CastMember o2) {
                if (o1.popularity == o2.popularity)
                    return 0;

                return o1.popularity < o2.popularity ? 1 : -1;
            }

        }.compare(a, b));

        // browse the clean cast list
        for (CastMember c : castFiltered) {
            // add cast to the final list if he isn't in the similar tv shows
            if (!isCastIsInThisTvShow(c.id, similarTvShowId, tmdbLanguage))
                peoples.add(c);

            // stop the research when we have the number of peoples we want
            if (peoples.size() >= number)
                break;
        }

        // if there not enough peoples in the final list, throw exception
        if (peoples.size() < number)
            throw new NotEnoughPeoplesInCast();

        return peoples;
    }

    private static CastPage getTvShowCastPage(int tvShowId, String tmdbLanguage) {
        CastPage page = null;
        RestTemplate rt = new RestTemplate();
        String url = "https://api.themoviedb.org/3/tv/" + tvShowId + "/credits?api_key=" + BackendApplication.API_KEY
                + "&language=" + tmdbLanguage;
        try {
            page = rt.getForObject(url, CastPage.class);
        } catch (final HttpClientErrorException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
        }

        // null if the target cast page isn't valid
        return page;
    }

    private static ArrayList<CastMember> getFiltredCastListInPage(CastPage page,
            PeopleTmdbFetchOptions options, int tmdbgenre) {
        ArrayList<Cast> cast = (ArrayList<Cast>) page.cast.stream()
                .filter((c) -> (!options.isProfile_path() || (c.profile_path != null && c.profile_path != ""))
                        && (!options.isName() || (c.name != null && c.name != ""))
                        && (!options.isGender() || c.gender == tmdbgenre))
                .collect(Collectors.toList());
        ArrayList<Crew> crew = (ArrayList<Crew>) page.crew.stream()
                .filter((c) -> (!options.isProfile_path() || (c.profile_path != null && c.profile_path != ""))
                        && (!options.isName() || (c.name != null && c.name != ""))
                        && (!options.isGender() || c.gender == tmdbgenre))
                .collect(Collectors.toList());
        ArrayList<CastMember> members = new ArrayList<CastMember>();
        members.addAll(cast);
        members.addAll(crew);
        return members;
    }

    private static boolean isCastIsInThisTvShow(int personId, int tvShowId, String tmdbLanguage) {
        boolean isIn = false;

        // get all the tv shows have participated the person
        ShowCreditPage creditPage = getPeopleTvShowCreditPage(personId, tmdbLanguage);
        if (creditPage != null) {
            ArrayList<ShowCredit> list = new ArrayList<ShowCredit>();
            list.addAll(creditPage.cast);
            list.addAll(creditPage.crew);
            list = (ArrayList<ShowCredit>) list.stream().filter(m -> m.id == tvShowId || m.media_type == "tv")
                    .collect(Collectors.toList());
            if (list.size() > 0)
                isIn = true;
        }

        return isIn;
    }

    private static ShowCreditPage getPeopleTvShowCreditPage(int personId, String tmdbLanguage) {
        ShowCreditPage page = null;
        RestTemplate rt = new RestTemplate();
        String url = "https://api.themoviedb.org/3/person/" + personId + "/combined_credits?api_key="
                + BackendApplication.API_KEY
                + "&language=" + tmdbLanguage;
        try {
            page = rt.getForObject(url, ShowCreditPage.class);
        } catch (final HttpClientErrorException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
        }
        // null if the target cast page isn't valid
        return page;
    }

}
