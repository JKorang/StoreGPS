package com.rowansenior.storegps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by Engineering on 4/28/15.
 */
public class UserLocation {

    Context context;

    public UserLocation(Context context)
    {
        this.context = context;
    }

    public void onCreate() {
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        LocationListener ll = new myLocationListener();
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, ll);
    }

    private class myLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                double dLong = location.getLongitude();
                double dLat = location.getLatitude();

                System.out.println("Latitude: " + dLat);
                System.out.println("Longitude: " + dLong);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

}
