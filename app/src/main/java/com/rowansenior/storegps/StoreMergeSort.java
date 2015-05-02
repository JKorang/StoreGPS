package com.rowansenior.storegps;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Engineering on 5/2/15.
 */
public class StoreMergeSort {
    ArrayList<Store> sortStore = new ArrayList<>();
    ArrayList<Store> tempSortStore;
    Context context;
    int size;

    public StoreMergeSort(Context context)
    {
        this.context = context;
    }

    public void sort(ArrayList<Store> unsortedStores) throws IOException {
        sortStore = unsortedStores;
        size = unsortedStores.size();
        tempSortStore = new ArrayList<>(size);
        doMergeSort(0, size - 1);
    }

    private void doMergeSort(int lowNDX, int highNDX) throws IOException {

        if (lowNDX < highNDX) {
            int middle = lowNDX + (highNDX - lowNDX) / 2;
            // Below step sorts the left side of the array
            doMergeSort(lowNDX, middle);
            // Below step sorts the right side of the array
            doMergeSort(middle + 1, highNDX);
            // Now merge both sides
            mergeParts(lowNDX, middle, highNDX);
        }
    }

    private void mergeParts(int low, int middle, int high) throws IOException {

        for (int i = low; i <= high; i++) {
            Store temp = sortStore.get(i);
            tempSortStore.add(temp);
        }
        int i = low;
        int j = middle + 1;
        int k = low;
        while (i <= middle && j <= high) {
            String strAddress = tempSortStore.get(i).getLocation();
            String tempAddress = tempSortStore.get(j).getLocation();
            UserLocation ul = new UserLocation(context, strAddress);
            UserLocation tempUL = new UserLocation(context, tempAddress);
            Double locate1 = ul.getDistances(ul.getUserLocation(), ul.getDestinationLocation()); //i
            Double locate2 = tempUL.getDistances(tempUL.getUserLocation(), tempUL.getDestinationLocation()); //j
            if (locate1 <= locate2) {
                Store tempStore = tempSortStore.get(i);
                sortStore.add(tempStore);
                i++;
            } else {
                Store tempStore = tempSortStore.get(j);
                sortStore.add(tempStore);
                j++;
            }
            k++;
        }
        while (i <= middle) {
            Store tempStore = tempSortStore.get(i);
            sortStore.add(tempStore);
            k++;
            i++;
        }

    }
}
