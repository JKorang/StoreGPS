package com.rowansenior.storegps;

import java.util.ArrayList;

/**
 * Created by root on 4/6/15.
 */
public class Store {
    private String vName;
    private String vLocation;
    private String vURL;
    private int vHourOpen;
    private int vHourClosed;
    private int vPhoneNumber;


    public Store(String name, String location) {
      this.vName = name;       this.vLocation = location;
    }

    public Store(String name, String location, int phone, String url, int hourOpen, int hourClosed) {
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

}
