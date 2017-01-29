package example.com.projectone.models;

/**
 * Created by Abhinav Ravi on 13/10/16.
 */

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


// Used org.jsonschema2pojo to generate this class from the JSON response of api.
public class Result extends RealmObject implements Serializable {

    @PrimaryKey
    private String id;
    private String poster_path;
    private String adult;
    private String overview;
    private String release_date;
   // private List<Integer> genreIds = new ArrayList<Integer>();

    private String original_title;
    private String original_language;
    private String title;
    private String backdrop_path;
    private String popularity;
    private String vote_count;
    private String video;
    private String vote_average;

    public Result(){

    }

    public Result(Context context, String posterPath, String adult, String overview, String releaseDate, List<Integer> genreIds, String id, String originalTitle, String originalLanguage, String title, String backdropPath, String popularity, String voteCount, String video, String voteAverage) {

        this.poster_path = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.release_date = releaseDate;
        //this.genreIds = genreIds;
        this.id = id;
        this.original_title = originalTitle;
        this.original_language = originalLanguage;
        this.title = title;
        this.backdrop_path = backdropPath;
        this.popularity = popularity;
        this.vote_count = voteCount;
        this.video = video;
        this.vote_average = voteAverage;
    }

    /**
     * @return The poster_path
     */
    public String getPoster_path() {
        return poster_path;
    }

    /**
     * @param poster_path The poster_path
     */
    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    /**
     * @return The adult
     */
    public String getAdult() {
        return adult;
    }

    /**
     * @param adult The adult
     */
    public void setAdult(String adult) {
        this.adult = adult;
    }

    /**
     * @return The overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     * @param overview The overview
     */
    public void setOverview(String overview) {
        this.overview = overview;
    }

    /**
     * @return The release_date
     */
    public String getRelease_date() {
        return release_date;
    }

    /**
     * @param release_date The release_date
     */
    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    /**
     * @return The genreIds
     */
//    public List<Integer> getGenreIds() {
//        return genreIds;
//    }

    /**
     * @param genreIds The genre_ids
     */
//    public void setGenreIds(List<Integer> genreIds) {
//        this.genreIds = genreIds;
//    }

    /**
     * @return The id
     */
    public String getResultId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The original_title
     */
    public String getOriginal_title() {
        return original_title;
    }

    /**
     * @param original_title The original_title
     */
    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    /**
     * @return The original_language
     */
    public String getOriginal_language() {
        return original_language;
    }

    /**
     * @param original_language The original_language
     */
    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The backdrop_path
     */
    public String getBackdrop_path() {
        return backdrop_path;
    }

    /**
     * @param backdrop_path The backdrop_path
     */
    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    /**
     * @return The popularity
     */
    public String getPopularity() {
        return popularity;
    }

    /**
     * @param popularity The popularity
     */
    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    /**
     * @return The vote_count
     */
    public String getVote_count() {
        return vote_count;
    }

    /**
     * @param vote_count The vote_count
     */
    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    /**
     * @return The video
     */
    public String getVideo() {
        return video;
    }

    /**
     * @param video The video
     */
    public void setVideo(String video) {
        this.video = video;
    }

    /**
     * @return The vote_average
     */
    public String getVote_average() {
        return vote_average;
    }

    /**
     * @param vote_average The vote_average
     */
    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    @Override
    public String toString() {
        return "Result{" +
                "poster_path='" + poster_path + '\'' +
                ", adult=" + adult +
                ", overview='" + overview + '\'' +
                ", release_date='" + release_date + '\'' +
                //", genreIds=" + genreIds +
                ", id=" + id +
                ", original_title='" + original_title + '\'' +
                ", original_language='" + original_language + '\'' +
                ", title='" + title + '\'' +
                ", backdrop_path='" + backdrop_path + '\'' +
                ", popularity=" + popularity +
                ", vote_count=" + vote_count +
                ", video=" + video +
                ", vote_average=" + vote_average +
                '}';
    }
}

