package com.yahoo.learn.android.mylocalworld.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.learn.android.mylocalworld.R;
import com.yahoo.learn.android.mylocalworld.adapters.CustomWindowAdapter;
import com.yahoo.learn.android.mylocalworld.adapters.HomePagerAdapter;
import com.yahoo.learn.android.mylocalworld.ApiClient.ApiClient;
import com.astuetz.PagerSlidingTabStrip;
import com.yahoo.learn.android.mylocalworld.fragments.ListFragment;
import com.yahoo.learn.android.mylocalworld.fragments.MapViewFragment;
import com.yahoo.learn.android.mylocalworld.models.BaseItem;
import com.yahoo.learn.android.mylocalworld.models.InstagramItem;
import com.yahoo.learn.android.mylocalworld.models.YelpItem;
import com.yahoo.learn.android.mylocalworld.util.LocationComparator;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends ActionBarActivity implements


        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, MapViewFragment.OnItemSelectedListener{

    private final CustomWindowAdapter mCustomWindowAdapter = new CustomWindowAdapter(this);
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 300000;  /* 5 min */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */

    private String mSearchQuery = "";
    private Location mLocation = null;

    private ArrayList<BaseItem> mItems = new ArrayList<BaseItem>();
    private ViewPager           mViewPager;
    private ListFragment        mListFragment;
    private MapViewFragment     mMapFragment;

    private ProgressBar         mPbFetch;

    /*
     * Define a request code to send to Google Play services This code is
     * returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        context=this;

        mPbFetch = (ProgressBar) findViewById(R.id.pbFetch);
        mPbFetch.setVisibility(View.GONE);

        mListFragment = new ListFragment();
        mMapFragment = new MapViewFragment();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
//        mViewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager(),new Fragment[] {mHomeTimelineFragment, mMentionsFragment, mUserTimelineFragment} ));
        mViewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager(),
                new Fragment[] {mMapFragment, mListFragment},
                new String[] {"Map", "List"}));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(mViewPager);

//        populateItems();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("SearchQueryCalled", "Perform search text submitted - " + s);
                if (replaceSearchQuery(s)) {
                    Log.d("SearchQueryExecuting", "Perform search text submitted - " + s);
                    performSearchQuery();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.trim().length() == 0) {
                    if (replaceSearchQuery(s)) {
                        Log.d("SearchRemovedExecuting", "Perform search text cleaned up - " + s);
                        performSearchQuery();
                        return true;
                    }
                }
                return false;
            }

        });

        return true;
    }


    // Atomically set the search query, return true if the query is a new string
    private synchronized boolean replaceSearchQuery(String s) {
        if (s == null)
            s = "";

        s = s.trim();
        if (mSearchQuery.equals(s)) {
            Log.d("SearchQueryDuplicate", "Duplicate Search: " + s);
            return false;
        }

        Log.d("SearchQueryReplace", "Current " + mSearchQuery + ", new " + s);
        mSearchQuery = s;
        return true;
    }

    private void performSearchQuery() {

        if (mLocation == null){
            Toast.makeText(this, "location not updated", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d("SearchQueryLocExecuting", "Perform search " + mLocation.getLatitude() + ":" + mLocation.getLongitude() + " - " + mSearchQuery);

        mItems.clear();
        mPbFetch.setVisibility(View.VISIBLE);
        notifyFragmentAdapters();

        // Populate some dummy items
//        populateItems();
        ApiClient.searchInstagram(mLocation.getLatitude(), mLocation.getLongitude(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());

                try {
                    addItemsToList(InstagramItem.getInstance().fromJSONArray(response.getJSONArray("data")));
                } catch (JSONException e) {
                    Log.e("SearchQueryInstagramFailure", "Failed: " + e);
                    e.printStackTrace();
                }
            }
        });



        ApiClient.getYelpLocationByLatLong(mSearchQuery, mLocation.getLatitude(), mLocation.getLongitude(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", "Success: " + response.toString());
                try {
                    addItemsToList(YelpItem.getInstance().fromJSONArray(response.getJSONArray("businesses")));
                } catch (JSONException e) {
                    Log.e("ERROR", "failedd to parse; " + e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject response){
                Log.e("SearchQueryYelpFailure", "Failed: " + t);
                t.printStackTrace();;
            }
        });

    }


    private synchronized void addItemsToList(List<BaseItem> items) {
        mItems.addAll(items);
        Collections.sort(mItems, new LocationComparator(mLocation));
        mPbFetch.setVisibility(View.GONE);
        notifyFragmentAdapters();
    }

    private void notifyFragmentAdapters() {
        mListFragment.onItemsChanged();
        mMapFragment.setMarkers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ArrayList<BaseItem> getItems() {
        return mItems;
    }


    @Override
    public void onRssItemSelected(GoogleMap googleMap) {
        loadMap(googleMap);
    }

    protected void loadMap(GoogleMap googleMap) {
        if (googleMap != null) {
//            Toast.makeText(this, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
            googleMap.setMyLocationEnabled(true);

            // Now that map has loaded, let's get our location!
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();

            googleMap.setInfoWindowAdapter(mCustomWindowAdapter);


            focusMapOnCurrentLocation();
            connectClient();
        } else {
            Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    protected void connectClient() {
        // Connect the client.
        if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    /*
     * Called when the Activity becomes visible.
    */
    @Override
    protected void onStart() {
        super.onStart();
        connectClient();
    }

    /*
	 * Called when the Activity is no longer visible.
	 */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    /*
     * Handle results returned to the FragmentActivity by Google Play services
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {

            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mGoogleApiClient.connect();
                        break;
                }

        }
    }

    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getSupportFragmentManager(), "Location Updates");
            }

            return false;
        }
    }

    /*
     * Called by Location Services when the request to connect the client
     * finishes successfully. At this point, you can request the current
     * location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            performLocationUpdate(location);
            startLocationUpdates();
        } else {
            Toast.makeText(this, "Current location was null, enable GPS on emulator!", Toast.LENGTH_LONG).show();
        }
    }

    private void performLocationUpdate(Location location) {
        mLocation = location;

        focusMapOnCurrentLocation();

        performSearchQuery();
        // Report to the UI that the location was updated
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void focusMapOnCurrentLocation() {
        if ((mMapFragment != null) && (mLocation != null)) {
            LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);

            mMapFragment.animateCamera(cameraUpdate);
        }
    }

    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    public void onLocationChanged(Location location) {
        // TODO - ensure that app is in the foreground before calling any of the below code

        // Check if we are far enough (500 meters) from the previous saved location
        if ((mLocation == null) || (mLocation.distanceTo(location) > 500.0)) {
            performLocationUpdate(location);
        }
    }

    /*
     * Called by Location Services if the connection to the location client
     * drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * Called by Location Services if the attempt to Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
        }
    }

    public Location getMapLocation() {
        return mLocation;
    }


    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
}
