package com.rowansenior.storegps;

import android.app.Activity;
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
public class DialogNewList extends DialogFragment implements View.OnClickListener {
    Context mContext;
    Button cancel;
    Button accept;
    EditText listName;

    public DialogNewList() {
        mContext = getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_new_list, container, false);
        listName = (EditText) v.findViewById(R.id.txt_name);
        cancel = (Button) v.findViewById(R.id.btn_cancel);
        accept = (Button) v.findViewById(R.id.btn_accept);

        getDialog().setTitle("Create A New List");
        cancel.setOnClickListener(this);
        accept.setOnClickListener(this);

        return v;
    }

    public void onClick(View v) {
        CharSequence text = "List already exists!";
        int duration = Toast.LENGTH_SHORT;
            switch (v.getId()) {
            case R.id.btn_accept:
                Editable name = listName.getText();
                DatabaseHelper db = new DatabaseHelper(getActivity());
                try {
                    db.createNewList(name.toString(), 2, 1);
                }
                catch(Exception e){
                    Toast toast = Toast.makeText(getActivity(), text, duration);
                    toast.show();
                }
                getDialog().dismiss();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, new MyListFragment().newInstance()).addToBackStack(null).commit();
                break;
            case R.id.btn_cancel:
                getDialog().dismiss();
                break;
        }
    }
}