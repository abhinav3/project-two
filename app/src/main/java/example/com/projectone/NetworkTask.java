package example.com.projectone;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;


import java.io.IOException;
import java.net.URL;

import com.squareup.okhttp.*;

import example.com.projectone.util.KEY_EXTRA;

/**
 * Created by Abhinav Ravi on 15/10/16.
 */
public class NetworkTask extends AsyncTask<String, Void, String> {

    private final String LOG_TAG = NetworkTask.class.getSimpleName();
    public static final String THE_MOVIE_DB_API_KEY = KEY_EXTRA.KEY.getDescription();
    
    @Override
    protected String doInBackground(String... params) {

        
        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }

        // Will contain the raw JSON response as a string.
        String movieResponse = null;
        String REQUEST_MOVIE_BY = params[0];
        try {
            // Construct the URL for the themoviedb query
            final String MOVIEDB_BASE_URL =
                    "http://api.themoviedb.org/3/movie/"+REQUEST_MOVIE_BY;
            final String APPID_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                    .appendQueryParameter(APPID_PARAM, THE_MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            // OkHttpClient
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            movieResponse = response.body().string();
            Log.d(LOG_TAG ,movieResponse);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movieResponse data, return null.
            return null;
        } finally {

        }

        return movieResponse;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
