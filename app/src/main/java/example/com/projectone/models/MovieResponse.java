package example.com.projectone.models;

/**
 * Created by Abhinav Ravi on 13/10/16.
 */



import java.io.Serializable;
import java.util.List;


// Used org.jsonschema2pojo to generate this class from the JSON response of api.
public class MovieResponse implements Serializable{

    private String page;
    private List<Result> results;
    private String total_results;
    private String total_pages;

    /**
     * @return The page
     */
    public String getPage() {
        return page;
    }

    /**
     * @param page The page
     */
    public void setPage(String page) {
        this.page = page;
    }

    /**
     * @return The results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

    /**
     * @return The total_results
     */
    public String getTotal_results() {
        return total_results;
    }

    /**
     * @param total_results The total_results
     */
    public void setTotal_results(String total_results) {
        this.total_results = total_results;
    }

    /**
     * @return The total_pages
     */
    public String getTotal_pages() {
        return total_pages;
    }

    /**
     * @param total_pages The total_pages
     */
    public void setTotal_pages(String total_pages) {
        this.total_pages = total_pages;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "page=" + page +
                ", results=" + results +
                ", total_results=" + total_results +
                ", total_pages=" + total_pages +
                '}';
    }
}
