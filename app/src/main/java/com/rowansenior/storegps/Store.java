package com.rowansenior.storegps;

/**
 * Created by root on 4/6/15.
 */
public class Store {
    private String vName;
    private String vLocation;
    private String vURL;
    private int vHourOpen;
    private int vHourClosed;
    private String vPhoneNumber;
    private double vDistanceTo;


    public Store(String name, String location) {
        this.vName = name;
        this.vLocation = location;
    }

    public Store(String name, String location, String phone, String url, int hourOpen, int hourClosed) {
        this.vName = name;
        this.vLocation = location;
        this.vURL = url;
        this.vHourOpen = hourOpen;
        this.vHourClosed = hourClosed;
        this.vPhoneNumber = phone;
    }


    public String getName() {
        return vName;
    }

    public String getLocation() {
        return vLocation;
    }

    public String getURL() {
        return vURL;
    }

    public int getHoursOpen() {
        return vHourOpen;
    }

    public int getHoursClosed() {
        return vHourClosed;
    }

    public String getPhoneNumber() {
        return vPhoneNumber;
    }

    public double getvDistanceTo() {
        return vDistanceTo;
    }

    public void setvDistanceTo(double dist) {
        this.vDistanceTo = dist;
    }

}
