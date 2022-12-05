package cinequiz.backend.api_questions.tmdb_objects.show.tv_show.list;

import java.util.ArrayList;

public class TvShowPage {
    public int page;
    public ArrayList<TvShowResult> results;
    public int total_pages;
    public int total_results;
}
