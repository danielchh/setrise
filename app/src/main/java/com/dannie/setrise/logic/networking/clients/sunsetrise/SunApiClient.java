package com.dannie.setrise.logic.networking.clients.sunsetrise;

import com.dannie.setrise.logic.networking.LiveDataCallAdapterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

import static com.dannie.setrise.logic.other.Constants.NETWORK_REQUEST_TIMEOUT_SEC;

public class SunApiClient {

    private static final String BASE_URL = "https://api.sunrise-sunset.org/";

    private static SunApiClient apiClient;
    private static SunWebService webService;

    private SunApiClient() {
        OkHttpClient client;
        client = new OkHttpClient.Builder()
                .readTimeout(NETWORK_REQUEST_TIMEOUT_SEC, TimeUnit.SECONDS)
                .connectTimeout(NETWORK_REQUEST_TIMEOUT_SEC, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build();
        webService = retrofit.create(SunWebService.class);
    }

    public static SunApiClient getClient() {
        if (apiClient == null) {
            apiClient = new SunApiClient();
        }
        return apiClient;
    }

    public SunWebService getWebService() {
        return webService;
    }

}