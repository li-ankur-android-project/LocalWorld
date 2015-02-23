package com.yahoo.learn.android.mylocalworld.ApiClient;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by liyli on 2/22/15.
 */
public class ApiClient {

    private static final String INSTAGRAM_BASE_URL =  "https://api.instagram.com/v1/locations/search";
    private static final String INSTAGRAM_CLIENT_ID = "68ecacb5efa94844a70e7a6e55e44d2d";

    public static void getInstagramLocation(double lat, double lng, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("client_id", INSTAGRAM_CLIENT_ID);

        new AsyncHttpClient().get(INSTAGRAM_BASE_URL, params, handler);
    }
}
