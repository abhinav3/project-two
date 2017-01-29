package example.com.projectone.util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.squareup.okhttp.*;

/**
 * Created by Abhinav Ravi on 22/11/16.
 */
public class NetworkUtil {

     Context mContext;
    public NetworkUtil(Context mContext) {
        this.mContext = mContext;
    }

    public  boolean isInternetWorking() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    //TODO : Have to implement this in background thread.
    public static boolean isInternetWorking2() {
        boolean success = false;
        try {
            URL url = new URL("https://google.com");
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();

            success = response.code() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }
}
