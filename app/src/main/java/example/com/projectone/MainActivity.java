package example.com.projectone;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import example.com.projectone.Adapters.GridViewAdapter;
import example.com.projectone.models.MovieResponse;
import example.com.projectone.models.Result;
import example.com.projectone.services.MovieService;
import example.com.projectone.util.JSONUtil;
import example.com.projectone.util.KEY_EXTRA;
import example.com.projectone.util.NetworkServiceBuilder;
import example.com.projectone.util.NetworkUtil;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Abhinav Ravi on 22/11/16.
 */

public class MainActivity extends AppCompatActivity {
    private final String ID = "id";
    private final String TITLE = "title";
    private final String RELEASE_DATE = "release_date";
    private final String MOVIE_POSTER = "poster_path";
    private final String VOTE_AVERAGE = "vote_average";
    private static final String VOTE_COUNT = "vote_count";
    private final String OVERVIEW = "overview";
    private static String REQUEST_MOVIE_BY;
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String FAVOURITES = "favourites";
    private static JSONUtil jsonUtil = new JSONUtil();
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private List<String> moviePosters = new ArrayList<>();
    private static final String POSTER_URL = "http://image.tmdb.org/t/p/w185/";
    private List<Result> resultObjectList = new ArrayList<>();
    private Toolbar toolbar;
    private GridView gridView;

    private TextView errorTextView;
    private ImageView errorImageview;
    private NetworkUtil networkUtil;

    private Realm realm;

    //variable to save the instance
    private String savedRequestType = "savedRequestType";
    private String savedGridViewPosition = "savedGridViewPosition";




    private MovieResponse mMovieResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gridView = (GridView) findViewById(R.id.gridView);
        REQUEST_MOVIE_BY = POPULAR;


        errorImageview = (ImageView) findViewById(R.id.errimg);
        errorTextView = (TextView) findViewById(R.id.errtext);

        //get the realm DB file.
        realm = Realm.getInstance(new RealmConfiguration.Builder()
                .name("favouriteMovies.realm")
                .deleteRealmIfMigrationNeeded()
                .build());


        if (savedInstanceState != null) {
            //debug whether app state is being restored.
            //Toast.makeText(this.getApplicationContext(), "REQUEST_MOVIE_BY" + REQUEST_MOVIE_BY, Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, "REQUEST_MOVIE_BY" + REQUEST_MOVIE_BY);
        }
        networkUtil = new NetworkUtil(this);
        if (!networkUtil.isInternetWorking()) {
            showErrorMessageDialog();
        }
        fetchMovie(REQUEST_MOVIE_BY);

    }

    // Save the request_by and grid position, helpful while recreating the activity during app rotation etc.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(savedRequestType, REQUEST_MOVIE_BY);
        int position = gridView.getFirstVisiblePosition();
        outState.putInt(savedGridViewPosition, position);
    }

    // Restore the request_by and grid position.
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        REQUEST_MOVIE_BY = savedInstanceState.getString(savedRequestType);
        gridView.setSelection(savedInstanceState.getInt(savedGridViewPosition));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String present_request_type = REQUEST_MOVIE_BY;
        switch (item.getItemId()) {
            case R.id.POPULAR:
                REQUEST_MOVIE_BY = POPULAR;
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                if (present_request_type != REQUEST_MOVIE_BY) fetchMovie(REQUEST_MOVIE_BY);

                return true;
            case R.id.TOP_RATED:
                REQUEST_MOVIE_BY = TOP_RATED;
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                if (present_request_type != REQUEST_MOVIE_BY) fetchMovie(REQUEST_MOVIE_BY);

                return true;

            case R.id.FAVOURITES:
                REQUEST_MOVIE_BY = FAVOURITES;
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                if (present_request_type != REQUEST_MOVIE_BY) fetchMovie(REQUEST_MOVIE_BY);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchMovie(REQUEST_MOVIE_BY);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fetchMovie(REQUEST_MOVIE_BY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


    private void setMoviePosters(String request_movie_by){

        try {

            resultObjectList = mMovieResponse.getResults();
            Log.d(LOG_TAG, "resultObjectList : " + resultObjectList);
            moviePosters.clear(); // flush previously stored posters.


            for (Result result : resultObjectList){
                moviePosters.add(POSTER_URL + result.getPoster_path());
            }

            Log.d(LOG_TAG, moviePosters.toString());
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
            Toast.makeText(this,"Error while loading movie posters result",Toast.LENGTH_SHORT).show();
        }

    }

    private List<Result> getSavedMovies() {
        RealmResults<Result> results = realm.where(Result.class).findAll();
        List<Result> movieResults = new ArrayList<>();
        moviePosters.clear();
        for (Result result : results) {
            moviePosters.add(POSTER_URL + result.getPoster_path());
            movieResults.add(realm.copyFromRealm(result));
        }

        return movieResults;
    }

    private void updateView(String request_movie_by) {
        REQUEST_MOVIE_BY = request_movie_by;
        toolbar.setTitle(REQUEST_MOVIE_BY == POPULAR ? "Popular Movies" : (REQUEST_MOVIE_BY == TOP_RATED) ? "TOP_RATED" : "FAVOURITES");

        networkUtil = new NetworkUtil(this);
        if (!networkUtil.isInternetWorking()) {
            showErrorMessageDialog();
            gridView.setAdapter(new GridViewAdapter(this, new ArrayList<String>()));
        }

        if (request_movie_by.equals(FAVOURITES)) {
            final List<Result> results = getSavedMovies();
            errorImageview.setVisibility(View.INVISIBLE);
            if(results.isEmpty()){
                errorTextView.setVisibility(View.VISIBLE);
                errorTextView.setText("No Favourites movie !");
            }
            gridView.setAdapter(new GridViewAdapter(this, moviePosters));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    try {

                        Result object = results.get(position);

                        Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);

                        intent.putExtra(KEY_EXTRA.Result.getDescription(), object);
                        intent.putExtra(KEY_EXTRA.IS_FAV.getDescription(), true);
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } else {

            try {
                Log.d(LOG_TAG, "mMovieResponse in update is: " + this.mMovieResponse);
                Log.d(LOG_TAG, "moviePosters in update is: " + this.moviePosters);
                    // Instance of GridViewAdapter Class
                    gridView.setAdapter(new GridViewAdapter(this, moviePosters));
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            try {
                                Result result = resultObjectList.get(position);
//                                Result result = new Result();
//                                result.setId(object.getString(ID));
//                                result.setTitle(object.getString(TITLE));
//                                result.setOverview(object.getString(OVERVIEW));
//                                result.setPoster_path(object.getString(MOVIE_POSTER));
//                                result.setVote_average(object.getString(VOTE_AVERAGE));
//                                result.setVote_count(object.getString(VOTE_COUNT));
//                                result.setRelease_date(object.getString(RELEASE_DATE));

                                Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);

                                intent.putExtra(KEY_EXTRA.Result.getDescription(), result);
                                intent.putExtra(KEY_EXTRA.IS_FAV.getDescription(), false);
                                startActivity(intent);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
//                }else {
//                    Toast.makeText(this,"Not able to fetchmovie",Toast.LENGTH_SHORT).show();
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // Note : Since retrofit rest calls are executed asynchronously, hence mMovieResponse  noght not be available just after calling fetchMovie.
    // Better is to return MovieResponse from fetchMovie once network call is successful to getMoviePoster fn and use it there.
    private MovieResponse fetchMovie(String request_movie_by) {
        hideErrorMessageDialog();

        REQUEST_MOVIE_BY = request_movie_by;
        if (request_movie_by.equals(FAVOURITES)) {
            updateView(REQUEST_MOVIE_BY);
        }
        Log.d(LOG_TAG, "Calling fetchMovie");
        MovieService movieService = NetworkServiceBuilder.createService(MovieService.class);
        movieService.fetchMovies(REQUEST_MOVIE_BY, "24c92c0158254535df5f48cf5f8b6db2",  new Callback<MovieResponse>() {
                @Override
                public void success(MovieResponse movieResponse, Response response) {
                    mMovieResponse = movieResponse;
                    Log.d(LOG_TAG, "Called url: " + response.getUrl());
                    Log.d(LOG_TAG, "mMovieResponse in callback is: " + mMovieResponse);
                    setMovieResponse(mMovieResponse);
                    setMoviePosters(REQUEST_MOVIE_BY);
                    updateView(REQUEST_MOVIE_BY);
                }

                @Override
                public void failure(RetrofitError error) {
                    showErrorMessageDialog();
                    Log.d(LOG_TAG, "Error occured in fetch movie: " + error);
                }
            });
        return mMovieResponse;
    }

    private void showErrorMessageDialog() {
        errorImageview.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void hideErrorMessageDialog() {
        errorImageview.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.INVISIBLE);
        errorTextView.setText("Sorry!Network Error! check back Later");
    }

    private void setMovieResponse(MovieResponse mMovieResponse){
        this.mMovieResponse = mMovieResponse;
        Log.d(LOG_TAG, "mMovieResponse in setter is: " + this.mMovieResponse);
    }

}
