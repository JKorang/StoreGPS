package com.rowansenior.storegps;

import java.util.ArrayList;

/**
 * Created by root on 4/6/15.
 */
public class Store {
    private String vName;
    private String vLocation;

    public Store(String name, String location) {
        this.vName = name;
        this.vLocation = location;
    }

    public String getName() {
        return vName;
    }

    public String getLocation() {
        return vLocation;
    }

}
