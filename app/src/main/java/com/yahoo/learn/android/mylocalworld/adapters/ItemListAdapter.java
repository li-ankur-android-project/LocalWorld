package com.yahoo.learn.android.mylocalworld.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.yahoo.learn.android.mylocalworld.R;
import com.yahoo.learn.android.mylocalworld.models.BaseItem;

import java.util.List;

/**
 * Created by ankurj on 2/20/2015.
 */
public class ItemListAdapter extends ArrayAdapter <BaseItem> {

    public ItemListAdapter(Context context, List<BaseItem> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_local_stuff, parent, false);
        }

        return CustomItemAdapter.getViewForItem(item, convertView);
    }


    //    @Override
//    // Fragments based adapter
//    public View getView(int position, View convertView, ViewGroup parent) {
//        BaseItem item = getItem(position);
//        Fragment fragment = ItemFragment.getFragmentForItem(item);
//
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_fragment_holder, parent, false);
//        }
//
//        FragmentTransaction ft = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
//
//        ft.replace(R.id.flItemContainer, fragment);
//        ft.commit();
//
//        return convertView;
//    }

}
