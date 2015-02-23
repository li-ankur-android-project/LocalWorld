package com.yahoo.learn.android.mylocalworld.fragments;


import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yahoo.learn.android.mylocalworld.R;
import com.yahoo.learn.android.mylocalworld.activities.MainActivity;
import com.yahoo.learn.android.mylocalworld.adapters.CustomItemAdapter;
import com.yahoo.learn.android.mylocalworld.adapters.CustomWindowAdapter;
import com.yahoo.learn.android.mylocalworld.models.BaseItem;

import java.util.ArrayList;

public class MapViewFragment extends Fragment {

    private OnItemSelectedListener listener;
    private CustomWindowAdapter     mCustomWindowAdapter;
    private GoogleMap mGoogleMap;

    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {
        public void onRssItemSelected(GoogleMap googleMap);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static MapViewFragment newInstance(String param1, String param2) {
        MapViewFragment fragment = new MapViewFragment();

        return fragment;
    }

    public MapViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mCustomWindowAdapter = new CustomWindowAdapter((MainActivity) getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
//                    Toast.makeText(getActivity(), "Success!!", Toast.LENGTH_SHORT).show();
                    mGoogleMap = map;
                    if (listener != null)
                        listener.onRssItemSelected(map);
//                    setMarkers();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }

//        if (mapFragment != null) {
//            // The Map is verified. It is now safe to manipulate the map.
//            mapFragment.getMap().setInfoWindowAdapter(mCustomWindowAdapter);
//        }
//


        // Inflate the layout for this fragment
        return v;
    }


    public void setMarkers() {
        mGoogleMap.clear();
        MainActivity activity = (MainActivity) getActivity();
        ArrayList<BaseItem> items = activity.getItems();


//        BitmapDescriptor defaultMarker =
//                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);

        for (int i=0, len=items.size(); i<len; i++) {
            BaseItem item = items.get(i);
            BitmapDescriptor defaultMarker =
                    BitmapDescriptorFactory.defaultMarker(CustomItemAdapter.getColorForMarker(item));

            // Creates and adds marker to the map
            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(item.getPosition())
                    .title(item.getTitle())
                    .snippet("" + i)
                    .icon(defaultMarker));


        }

        if (items.size() > 0)
            setMapBounds(activity.getMapLocation(), items, 20);
    }



    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }


    private void setMapBounds(Location currentLoc, ArrayList<BaseItem> items, int maxItems)
    {
        LatLngBounds.Builder bc = new LatLngBounds.Builder();

        if(items.size() < maxItems)
            maxItems = items.size();

        for (int i=0; i<maxItems; i++) {
            bc.include(items.get(i).getPosition());
        }


        bc.include(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()));

//        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 17));
        animateCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));
    }

    public void animateCamera(CameraUpdate cameraUpdate) {
        mGoogleMap.animateCamera(cameraUpdate);
    }
}

