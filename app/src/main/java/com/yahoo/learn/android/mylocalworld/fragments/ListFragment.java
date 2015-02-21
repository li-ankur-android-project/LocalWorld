package com.yahoo.learn.android.mylocalworld.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.yahoo.learn.android.mylocalworld.R;
import com.yahoo.learn.android.mylocalworld.activities.MainActivity;
import com.yahoo.learn.android.mylocalworld.adapters.ItemListAdapter;
import com.yahoo.learn.android.mylocalworld.models.BaseItem;

import java.util.ArrayList;

/**
 * Created by ankurj on 2/20/2015.
 */
public class ListFragment extends Fragment {
    private ListView                mLvItems;
    private ArrayList<BaseItem>     mItems;
    private ItemListAdapter         mAdapter;

    // Oncreate of fragment is called before activity's onCreate gets called, the subsequent
    // onActivityCreated method gets called after activity gets created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItems = ((MainActivity) getActivity()).getItems();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Create
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        // Lookup subviews
        mLvItems = (ListView) v.findViewById(R.id.lvItems);
        mAdapter = new ItemListAdapter(getActivity(), mItems);
        mLvItems.setAdapter(mAdapter);

        // Return view
        return v;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void onItemsChanged() {
        mAdapter.notifyDataSetChanged();
    }
}
