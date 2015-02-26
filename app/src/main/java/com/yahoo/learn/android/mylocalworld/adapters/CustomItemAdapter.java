package com.yahoo.learn.android.mylocalworld.adapters;

import android.app.ActionBar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.squareup.picasso.Picasso;
import com.yahoo.learn.android.mylocalworld.R;
import com.yahoo.learn.android.mylocalworld.models.BaseItem;
import com.yahoo.learn.android.mylocalworld.models.GooglePlacesItem;
import com.yahoo.learn.android.mylocalworld.models.InstagramItem;
import com.yahoo.learn.android.mylocalworld.models.YelpItem;

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
        ImageView ivProviderIcon = (ImageView) view.findViewById(R.id.ivProviderIcon);

        tvTitle.setText(item.getTitle());
        tvDesc.setText(item.getDesc());
        ivProviderIcon.setImageResource(item.getIconResID());
        ivIcon.setImageResource(0);
        Picasso.with(view.getContext()).load(item.getImageIconURL()).into(ivIcon);

        tvDummy.setVisibility(isListView ? View.VISIBLE : View.GONE);

        if (isListView) {
            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, view.getContext().getResources().getDisplayMetrics());
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivIcon.getLayoutParams();
            params.height = (int)pixels;
            params.width = (int)pixels;
            ivIcon.setLayoutParams(params);
        }

        if (isListView && (item instanceof InstagramItem)) {
            ivIcon.setVisibility(View.GONE);
            tvTitle.setVisibility(View.GONE);
            tvDesc.setVisibility(View.GONE);
            ivHighResImage.setVisibility(View.VISIBLE);
            ivProviderIcon.setVisibility(View.GONE);
            ivHighResImage.setImageResource(0);
            Picasso.with(view.getContext()).load(item.getHighResImageURL()).into(ivHighResImage);
        } else {
            ivIcon.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
            tvDesc.setVisibility(View.VISIBLE);
            ivProviderIcon.setVisibility(View.VISIBLE);
            ivHighResImage.setVisibility(View.GONE);
        }

        return view;
    }

    public static float getColorForMarker(BaseItem item) {
        if (item instanceof InstagramItem) {
            return BitmapDescriptorFactory.HUE_ORANGE;
        } else if (item instanceof YelpItem) {
            return BitmapDescriptorFactory.HUE_AZURE;
        } else if (item instanceof GooglePlacesItem) {
            return BitmapDescriptorFactory.HUE_RED;
        } else {
            return BitmapDescriptorFactory.HUE_GREEN;
        }
    }
}
