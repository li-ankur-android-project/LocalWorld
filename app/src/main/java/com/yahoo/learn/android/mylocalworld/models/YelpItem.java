package com.yahoo.learn.android.mylocalworld.models;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ankurj on 2/23/2015.
 */
public class YelpItem extends BaseItem {
    private static final YelpItem      mInstance = new YelpItem();



    public YelpItem fromJSON(JSONObject jsonObject) throws JSONException {
        YelpItem baseItem = new YelpItem();

        baseItem.title = jsonObject.getString("name");

        double lat = jsonObject.getJSONObject("location").getJSONObject("coordinate").getDouble("latitude");
        double lng = jsonObject.getJSONObject("location").getJSONObject("coordinate").getDouble("longitude");
        baseItem.position = new LatLng(lat, lng);

        baseItem.address = jsonObject.getJSONObject("location").getString("address");

        baseItem.imageIconURL = jsonObject.getString("snippet_image_url");
        //TODO Yelp doesn't provide description
        baseItem.desc = jsonObject.getString("snippet_text");
        baseItem.externalURL = jsonObject.getString("mobile_url");

        return baseItem;

    }

    public static BaseItem getInstance() {
        return mInstance;
    }

}
