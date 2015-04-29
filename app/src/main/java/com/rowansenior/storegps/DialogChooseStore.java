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
        getDialog().setTitle("Choose a Store to Navigate");
        DatabaseHelper db = new DatabaseHelper(getActivity());
        ArrayList list = db.getAllStores();
        generateListOfStores(list);
        final ArrayAdapter<String> storeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listOfStores);
        ListView listView = (ListView) v.findViewById(R.id.choose_list_view);
        final FragmentManager fm = (getActivity()).getSupportFragmentManager();
        listView.setAdapter(storeAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fm.beginTransaction().replace(R.id.container, new SingleListFragment().newInstance(mOrigin, true, null)).addToBackStack(null).commit();
                getDialog().dismiss();
            }
        });

        Button cancel = (Button) v.findViewById(R.id.cancel_choose_list);
        cancel.setOnClickListener(this);

        return v;
    }

    public void generateListOfStores(ArrayList tempList) {
        listOfStores = new ArrayList<String>();
        for (int i = 0; i < tempList.size(); i++) {
            Store temp = (Store) tempList.get(i);
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