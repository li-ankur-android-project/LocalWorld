package com.yahoo.learn.android.mylocalworld.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yahoo.learn.android.mylocalworld.R;

/**
 * Created by ankurj on 2/20/2015.
 */
public class ItemFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Create
        View v = inflater.inflate(R.layout.item_local_stuff, container, false);

        // Lookup subviews
        TextView tvTitle= (TextView) v.findViewById(R.id.tvTitle);


        // Return view
        return v;
    }


    // Oncreate of fragment is called before activity's onCreate gets called, the subsequent
    // onActivityCreated method gets called after activity gets created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
