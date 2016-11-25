package example.com.projectone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import example.com.projectone.models.Result;
import example.com.projectone.util.ColorUtil;
import example.com.projectone.util.KEY_EXTRA;
import example.com.projectone.util.NetworkUtil;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
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

        TextView ratingValue = (TextView) findViewById(R.id.ratingValue);
        TextView reviewValue = (TextView) findViewById(R.id.reviewValue);
        TextView overView = (TextView) findViewById(R.id.overview);
        TextView release_date = (TextView) findViewById(R.id.release_date_value);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Result result = (Result)getIntent().getSerializableExtra(KEY_EXTRA.Result.getDescription());
        this.setTitle(result.getTitle());

        ratingValue.setText(result.getVoteAverage().toString() + " / 10");
        reviewValue.setText(result.getVoteCount().toString());
        overView.setText(result.getOverview());
        release_date.setText(result.getReleaseDate());
        ImageView imageView = (ImageView) findViewById(R.id.header);
        Log.d(LOG_TAG,"Poster Path" +result.getPosterPath() + " title " + result.getOriginalTitle());

        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w500/"+result.getPosterPath())
                .placeholder(new ColorUtil().getRandomDrawbleColor())
                .into(imageView);
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
