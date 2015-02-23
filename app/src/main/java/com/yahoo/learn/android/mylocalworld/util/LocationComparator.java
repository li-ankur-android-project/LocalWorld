package com.yahoo.learn.android.mylocalworld.util;

import android.location.Location;

import com.yahoo.learn.android.mylocalworld.models.BaseItem;

import java.util.Comparator;

/**
 * Created by ankurj on 2/23/2015.
 */
public class LocationComparator implements Comparator<BaseItem> {
    private final Location mLocation;

    public LocationComparator(Location currentLocation) {
        mLocation = currentLocation;
    }

    @Override
    public int compare(BaseItem lhs, BaseItem rhs) {
        Location lhsLocation = createLocation(lhs, "left");
        Location rhsLocation = createLocation(rhs, "right");

        float ldist = mLocation.distanceTo(lhsLocation);
        float rdist = mLocation.distanceTo(rhsLocation);

        return ((ldist < rdist) ? -1 : ((ldist == rdist) ? 0 : 1));
    }

    private Location createLocation(BaseItem item, String name) {
        Location loc = new Location(name);
        loc.setLatitude(item.getPosition().latitude);
        loc.setLongitude(item.getPosition().longitude);

        return loc;
    }
}
