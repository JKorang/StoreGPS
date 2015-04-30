package com.rowansenior.storegps;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;

/**
 * Created by Engineering on 4/28/15.
 */
public class UserLocation {

    Context context;
    List<Address> locationList;
    Location uLocate = new Location("");
    Location dLocate = new Location("");
    Address dAddress;
    double userLong;
    double userLat;

    public UserLocation(Context context) throws IOException {
        this.context = context;
        onCreate();
    }

    public void onCreate() throws IOException {
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        LocationListener ll = new myLocationListener();
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, ll);

        //dAddress = locationList.get(0);
    }

    private class myLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                System.out.println(location);
                userLong = location.getLongitude();
                userLat = location.getLatitude();
                System.out.println("Lat: " + userLat + "Long: " +userLong);

                uLocate.setLatitude(userLat);
                uLocate.setLongitude(userLong);

                try {
                    geoLocate(uLocate);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                dLocate.setLatitude(40.426671/*dAddress.getLatitude()*/);
                dLocate.setLongitude(-74.430390/*dAddress.getLongitude()*/);
                System.out.println(distances(uLocate, dLocate) + " Miles");
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

    public void geoLocate(Location location) throws IOException
    {
        Geocoder gc = new Geocoder(context);
        locationList = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
    }

    public Double distances(Location uLocation, Location dLocation)
    {
        double mtDistance = uLocation.distanceTo(dLocation);
        double mlDistance = mtDistance * 0.000621371;
        return mlDistance;
    }
}
