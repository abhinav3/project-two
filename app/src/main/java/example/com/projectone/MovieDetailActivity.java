package example.com.projectone;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import java.util.List;

import example.com.projectone.Adapters.CustomerTrailerAdapter;
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
    private static RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView.Adapter adapter;
    private VideosResults[] videosResultses;
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


        //set card view adapter
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());


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

        ImageView shareImageView = (ImageView) findViewById(R.id.shareButton);
        shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareYoutubeIntent();
            }
        });


    }

    public  class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            int selectedItemPosition = recyclerView.getChildPosition(view);
            watchYoutubeVideo(videosResultses[selectedItemPosition].getKey());
        }
    }

    public void watchYoutubeVideo(String id) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            startActivity(intent);
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
                    ((TextView) review).setText("Be the first to review");

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                ((TextView) review).setText("Network Error!");
            }
        });
    }

    protected void getMovieTrailers(final View review, String id) {
        MovieService movieService = NetworkServiceBuilder.createService(MovieService.class);
        movieService.fetchVideos(KEY_EXTRA.KEY.getDescription(), id, new Callback<Videos>() {

            @Override
            public void success(Videos videos, Response response) {
                videosResultses = videos.getResults();
                if (videosResultses.length > 0){
                    //set card view trailers data
                    adapter = new CustomerTrailerAdapter(videosResultses, MovieDetailActivity.this);
                    recyclerView.setAdapter(adapter);
                    Log.e(LOG_TAG, "***************************************************************\n"+videosResultses.toString());
                }
                else
                    Log.e(LOG_TAG, "not setting videos");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                ((TextView) review).setText("Network Error!");
            }
        });
    }

    private void shareYoutubeIntent(){
        String shareBody = "Find this exciting movie : ";
        try {
            if(videosResultses[0].getKey() != null && !videosResultses[0].getKey().isEmpty()){
                String youTubeURL = "https://www.youtube.com/watch?v=" +  videosResultses[0].getKey();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sharing movie trailer with you");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody + youTubeURL);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "URLs still not loaded");

        }

    }
}
