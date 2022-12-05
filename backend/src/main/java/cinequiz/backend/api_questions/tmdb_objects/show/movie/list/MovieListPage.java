package cinequiz.backend.api_questions.tmdb_objects.show.movie.list;

import java.util.ArrayList;

public class MovieListPage {
    public int page;
    public ArrayList<MovieResult> results;
    public int total_pages;
    public int total_results;
}
