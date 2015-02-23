package com.yahoo.learn.android.mylocalworld.models;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ankurj on 2/22/2015.
 */
public class InstagramItem extends BaseItem {
    private static final InstagramItem      mInstance = new InstagramItem();



    public InstagramItem fromJSON(JSONObject jPhoto) throws JSONException {
        InstagramItem item = new InstagramItem();

        JSONObject jLocation = jPhoto.getJSONObject("location");
        item.position = new LatLng(jLocation.getDouble("latitude"), jLocation.getDouble("longitude"));
        item.title = jLocation.isNull("name") ? "Image" : jLocation.getString("name");

        item.externalURL = jPhoto.isNull("link") ? null : jPhoto.getString("link");
        item.desc = jPhoto.isNull("caption") ? "" : jPhoto.getJSONObject("caption").getString("text");

        JSONObject images = jPhoto.getJSONObject("images");
        item.imageIconURL = images.getJSONObject("thumbnail").getString("url");
        item.highResImageURL = images.getJSONObject("standard_resolution").getString("url");

        return item;
    }

    public static BaseItem getInstance() {
        return mInstance;
    }
}
