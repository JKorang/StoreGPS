package com.rowansenior.storegps;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Engineering on 5/2/15.
 */
public class StoreMergeSort {
    Context context;
    Boolean distCalcd;

    public StoreMergeSort(Context context, Boolean jawn)
    {
        this.context = context;
        this.distCalcd = jawn;
    }

    public ArrayList<Store> mergeSort(ArrayList<Store> myStores) throws IOException {
        if(distCalcd == false) {
                calcDistances(myStores);
                distCalcd = true;
            }
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

            leftHand = mergeSort(leftHand);
            rightHand = mergeSort(rightHand);

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
            Double locate1 = left.get(leftNDX).getvDistanceTo(); //left
            Double locate2 = right.get(rightNDX).getvDistanceTo(); //right

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

    public ArrayList<Store> calcDistances(ArrayList<Store> aLS) {
        UserLocation ul;
        for(int i = 0; i < aLS.size(); i++) {
            try {
                ul = new UserLocation(context, aLS.get(i).getLocation());
                aLS.get(i).setvDistanceTo(ul.getDistances(ul.getUserLocation(), ul.getDestinationLocation()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return aLS;
    }
}
