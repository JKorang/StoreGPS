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
public class DialogNewList extends DialogFragment implements View.OnClickListener {
    Context mContext;
    Button cancel;
    Button accept;
    EditText listName;

    private OnNameSettedCallback callback;

    public interface OnNameSettedCallback {
        void onNameSetted(String newName);
    }

    public DialogNewList() {
        mContext = getActivity();
    }

    static DialogNewList newInstance() {
        return new DialogNewList();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_new_list, container, false);
        listName = (EditText) v.findViewById(R.id.txt_name);
        cancel = (Button) v.findViewById(R.id.btn_cancel);
        accept = (Button) v.findViewById(R.id.btn_accept);

        getDialog().setTitle("Create A New List");
        cancel.setOnClickListener((View.OnClickListener) this);
        accept.setOnClickListener((View.OnClickListener) this);

        return v;
    }

    public void onClick(View v) {
            switch (v.getId()) {
            case R.id.btn_accept:
                Editable name = listName.getText();
                DatabaseHelper db = new DatabaseHelper(getActivity());
                db.createNewList(name.toString(), 2, 1);
                getDialog().dismiss();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, new MyListFragment().newInstance()).commit();
                break;
            case R.id.btn_cancel:
                getDialog().dismiss();
                break;
        }
    }
}