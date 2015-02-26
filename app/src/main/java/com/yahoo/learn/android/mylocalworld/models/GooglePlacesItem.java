package com.yahoo.learn.android.mylocalworld.models;

import com.google.android.gms.maps.model.LatLng;
import com.yahoo.learn.android.mylocalworld.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ankurj on 2/25/2015.
 */
public class GooglePlacesItem extends BaseItem {
    private static final GooglePlacesItem   mInstance = new GooglePlacesItem();
    private String detailReferenceID;

    public int getIconResID() { return R.mipmap.ic_gplaces; }

    public GooglePlacesItem fromJSON(JSONObject jPlace) throws JSONException {
        GooglePlacesItem item = new GooglePlacesItem();

        JSONObject jLocation = jPlace.getJSONObject("geometry").getJSONObject("location");
        item.position = new LatLng(jLocation.getDouble("lat"), jLocation.getDouble("lng"));

        item.imageIconURL = jPlace.getString("icon");
        item.title = jPlace.getString("name");

        item.detailReferenceID = jPlace.isNull("reference") ? null : jPlace.getString("reference");
        // TODO fill other params using reference
        item.desc = jPlace.isNull("vicinity") ? "" : ("Vicinity:" + jPlace.getString("vicinity"));


        return item;
    }

    public static BaseItem getInstance() {
        return mInstance;
    }
}
