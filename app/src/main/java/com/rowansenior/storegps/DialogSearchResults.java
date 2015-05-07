package com.rowansenior.storegps;

import android.app.Fragment;
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
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by root on 3/31/15.
 */
public class DialogSearchResults extends DialogFragment implements View.OnClickListener {
    Context mContext;
    String storeName;
    String itemName;
    Button cancel;
    Button addToList;
    ArrayList itemResults;
    StoreItem mItem;
    TextView tvItemName;
    TextView tvPrice;
    TextView tvCategory;
    TextView tvAisle;
    TextView tvStore;

    public DialogSearchResults(String store, String item, Context context) {
        mContext = context;
        storeName = store;
        itemName = item;
        generateResult();
    }

    public DialogSearchResults(StoreItem item, Context context) {
        mContext = context;
        mItem = item;
    }

    private void generateResult() {
        DatabaseHelper db = new DatabaseHelper(mContext);
        itemResults = db.searchResult(storeName, itemName);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_searched_item, container, false);
        cancel = (Button) v.findViewById(R.id.addToListFromSearch);
        addToList = (Button) v.findViewById(R.id.closeSearchResults);


        tvItemName = (TextView) v.findViewById(R.id.searchItemName);
        tvPrice = (TextView) v.findViewById(R.id.searchPrice);
        tvCategory = (TextView) v.findViewById(R.id.searchCategory);
        tvAisle = (TextView) v.findViewById(R.id.searchAisle);
        tvStore = (TextView) v.findViewById(R.id.searchStore);


        tvItemName.setText(mItem.getvName());
        tvPrice.setText("Price: $" + mItem.getvPrice());
        tvCategory.setText("Category: " + mItem.getvCategory());
        tvAisle.setText("Location: " + mItem.getvLocation());
        tvStore.setText("Store: " + mItem.getvStore());


        cancel.setOnClickListener(this);
        addToList.setOnClickListener(this);
        return v;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            //Create a new list
            case R.id.addToListFromSearch:
                DialogChooseList diagCL = new DialogChooseList(mItem);
                FragmentManager fm = (getActivity()).getSupportFragmentManager();
                diagCL.show(fm, null);
                break;

            //Cancel list creation.
            case R.id.closeSearchResults:
                getDialog().dismiss();
                break;
        }
    }
}