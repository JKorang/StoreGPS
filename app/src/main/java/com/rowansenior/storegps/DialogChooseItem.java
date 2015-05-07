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
public class DialogChooseItem extends DialogFragment implements View.OnClickListener {
    Context mContext;
    ArrayList<StoreItem> listOfItems;
    String mOrigin;

    public DialogChooseItem(String origin, ArrayList itemList) {
        mOrigin = origin;
        mContext = getActivity();
        listOfItems = itemList;
    }

    public DialogChooseItem(ArrayList itemList, Context context) {
        mContext = context;
        listOfItems = itemList;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_choose_list, container, false);

        ArrayList listOfItemNames = buildItemNames(listOfItems);

        //Build dialog
        getDialog().setTitle("Search Results");
        final ArrayAdapter<StoreItem> searchResultsAdapter = new ArrayAdapter<StoreItem>(getActivity(), android.R.layout.simple_list_item_1, listOfItemNames);
        ListView listView = (ListView) v.findViewById(R.id.choose_list_view);
        final FragmentManager fm = (getActivity()).getSupportFragmentManager();
        listView.setAdapter(searchResultsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogSearchResults diagSI = new DialogSearchResults(listOfItems.get(position), mContext);
                diagSI.show(fm, null);
            }
        });

        Button cancel = (Button) v.findViewById(R.id.cancel_choose_list);
        cancel.setOnClickListener(this);

        return v;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_choose_list:
                getDialog().dismiss();
                break;
        }
    }

    public ArrayList<String> buildItemNames(ArrayList<StoreItem> siList) {
        ArrayList returnList = new ArrayList();
        for(int i = 0; i < siList.size(); i++) {
            returnList.add(siList.get(i).getvName());
        }
        return returnList;
    }
}