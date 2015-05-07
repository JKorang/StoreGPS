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
import android.widget.Toast;

import java.util.ArrayList;

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
        ArrayList itemResults = null;
        switch (v.getId()) {
            case R.id.searchButton:
                Editable tempTerm = searchBox.getText();
                String searchTerm = tempTerm.toString();

                    if (searchTerm.trim().length() == 0) {
                        CharSequence searchText = "Please enter a search term";
                        int searchDuration = Toast.LENGTH_SHORT;
                        Toast foundToast = Toast.makeText(mContext, searchText, searchDuration);
                        foundToast.show();
                    } else {

                        try {
                            DatabaseHelper db = new DatabaseHelper(mContext);
                            itemResults = db.searchResult(storeName, searchTerm);
                        }
                        catch (Exception e) {

                        }
                        switch(itemResults.size()) {
                            case 0:
                                Toast noResults = Toast.makeText(mContext, "Sorry, no items found", Toast.LENGTH_SHORT);
                                noResults.show();
                                break;
                            case 1:
                                DialogSearchResults diagSI = new DialogSearchResults((StoreItem) itemResults.get(0), mContext);
                                diagSI.show(fragmentManager, null);
                                break;
                            default:
                                DialogChooseItem diagCI = new DialogChooseItem(itemResults, mContext);
                                diagCI.show(fragmentManager, null);
                                break;
                        }

                    }
                break;
            case R.id.searchButtonCancel:
                getDialog().dismiss();
                break;
        }
    }
}