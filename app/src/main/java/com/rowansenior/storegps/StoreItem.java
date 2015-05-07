package com.rowansenior.storegps;

/**
 * Created by root on 4/6/15.
 */


public class StoreItem {
    private String vName;
    private String vLocation;
    private String vTag;
    private String vStore;
    private String vPrice;

    public String getvCategory() {
        return vCategory;
    }

    public void setvCategory(String vCategory) {
        this.vCategory = vCategory;
    }

    private String vCategory;

    public StoreItem(String name, String location, String tag, String store, String price, String category) {
        this.vName = name;
        this.vLocation = location;
        this.vTag = tag;
        this.vStore = store;
        this.vPrice = price;
        this.vCategory = category;
    }

    public String getvPrice() {
        return vPrice;
    }

    public void setvPrice(String vPrice) {
        this.vPrice = vPrice;
    }

    public String getvStore() {
        return vStore;
    }

    public void setvStore(String vStore) {
        this.vStore = vStore;
    }

    public String getvTag() {
        return vTag;
    }

    public void setvTag(String vTag) {
        this.vTag = vTag;
    }

    public String getvLocation() {
        return vLocation;
    }

    public void setvLocation(String vLocation) {
        this.vLocation = vLocation;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }
}
