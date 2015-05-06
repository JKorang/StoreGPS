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
import android.widget.EditText;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by root on 3/31/15.
 */
public class DialogSingleItemSearch extends DialogFragment implements View.OnClickListener {
    Context mContext;
    Button cancel;
    Button search;
    EditText searchBox;
    String storeName;

    public DialogSingleItemSearch(String store, Context context) {
        mContext = context;
        storeName = store;
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
        FragmentManager fragmentManager = getFragmentManager();
        switch (v.getId()) {
            case R.id.searchButton:
                Editable tempTerm = searchBox.getText();
                String searchTerm = tempTerm.toString();

                    if (searchTerm.trim().length() == 0) {
                        SnackbarManager.show(Snackbar.with(getActivity()).text("Please enter a search term"));
                    } else {
                        DialogSearchResults diagSI = new DialogSearchResults(storeName, searchTerm, mContext);
                        diagSI.show(fragmentManager, null);
                    }
                break;
            case R.id.searchButtonCancel:
                getDialog().dismiss();
                break;
        }
    }
}