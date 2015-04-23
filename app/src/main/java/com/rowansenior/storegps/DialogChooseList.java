package com.rowansenior.storegps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 3/31/15.
 */
public class DialogChooseList extends DialogFragment implements View.OnClickListener {
    Context mContext;
    ArrayList<String> listOfNames;

    public DialogChooseList() {
        mContext = getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_choose_list, container, false);
        getDialog().setTitle("Choose A List To Navigate");
        DatabaseHelper db = new DatabaseHelper(getActivity());
        ArrayList list = db.getAllLists();
        generateListOfNames(list);
        final ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listOfNames);
        ListView listView = (ListView) v.findViewById(R.id.choose_list_view);
        System.out.println(listView);
        final FragmentManager fm = (getActivity()).getSupportFragmentManager();
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fm.beginTransaction().replace(R.id.container, new SingleListFragment().newInstance(itemsAdapter.getItem(position), true)).addToBackStack(null).commit();
                getDialog().dismiss();
            }
        });

        Button cancel = (Button)v.findViewById(R.id.cancel_choose_list);
        cancel.setOnClickListener(this);

        return v;
    }

    public void generateListOfNames(ArrayList tempList) {
        listOfNames = new ArrayList<String>();
        for (int i = 0; i < tempList.size(); i++) {
            ShoppingList temp = (ShoppingList) tempList.get(i);
            listOfNames.add(temp.getName());
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