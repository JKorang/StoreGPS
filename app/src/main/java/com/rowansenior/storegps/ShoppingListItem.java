package com.rowansenior.storegps;

import java.util.ArrayList;

/**
 * Created by Joseph on 3/30/2015.
 */
public class ShoppingListItem {
    private String vItemName;
    private int vQuantity;
    private int vIfFound;
    private int vActive;

    public ShoppingListItem(String vItemName, int vQuantity, int vIfFound, int vActive) {
        super();
        this.vItemName = vItemName;
        this.vQuantity = vQuantity;
        this.vIfFound = vIfFound;
        this.vActive = vActive;
    }

    @Override
    public String toString(){
        return "tbd";
    }
}

