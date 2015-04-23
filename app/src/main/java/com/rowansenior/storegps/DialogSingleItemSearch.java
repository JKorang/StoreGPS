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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by root on 3/31/15.
 */
public class DialogSingleItemSearch extends DialogFragment implements View.OnClickListener {
    Context mContext;
    Button cancel;
    Button search;
    EditText searchBox;

    public DialogSingleItemSearch() {
        mContext = getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_single_item_search, container, false);
        searchBox = (EditText) v.findViewById(R.id.searchBox);
        cancel = (Button) v.findViewById(R.id.searchButtonCancel);
        search = (Button) v.findViewById(R.id.searchButton);

        getDialog().setTitle("Search This Store");
        cancel.setOnClickListener(this);
        search.setOnClickListener(this);

        return v;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchButton:
                break;
            case R.id.searchButtonCancel:
                getDialog().dismiss();
                break;
        }
    }
}