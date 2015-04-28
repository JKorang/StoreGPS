package com.rowansenior.storegps;

import java.util.ArrayList;

/**
 * Created by Joseph on 3/30/2015.
 */
public class ShoppingListItem {
    private String vItemName;
    private int vQuantity;
    private int vIfFound;

    public ShoppingListItem(String vItemName, int vQuantity, int vIfFound) {
        super();
        this.vItemName = vItemName;
        this.vQuantity = vQuantity;
        this.vIfFound = vIfFound;
    }

    public ShoppingListItem() {
        super();
        this.vItemName = "test";
        this.vQuantity = 1;
        this.vIfFound = 0;
    }

    public String getName() {
        return vItemName;
    }

    public int getQuantity() {
        return vQuantity;
    }

    public int getFound() {
        return vIfFound;
    }

    @Override
    public String toString() {
        return "tbd";
    }
}

