package example.com.projectone;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import example.com.projectone.models.Result;
import example.com.projectone.util.JSONUtil;
import example.com.projectone.util.KEY_EXTRA;
import example.com.projectone.util.NetworkUtil;

/**
 * Created by Abhinav Ravi on 22/11/16.
 */

public class MainActivity extends AppCompatActivity {
    private final String TITLE = "title";
    private final String RELEASE_DATE = "release_date";
    private final String MOVIE_POSTER = "poster_path";
    private final String VOTE_AVERAGE = "vote_average";
    private static final String VOTE_COUNT = "vote_count";
    private final String OVERVIEW = "overview";
    private static String REQUEST_MOVIE_BY;
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static JSONUtil jsonUtil = new JSONUtil();
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private List<String> moviePosters = new ArrayList<>();
    private static final String POSTER_URL = "http://image.tmdb.org/t/p/w185/";
    private List<JSONObject> resultObjectList = new ArrayList<>();
    private Toolbar toolbar;
    private GridView gridView;

    //variable to save the instance
    private String savedRequestType;
    private String savedGridViewPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gridView = (GridView) findViewById(R.id.gridView);
        REQUEST_MOVIE_BY = POPULAR;
        if(savedInstanceState != null){
            REQUEST_MOVIE_BY= (String) savedInstanceState.get(savedRequestType);
            gridView.setSelection((Integer) savedInstanceState.get(savedGridViewPosition));
        }
        NetworkUtil networkUtil = new NetworkUtil(this);
        if(!networkUtil.isInternetWorking()){
            showErrorMessageDialog();
        }
        updateView(REQUEST_MOVIE_BY);

    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString(savedRequestType, REQUEST_MOVIE_BY);
        int position = gridView.getFirstVisiblePosition();
        outState.putInt(savedGridViewPosition, position);
    }

//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        // Always call the superclass so it can restore the view hierarchy
//        super.onRestoreInstanceState(savedInstanceState);
//
//        // Restore state members from saved instance
//        REQUEST_MOVIE_BY= savedInstanceState.get(savedRequestType);
//    }

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
                if (present_request_type != REQUEST_MOVIE_BY) updateView(REQUEST_MOVIE_BY);

                return true;
            case R.id.TOP_RATED:
                REQUEST_MOVIE_BY = TOP_RATED;
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                if (present_request_type != REQUEST_MOVIE_BY) updateView(REQUEST_MOVIE_BY);

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
        updateView(REQUEST_MOVIE_BY);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateView(REQUEST_MOVIE_BY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private List<String> getMoviePosters(String request_movie_by) throws Exception {
        NetworkTaskActivity networkTaskActivity = new NetworkTaskActivity();
        try {
            String movieResponse = networkTaskActivity.execute(request_movie_by).get();
            //Toast.makeText(getApplicationContext(),movieResponse,Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "movieResponse : " + movieResponse);
            if (movieResponse != null) {
                String JSONEntry = "results";
                resultObjectList = jsonUtil.getEntryFromJson(movieResponse, JSONEntry);
                Log.d(LOG_TAG, "resultObjectList : " + resultObjectList);
                moviePosters.clear(); // flush previously stored posters.
                for (JSONObject jsonObject : resultObjectList) {
                    moviePosters.add(POSTER_URL + jsonObject.getString("poster_path"));
                }
                Log.d(LOG_TAG, moviePosters.toString());
                //Toast.makeText(getApplicationContext(),"hi"+moviePosters.toString(),Toast.LENGTH_SHORT).show();
                return moviePosters;
            } else
                return new ArrayList<>();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
            throw new Exception("Error while loading movie posters result");
        }

    }

    private void updateView(String request_movie_by) {
        REQUEST_MOVIE_BY = request_movie_by;
        toolbar.setTitle(REQUEST_MOVIE_BY == POPULAR ? "Popular Movies" : "Top Rated Movies");
        try {
            if (!getMoviePosters(request_movie_by).isEmpty()) {

                // Instance of GridViewAdapter Class
                gridView.setAdapter(new GridViewAdapter(this, moviePosters));
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        try {
                            JSONObject object = resultObjectList.get(position);
                            Result result = new Result();
                            result.setTitle(object.getString(TITLE));
                            result.setOverview(object.getString(OVERVIEW));
                            result.setPosterPath(object.getString(MOVIE_POSTER));
                            result.setReleaseDate(object.getString(RELEASE_DATE));
                            result.setVoteAverage(object.getDouble(VOTE_AVERAGE));
                            result.setVoteCount(object.getInt(VOTE_COUNT));
                            result.setReleaseDate(object.getString(RELEASE_DATE));

                            Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);

                            intent.putExtra(KEY_EXTRA.Result.getDescription(), result);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showErrorMessageDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Connectivity");
        alertDialog.setMessage("Failed to connect to internet.");
        alertDialog.setButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.setIcon(android.R.drawable.stat_notify_error);
        alertDialog.show();
    }

}
