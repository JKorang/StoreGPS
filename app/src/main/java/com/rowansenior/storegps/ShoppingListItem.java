package com.rowansenior.storegps;

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

    public void setFound() {
        if (this.vIfFound == 1) {
            this.vIfFound = 0;
        }
        else {
            this.vIfFound = 1;
        }
    }

    public void increaseQuantity(){
        this.vQuantity++;
    }

    public void decreaseQuantity() {
        this.vQuantity--;
    }

    public int getFound() {
        return vIfFound;
    }

}

