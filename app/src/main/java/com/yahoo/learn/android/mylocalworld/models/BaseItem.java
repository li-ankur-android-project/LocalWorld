package com.yahoo.learn.android.mylocalworld.models;

import com.google.android.gms.maps.model.LatLng;
import com.yahoo.learn.android.mylocalworld.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ankurj on 2/20/2015.
 */
public class BaseItem {
    private static final BaseItem       mInstance = new BaseItem();

    protected LatLng      position;
    protected String      imageIconURL;
    protected String      desc;
    protected String      title;
    protected String      externalURL;
    protected String      highResImageURL;
    protected String      address;


    public String getHighResImageURL() {
        return highResImageURL;
    }

    public LatLng getPosition() {
        return position;
    }

    public String getImageIconURL() {
        return imageIconURL;
    }

    public String getDesc() {
        return desc;
    }

    public String getTitle() {
        return title;
    }

    public String getExternalURL() {
        return externalURL;
    }

    public int getIconResID() { return R.mipmap.ic_launcher; }

    public String toString() { return title; }


    // Code to create dummy objects
    public BaseItem fromJSON(JSONObject jsonObject) throws JSONException {
        BaseItem baseItem = new BaseItem();

        baseItem.title = "Title" + Math.random();
        baseItem.position = new LatLng(36.1 + (Math.random() - 0.5) / 200.0, -115.2 + (Math.random() - 0.5) / 200.0);
        baseItem.imageIconURL = "https://igcdn-photos-g-a.akamaihd.net/hphotos-ak-xpf1/t51.2885-19/10467833_658921474177126_1611352287_a.jpg";
        baseItem.desc = "Go visit http://www.yahoo.com";

        if (Math.random() < 0.3) {
            baseItem.highResImageURL = baseItem.imageIconURL;
        }

        return baseItem;

    }

    public ArrayList<BaseItem> fromJSONArray(JSONArray jsonArray) {

        ArrayList<BaseItem> items = new ArrayList<BaseItem>();

        for (int i = 0; i< jsonArray.length(); i++)
        {
            try {
                BaseItem item = fromJSON(jsonArray.getJSONObject(i));
                items.add(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return items;
    }

    public static BaseItem getInstance() {
        return mInstance;
    }

}
