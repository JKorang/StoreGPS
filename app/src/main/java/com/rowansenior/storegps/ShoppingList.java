package com.rowansenior.storegps;

import java.util.ArrayList;

/**
 * Created by root on 3/29/15.
 */
public class ShoppingList {
    private String vName;
    private int vDate;
    private int vIcon;
    private int vColor;
    private boolean vActive;
    private ArrayList vItems;

    public ShoppingList() {
    }

    public ShoppingList(String vName, int vDate, int vIcon, int vColor, boolean vActive, ArrayList vItems) {
        super();
        this.vName = vName;
        this.vDate = vDate;
        this.vIcon = vIcon;
        this.vColor = vColor;
        this.vActive = vActive;
        this.vItems = vItems;
    }

    @Override
    public String toString(){
        return "tbd";
    }
}
