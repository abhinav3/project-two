package example.com.projectone.services;

import example.com.projectone.models.MovieResponse;
import example.com.projectone.models.Reviews;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Abhinav Ravi on 26/01/17.
 */
public interface MovieService {

    //this method is to fetch the ALL movies with specific request type
    @GET("/movie/{request_movie_by}")
    void fetchMovies(
            @Path("request_movie_by") String request_movie_by,
            @Query("api_key") String mApiKey,
            Callback<MovieResponse> cb
    );

    @GET("/movie/{id}/reviews")
    void fetchReview(
            @Query("api_key") String mApiKey,
            @Path("id") String id,
            Callback<Reviews> cb
    );

    @GET("/movie/{id}/videos")
    void fetchVideos(
            @Query("api_key") String mApiKey,
            @Path("id") String id,
            Callback<MovieResponse> cb
    );

}
