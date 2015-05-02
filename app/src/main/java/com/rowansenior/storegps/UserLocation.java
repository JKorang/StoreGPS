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
import java.util.Locale;

/**
 * Created by Engineering on 4/28/15.
 */
public class UserLocation {

    Context context;
    List<Address> locationList;
    Location uLocate = new Location("");
    Location dLocate = new Location("");
    Location tempULocate = new Location("");
    Address dAddress;
    String strAddress;
    double userLong;
    double userLat;

    public UserLocation(Context context) throws IOException {
        this.context = context;
        onCreate();
    }

    public UserLocation(Context context, String address) throws IOException
    {
        this.context = context;
        strAddress = address;
        onCreate();
    }

    public void onCreate() throws IOException {
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        uLocate = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        //LocationListener ll = new myLocationListener();
        //lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 150, ll);
        while(uLocate == tempULocate)
        {
            LocationListener ll = new myLocationListener();
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, ll);
        }
        tempULocate = uLocate;
        geoLocate();

        //dAddress = locationList.get(0);
    }

    private class myLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                userLong = location.getLongitude();
                userLat = location.getLatitude();

                uLocate.setLatitude(userLat);
                uLocate.setLongitude(userLong);

                //dLocate.setLatitude(40.426671/*dAddress.getLatitude()*/);
                //dLocate.setLongitude(-74.430390/*dAddress.getLongitude()*/);
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

    public Location getUserLocation()
    {
        return uLocate;
    }

    public Location getDestinationLocation()
    {
        return dLocate;
    }

    public void geoLocate() throws IOException
    {
        Geocoder gc = new Geocoder(context);
        locationList = gc.getFromLocationName(strAddress, 1);

        dAddress = locationList.get(0);
        dLocate.setLatitude(dAddress.getLatitude());
        dLocate.setLongitude(dAddress.getLongitude());
    }

    public Double getDistances(Location uLocation, Location dLocation)
    {
        double mtDistance = uLocation.distanceTo(dLocation);
        double mlDistance = mtDistance * 0.000621371;
        return mlDistance;
    }
}
