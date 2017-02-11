package example.com.projectone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import java.util.List;

import example.com.projectone.models.Result;
import example.com.projectone.models.review.ReviewResults;
import example.com.projectone.models.review.Reviews;
import example.com.projectone.models.youTube.Videos;
import example.com.projectone.models.youTube.VideosResults;
import example.com.projectone.services.MovieService;
import example.com.projectone.util.ColorUtil;
import example.com.projectone.util.KEY_EXTRA;
import example.com.projectone.util.NetworkServiceBuilder;
import example.com.projectone.util.NetworkUtil;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    private Realm realm;
    private String POSTER_URL = "http://image.tmdb.org/t/p/w500/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkUtil networkUtil = new NetworkUtil(this);
        if(!networkUtil.isInternetWorking()){
            showErrorMessageDialog();
        }


        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get the realm DB file.
        realm = Realm.getInstance( new RealmConfiguration.Builder()
                .name("favouriteMovies.realm")
                .deleteRealmIfMigrationNeeded()
                .build());

        TextView ratingValue = (TextView) findViewById(R.id.ratingValue);
        TextView reviewValue = (TextView) findViewById(R.id.reviewValue);
        TextView overView = (TextView) findViewById(R.id.overview);
        TextView release_date = (TextView) findViewById(R.id.release_date_value);
        TextView story_review = (TextView) findViewById(R.id.story_review);
        story_review.setMovementMethod(new ScrollingMovementMethod());


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Result result = (Result)getIntent().getSerializableExtra(KEY_EXTRA.Result.getDescription());
        final boolean is_fav = (Boolean)getIntent().getSerializableExtra(KEY_EXTRA.IS_FAV.getDescription());
        this.setTitle(result.getTitle());

        ratingValue.setText(result.getVote_average().toString() + " / 10");
        reviewValue.setText(result.getVote_count().toString());
        overView.setText(result.getOverview());
        release_date.setText(result.getRelease_date());
        getMovieReview(story_review, result.getResultId());
        getMovieTrailers(story_review,result.getResultId());
        ImageView imageView = (ImageView) findViewById(R.id.header);
        Log.d(LOG_TAG,"Poster Path" +result.getBackdrop_path() + " title " + result.getOriginal_title());

        Picasso.with(this)
                .load(POSTER_URL+result.getBackdrop_path())
                .placeholder(new ColorUtil().getRandomDrawbleColor())
                .into(imageView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(is_fav){
            fab.setImageResource(android.R.drawable.ic_menu_delete);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!is_fav){
                    saveToDataBase(result);
                }else {
                    deleteFromDataBase(result.getResultId());
                }
            }
        });
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

    @Override
    protected void onDestroy(){
        super.onDestroy();
        realm.close();

    }

    private void saveToDataBase(final Result result){

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                // Create realm object with the primary key value from java object. Rest values are set from java object into realm object.
                Result row = bgRealm.createObject(Result.class, result.getResultId());
                row.setTitle(result.getTitle());
                row.setOverview(result.getOverview());
                row.setPoster_path(result.getPoster_path());
                row.setVote_average(result.getVote_average());
                row.setVote_count(result.getVote_count());
                row.setRelease_date(result.getRelease_date());
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(LOG_TAG, " Transaction was a success.");
                Toast.makeText(getApplicationContext(), "Movie added to your favourites", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
               Log.e(LOG_TAG, "Transaction failed and was automatically canceled." + error.toString());
                if(error.toString().contains("RealmPrimaryKeyConstraintException")){
                    Toast.makeText(getApplicationContext(), "It's already added in your favourites", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteFromDataBase(final String movieId){


        realm.beginTransaction();
        Result row = realm.where(Result.class).equalTo("id", movieId).findFirst();
        Toast.makeText(getApplicationContext(), row.toString(), Toast.LENGTH_LONG);
        row.deleteFromRealm();
        realm.commitTransaction();
        Toast.makeText(getApplicationContext(), "Removed movie from favourites", Toast.LENGTH_SHORT).show();

        //move back to parent activity.
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        NavUtils.navigateUpTo(this, upIntent);
    }

    protected void getMovieReview(final View review, String id) {
        MovieService movieService = NetworkServiceBuilder.createService(MovieService.class);
        movieService.fetchReview(KEY_EXTRA.KEY.getDescription(), id, new Callback<Reviews>() {

            @Override
            public void success(Reviews movieReview, Response response) {
                ReviewResults[] movieResult = movieReview.getResults();
                if (movieResult.length > 0)
                    ((TextView) review).setText(movieResult[0].getContent());
                else
                    ((TextView) review).setText("Sorry No Review is Available Till Now!");

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                ((TextView) review).setText("Sorry! Check Back Latter! Network Error!");
            }
        });
    }

    protected void getMovieTrailers(final View review, String id) {
        MovieService movieService = NetworkServiceBuilder.createService(MovieService.class);
        movieService.fetchVideos(KEY_EXTRA.KEY.getDescription(), id, new Callback<Videos>() {

            @Override
            public void success(Videos videos, Response response) {
                VideosResults[] videosResultses = videos.getResults();
                if (videosResultses.length > 0){
                    Log.d(LOG_TAG, videosResultses.toString());
                    ((TextView) review).setText(videosResultses.toString());
                }
                else
                    Log.d(LOG_TAG, "not setting viders");

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                ((TextView) review).setText("Sorry! Check Back Latter! Network Error!");
            }
        });
    }
}
