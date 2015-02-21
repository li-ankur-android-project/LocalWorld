package com.yahoo.learn.android.mylocalworld.adapters;

import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.yahoo.learn.android.mylocalworld.R;
import com.yahoo.learn.android.mylocalworld.activities.MainActivity;

/**
 * Created by ankurj on 2/21/2015.
 */
public class CustomWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final MainActivity mActivity;

    public CustomWindowAdapter(MainActivity activity) {
        mActivity = activity;
    }

    // This defines the contents within the info window based on the marker
    @Override
    public View getInfoContents(Marker marker) {
        // Getting view from the layout file
        View v = mActivity.getLayoutInflater().inflate(R.layout.item_local_stuff, null);

        return CustomItemAdapter.getViewForItem(mActivity.getItems().get(Integer.parseInt(marker.getSnippet())), v);
    }

    // This changes the frame of the info window; returning null uses the default frame.
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }
}