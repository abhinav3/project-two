package example.com.projectone.util;

import com.squareup.okhttp.*;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Abhinav Ravi on 27/01/17.
 */
public class NetworkServiceBuilder {
    public static final String API_BASE_URL = "http://api.themoviedb.org/3";

    private static RestAdapter.Builder builder = new RestAdapter.Builder()
            .setEndpoint(API_BASE_URL)
            .setClient(new OkClient(new OkHttpClient()));

    public static <S> S createService(Class<S> serviceClass) {
        RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
    }
}
