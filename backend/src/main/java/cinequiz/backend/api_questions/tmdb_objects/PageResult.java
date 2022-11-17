package cinequiz.backend.api_questions.tmdb_objects;

import java.util.ArrayList;

public class PageResult {
    public int page;
    public ArrayList<MovieListResult> results;
    public int total_pages;
    public int total_results;
}
