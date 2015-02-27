package com.yahoo.learn.android.mylocalworld.ApiClient;

import android.view.Window;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liyli on 2/22/15.
 */
public class ApiClient {

    private static final String INSTAGRAM_BASE_URL =  "https://api.instagram.com/v1/locations/search";
    private static final String INSTAGRAM_MEDIA_URL = "https://api.instagram.com/v1/media/search";
    private static final String INSTAGRAM_CLIENT_ID = "68ecacb5efa94844a70e7a6e55e44d2d";
    private static final String YELP_SEARCH_URL = "http://api.yelp.com/v2/search";
    private static final String GOOGLE_CLIENT_ID = "AIzaSyC7vS0Zz7YiGWRR-RYBdBRo3Mb-IfP6-5w";
    private static final String GOOGLE_PLACES_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";

    private static final String GEMINI_AD_URL_FULL = "https://n.gemini.yahoo.com/ssi?format=json&ve=2&ic=1&sz=3x3&nc=1&tt=1&se=4494903&im=63&ff=&cc=STRM&tk=10&hs=1&ui=developer&ri=a33a476e&pu=&sl=&pc=&nr=2&mi=";
    private static final String GEMINI_AD_URL = "https://n.gemini.yahoo.com/ssi";

    public static void getGooglePlaces(String searchTerm, double lat, double lng, JsonHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("location", lat + "," + lng);
        if ((searchTerm != null) && (searchTerm.trim().length() > 0))
            params.put("keyword", searchTerm);
        params.put("radius", 1000);
        params.put("key", GOOGLE_CLIENT_ID);

        new AsyncHttpClient().get(GOOGLE_PLACES_URL, params, handler);

    }



    public static void getGeminiAd(int numAds, double lat, double lng, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();

        params.put("format", "json");
        params.put("ve", 2);
        params.put("tk", "" + numAds);
        params.put("ri", "" + (long) (2e9 * Math.random()));
        params.put("hs", "1");
        params.put("mi", "");
        params.put("se", "4494903");
        params.put("cc", "STRM");
        params.put("sz", "3x3");
        params.put("ir", 1);
        params.put("la", lat);
        params.put("lo", lng);


        Header xfHeader = new Header() {
            @Override
            public String getName() {
                return "X-Forwarded-For";
            }

            @Override
            public String getValue() {
                return "50.156.22.183";
            }

            @Override
            public HeaderElement[] getElements() throws ParseException {
                return new HeaderElement[0];
            }
        };


        new AsyncHttpClient().get(null, GEMINI_AD_URL, new Header[] {xfHeader}, params, handler);
    }


    public static void getInstagramLocation(double lat, double lng, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("client_id", INSTAGRAM_CLIENT_ID);

        new AsyncHttpClient().get(INSTAGRAM_BASE_URL, params, handler);

        //AsyncHttpClient client = new AsyncHttpClient();
        //client.
    }

    public static void getYelpLocationByName(String searchTerm, String location, JsonHttpResponseHandler handler){

        YelpClient yelpClient = new YelpClient();
        Map<String, String > headers = yelpClient.getHeader(searchTerm, location);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("term", searchTerm);
        params.put("location", location);

        client.addHeader("Authorization", headers.get("Authorization"));
        client.get(YELP_SEARCH_URL, params, handler);
    }

    public static void getYelpLocationByLatLong(String searchTerm, double lat, double lng, JsonHttpResponseHandler handler){

        YelpClient yelpClient = new YelpClient();
        Map<String, String > headers = yelpClient.getHeader(searchTerm, lat, lng);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("term", searchTerm);
        params.put("ll", Double.toString(lat)+","+Double.toString(lng));

        client.addHeader("Authorization", headers.get("Authorization"));
        client.get(YELP_SEARCH_URL, params, handler);
    }

    public static class YelpClient {
        private static final String YELP_CONSUMER_KEY = "LWJeR5VyZf4pC4b6FeCTbA";
        private static final String YELP_CONSUMER_SECRET = "JCxB5Fz2_UYNxJEnsBkEJ9e1nIw";
        private static final String YELP_TOKEN = "auZmDM3mU9iH5WYegkPIuBi338qfNdy7";
        private static final String YELP_TOKEN_SECRET = "kzKDysu56JGhx2Akg16O7L95k68";

        OAuthService service;
        Token accessToken;

        public YelpClient(){
            this.service =
                    new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(YELP_CONSUMER_KEY)
                            .apiSecret(YELP_CONSUMER_SECRET).build();
            this.accessToken = new Token(YELP_TOKEN, YELP_TOKEN_SECRET);
        }

        public Map<String, String> getHeader (String searchTerm, double lat, double lng){
            OAuthRequest request = new OAuthRequest(Verb.GET, YELP_SEARCH_URL);
            request.addQuerystringParameter("term", searchTerm);
            request.addQuerystringParameter("ll", Double.toString(lat)+","+Double.toString(lng));
            this.service.signRequest(this.accessToken, request);
            return request.getHeaders();
        }

        public Map<String, String> getHeader (String searchTerm, String location){
            OAuthRequest request = new OAuthRequest(Verb.GET, YELP_SEARCH_URL);
            request.addQuerystringParameter("term", searchTerm);
            request.addQuerystringParameter("location", location);
            this.service.signRequest(this.accessToken, request);
            return request.getHeaders();
        }
    }


    public static void searchInstagram(double lat, double lng, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("client_id", INSTAGRAM_CLIENT_ID);

        new AsyncHttpClient().get(INSTAGRAM_MEDIA_URL, params, handler);
    }
}
