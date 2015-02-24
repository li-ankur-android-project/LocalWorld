package com.yahoo.learn.android.mylocalworld.models;

import com.google.android.gms.maps.model.LatLng;
import com.yahoo.learn.android.mylocalworld.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ankurj on 2/22/2015.
 */
public class InstagramItem extends BaseItem {
    private static final InstagramItem      mInstance = new InstagramItem();

    protected String    userID;
    protected String    userName;

    @Override
    public String getTitle() {
        return "@" + userID;
    }

    public int getIconResID() { return R.mipmap.ic_instagram; }


    public InstagramItem fromJSON(JSONObject jPhoto) throws JSONException {
        InstagramItem item = new InstagramItem();

        JSONObject jLocation = jPhoto.getJSONObject("location");
        item.position = new LatLng(jLocation.getDouble("latitude"), jLocation.getDouble("longitude"));
        item.title = jLocation.isNull("name") ? "Image" : jLocation.getString("name");

        item.externalURL = jPhoto.isNull("link") ? null : jPhoto.getString("link");
        item.desc = jPhoto.isNull("caption") ? "" : jPhoto.getJSONObject("caption").getString("text");

        JSONObject images = jPhoto.getJSONObject("images");
        item.imageIconURL = images.getJSONObject("thumbnail").getString("url");
        item.highResImageURL = images.getJSONObject("low_resolution").getString("url");

        JSONObject jUser = jPhoto.getJSONObject("user");
        item.userID = jUser.getString("username");
        item.userName = jUser.getString("full_name");

        return item;
    }

    public static BaseItem getInstance() {
        return mInstance;
    }
}
