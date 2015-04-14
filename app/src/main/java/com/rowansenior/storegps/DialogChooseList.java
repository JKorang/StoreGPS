package com.rowansenior.storegps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    Button cancel;
    Button accept;
    EditText listName;
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
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listOfNames);
        ListView listView = (ListView) v.findViewById(R.id.choose_list_view);
        System.out.println(listView);
        listView.setAdapter(itemsAdapter);


        Button cancel = (Button)v.findViewById(R.id.cancel_choose_list);
        cancel.setOnClickListener(this);

        return v;
    }

    public void generateListOfNames(ArrayList tempList) {
        listOfNames = new ArrayList<String>();
        for (int i = 0; i < tempList.size(); i++) {
            ShoppingList temp = (ShoppingList) tempList.get(i);
            for (int j = 0; j < 1; j++) {
                listOfNames.add(temp.getName());
            }
        }
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_accept:
                break;
            case R.id.cancel_choose_list:
                getDialog().dismiss();
                break;
        }
    }
}