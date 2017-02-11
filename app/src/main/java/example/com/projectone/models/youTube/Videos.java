package example.com.projectone.models.youTube;

import java.util.List;

/**
 * Created by Abhinav Ravi on 12/02/17.
 */
public class Videos {
    private Integer id;
    private VideosResults[] results;

    public VideosResults[] getResults() {
        return results;
    }

    public void setResults(VideosResults[] results) {
        this.results = results;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Videos{" +
                "id=" + id +
                ", videosResultses=" + results +
                '}';
    }
}
