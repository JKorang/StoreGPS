package com.rowansenior.storegps;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by root on 3/31/15.
 */
public class DialogChooseStore extends DialogFragment implements View.OnClickListener {
    Context mContext;
    ArrayList<String> listOfStores;
    String mOrigin;

    public DialogChooseStore(String origin) {
        mOrigin = origin;
        mContext = getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_choose_list, container, false);

        //Build dialog
        getDialog().setTitle("Navigate A Store");
        final DatabaseHelper db = new DatabaseHelper(getActivity());
        ArrayList<Store> favList = db.getAllStores();
        ArrayList<Store> nearList = db.getNearbyStores();
        try {
            generateListOfStores(favList, nearList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final ArrayAdapter<String> storeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listOfStores);
        ListView listView = (ListView) v.findViewById(R.id.choose_list_view);
        final FragmentManager fm = (getActivity()).getSupportFragmentManager();
        listView.setAdapter(storeAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Store store = db.getStoreInfo(listOfStores.get(position));
                fm.beginTransaction().replace(R.id.container, new SingleListFragment().newInstance(mOrigin, true, store)).addToBackStack(null).commit();
                getDialog().dismiss();
            }
        });

        Button cancel = (Button) v.findViewById(R.id.cancel_choose_list);
        cancel.setOnClickListener(this);

        return v;
    }

    public void generateListOfStores(ArrayList<Store> tempFavList, ArrayList<Store> tempNearList) throws IOException {
        ArrayList<Store> allStores = new ArrayList<>();
        for(int i = 0; i < tempFavList.size(); i++)
        {
            allStores.add(tempFavList.get(i));
        }
        for(int i = 0; i < tempNearList.size(); i++)
        {
            if(!allStores.contains(tempNearList.get(i)))
            {
                allStores.add(tempNearList.get(i));
            }
        }
        StoreMergeSort sms = new StoreMergeSort(getActivity(), false);
        sms.mergeSort(allStores);
        listOfStores = new ArrayList<String>();
        for (int i = 0; i < allStores.size(); i++) {
            Store temp = allStores.get(i);
            listOfStores.add(temp.getName());
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_choose_list:
                getDialog().dismiss();
                break;
        }
    }
}