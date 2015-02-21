package com.yahoo.learn.android.mylocalworld.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.squareup.picasso.Picasso;
import com.yahoo.learn.android.mylocalworld.R;
import com.yahoo.learn.android.mylocalworld.models.BaseItem;

/**
 * Created by ankurj on 2/21/2015.
 *
 * All the item related customizations go in this class
 */
public class CustomItemAdapter {

    // TODO: Customize based on item type
    static View getViewForItem(BaseItem item, View view, boolean isListView) {
        ImageView ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvDesc = (TextView) view.findViewById(R.id.tvDesc);
        ImageView ivHighResImage = (ImageView) view.findViewById(R.id.ivHighResImage);
        TextView tvDummy = (TextView) view.findViewById(R.id.tvDummy);

        tvTitle.setText(item.getTitle());
        tvDesc.setText(item.getDesc());

        tvDummy.setVisibility(isListView ? View.VISIBLE : View.GONE);

        if (item.getHighResImageURL() != null) {
            ivIcon.setVisibility(View.GONE);
            tvTitle.setVisibility(View.GONE);
            tvDesc.setVisibility(View.GONE);
            ivHighResImage.setVisibility(View.VISIBLE);
            ivHighResImage.setImageResource(0);
            Picasso.with(view.getContext()).load(item.getHighResImageURL()).into(ivHighResImage);
        } else {
            ivIcon.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
            tvDesc.setVisibility(View.VISIBLE);
            ivHighResImage.setVisibility(View.GONE);
            Picasso.with(view.getContext()).load(item.getImageIconURL()).into(ivIcon);
        }

        return view;
    }

    public static float getColorForMarker(BaseItem item) {
        if (item.getHighResImageURL() != null) {
            return BitmapDescriptorFactory.HUE_ORANGE;
        } else {
            return BitmapDescriptorFactory.HUE_GREEN;
        }
    }
}
