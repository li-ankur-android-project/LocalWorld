package com.yahoo.learn.android.mylocalworld.models;

import com.yahoo.learn.android.mylocalworld.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ankurj on 2/26/2015.
 */
public class GeminiAdItem extends BaseItem {
    private static GeminiAdItem mInstance = new GeminiAdItem();

    public static GeminiAdItem getInstance() { return mInstance; }

    @Override
    public int getIconResID() {
        return R.mipmap.ic_gemini;
    }

    @Override
    public BaseItem fromJSON(JSONObject jsonObject) throws JSONException {
        GeminiAdItem item = new GeminiAdItem();


        String tag = jsonObject.getString("tag");

        JSONObject jTag = new JSONObject(tag);
        item.title = jTag.getString("headline");
        item.desc = jTag.getString("summary");
        item.imageIconURL = jTag.getString("image");
        item.externalURL = jTag.getString("clickUrl");

        return item;

    }
}
