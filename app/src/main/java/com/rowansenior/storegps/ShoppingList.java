package com.rowansenior.storegps;


import java.util.ArrayList;

/**
 * Created by root on 3/29/15.
 */
public class ShoppingList {
    private String vName;
    private String vDate;
    private int vIcon;
    private int vColor;
    private int vActive;
    private ArrayList vItems;

    public ShoppingList(String vName, String vDate, int vIcon, int vColor, int vActive, ArrayList vItems) {
        super();
        this.vName = vName;
        this.vDate = vDate;
        this.vIcon = vIcon;
        this.vColor = vColor;
        this.vActive = vActive;
        this.vItems = vItems;
    }

    public String getName() {
        return vName;
    }

    public String getDate() {
        return vDate;
    }

    public int getColor() {
        return vColor;
    }

    public int getIcon() {
        return vIcon;
    }

    @Override
    public String toString() {
        return "tbd";
    }


}
