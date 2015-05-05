package com.rowansenior.storegps;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

/**
 * Created by root on 3/31/15.
 */
public class DialogSearchResults extends DialogFragment implements View.OnClickListener {
    Context mContext;
    String storeName;
    String itemName;
    Button cancel;
    Button addToList;

    public DialogSearchResults(String store, String item) {
        mContext = getActivity();
        storeName = store;
        itemName = item;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_searched_item, container, false);
        cancel = (Button) v.findViewById(R.id.addToListFromSearch);
        addToList = (Button) v.findViewById(R.id.closeSearchResults);

        cancel.setOnClickListener(this);
        addToList.setOnClickListener(this);
        return v;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            //Create a new list
            case R.id.addToListFromSearch:
                break;

            //Cancel list creation.
            case R.id.closeSearchResults:
                getDialog().dismiss();
                break;
        }
    }
}