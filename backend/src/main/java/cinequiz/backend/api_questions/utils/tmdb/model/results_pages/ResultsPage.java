package cinequiz.backend.api_questions.utils.tmdb.model.results_pages;

import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultsPage<T> implements Iterable<T> {

    private List<T> results;

    @JsonProperty("page")
    private int page;

    @JsonProperty("total_pages")
    private int totalPages;

    @JsonProperty("total_results")
    private int totalResults;

    @Override
    public Iterator<T> iterator() {
        return results.iterator();
    }

    public List<T> getResults() {
        return results;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
