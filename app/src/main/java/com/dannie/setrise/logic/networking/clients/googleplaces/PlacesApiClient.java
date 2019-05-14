package com.dannie.setrise.logic.networking.clients.googleplaces;

import com.dannie.setrise.logic.networking.LiveDataCallAdapterFactory;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

import static com.dannie.setrise.logic.other.Constants.NETWORK_REQUEST_TIMEOUT_SEC;

public class PlacesApiClient {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";

    /**
     * Could have saved it in strings.xml, but then I'd need to inject Context here, so why not.
     */
    private static final String KEY_PARAM_NAME = "key";
    private static final String GOOGLE_PLACES_API_KEY = "AIzaSyAttjNEHFCK4xRenIC3hlkEF2t5Y_5zpM8";

    private static PlacesApiClient apiClient;
    private static PlacesWebService webService;

    private PlacesApiClient() {
        OkHttpClient client;
        client = new OkHttpClient.Builder().addInterceptor(chain -> {
            //adding api-key param to every request
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter(KEY_PARAM_NAME, GOOGLE_PLACES_API_KEY)
                    .build();

            Request request = original.newBuilder()
                    .url(url)
                    .build();

            return chain.proceed(request);
        })
                .readTimeout(NETWORK_REQUEST_TIMEOUT_SEC, TimeUnit.SECONDS)
                .connectTimeout(NETWORK_REQUEST_TIMEOUT_SEC, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build();
        webService = retrofit.create(PlacesWebService.class);
    }

    public static PlacesApiClient getClient() {
        if (apiClient == null) {
            apiClient = new PlacesApiClient();
        }
        return apiClient;
    }

    public PlacesWebService getWebService() {
        return webService;
    }

}
