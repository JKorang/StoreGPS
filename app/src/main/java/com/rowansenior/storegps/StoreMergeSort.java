package com.rowansenior.storegps;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Engineering on 5/2/15.
 */
public class StoreMergeSort {
    Context context;

    public StoreMergeSort(Context context)
    {
        this.context = context;
    }

    public ArrayList<Store> mergeSort(ArrayList<Store> myStores) throws IOException {
        ArrayList<Store> leftHand = new ArrayList<>();
        ArrayList<Store> rightHand = new ArrayList<>();
        int mid;

        if(myStores.size() <= 1)
        {
            return myStores;
        }
        else
        {
            mid = myStores.size()/2;

            for(int i = 0; i < mid; i++)
            {
                leftHand.add(myStores.get(i));
            }
            for(int i = mid; i < myStores.size(); i++)
            {
                rightHand.add(myStores.get(i));
            }

            rightHand = mergeSort(rightHand);
            leftHand = mergeSort(leftHand);

            merge(leftHand, rightHand, myStores);
        }
        return myStores;
    }

    private ArrayList<Store> merge(ArrayList<Store> left, ArrayList<Store> right, ArrayList<Store> myStores) throws IOException {
        int leftNDX = 0;
        int rightNDX = 0;
        int storeNDX = 0;

        while(leftNDX < left.size() && rightNDX < right.size())
        {
            String leftAddress = left.get(leftNDX).getLocation();
            String rightAddress = right.get(rightNDX).getLocation();
            UserLocation ul = new UserLocation(context, leftAddress);
            UserLocation tempUL = new UserLocation(context, rightAddress);
            Double locate1 = ul.getDistances(ul.getUserLocation(), ul.getDestinationLocation()); //left
            Double locate2 = tempUL.getDistances(tempUL.getUserLocation(), tempUL.getDestinationLocation()); //right

            if(locate1.compareTo(locate2) < 0)
            {
                myStores.set(storeNDX, left.get(leftNDX));
                leftNDX++;
            }
            else
            {
                myStores.set(storeNDX, right.get(rightNDX));
                rightNDX++;
            }
            storeNDX++;
        }

        ArrayList<Store> extra;
        int extraNDX;
        if(leftNDX >= left.size())
        {
            extra = right;
            extraNDX = rightNDX;
        }
        else
        {
            extra = left;
            extraNDX = leftNDX;
        }

        for(int i =extraNDX; i < extra.size(); i++)
        {
            myStores.set(storeNDX, extra.get(i));
            storeNDX++;
        }

        return myStores;
    }
}
