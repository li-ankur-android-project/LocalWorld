package com.yahoo.learn.android.mylocalworld.models;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.learn.android.mylocalworld.R;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ankurj on 2/25/2015.
 */
public class GooglePlacesItem extends BaseItem {
    private static final GooglePlacesItem   mInstance = new GooglePlacesItem();
    private String detailReferenceID;
    private static final String GOOGLE_CLIENT_ID = "AIzaSyC7vS0Zz7YiGWRR-RYBdBRo3Mb-IfP6-5w";
    private static final String GOOGLE_PHOTO_BASE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=120&key=" +  GOOGLE_CLIENT_ID + "&photoreference=";
    private static final String GOOGLE_PLACE_DETAIL_URL = "https://maps.googleapis.com/maps/api/place/details/json?key=" +  GOOGLE_CLIENT_ID + "&placeid=";

    public int getIconResID() { return R.mipmap.ic_gplaces; }

    public GooglePlacesItem fromJSON(JSONObject jPlace) throws JSONException {
        final GooglePlacesItem item = new GooglePlacesItem();

        JSONObject jLocation = jPlace.getJSONObject("geometry").getJSONObject("location");
        item.position = new LatLng(jLocation.getDouble("lat"), jLocation.getDouble("lng"));

        if (jPlace.has("photos") && jPlace.getJSONObject("photos").has("photo_reference")) {
            String imageRefID = jPlace.getJSONObject("photos").getString("photo_reference");
            item.imageIconURL = GOOGLE_PHOTO_BASE_URL + imageRefID;
        }
        else item.imageIconURL = jPlace.getString("icon");

        item.title = jPlace.getString("name");

        item.detailReferenceID = jPlace.isNull("reference") ? null : jPlace.getString("reference");
        // TODO fill other params using reference
        item.desc = jPlace.isNull("vicinity") ? "" : ("Vicinity:" + jPlace.getString("vicinity"));

        String place_id = jPlace.getString("place_id");
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(GOOGLE_PLACE_DETAIL_URL + place_id, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());

                try {
                    item.externalURL = response.getJSONObject("result").getString("url");
                } catch (JSONException e) {
                    Log.e("DEBUG", "Failed to parse Google Place Detail Json: " + e);
                    e.printStackTrace();

                }
            }
        });

        return item;
    }

    public static BaseItem getInstance() {
        return mInstance;
    }
}
