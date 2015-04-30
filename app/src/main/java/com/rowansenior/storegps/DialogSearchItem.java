package com.rowansenior.storegps;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

/**
 * Created by root on 3/31/15.
 */
public class DialogSearchItem extends DialogFragment implements View.OnClickListener {
    Context mContext;

    RadioGroup.OnCheckedChangeListener radioGroupOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

        }
    };

    public DialogSearchItem() {
        mContext = getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_searched_item, container, false);
        return v;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            //Create a new list
            case R.id.btn_accept:
                break;

            //Cancel list creation.
            case R.id.btn_cancel:
                getDialog().dismiss();
                break;
        }
    }
}