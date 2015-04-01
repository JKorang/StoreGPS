package com.rowansenior.storegps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
public class DialogNewList extends DialogFragment {
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

        return v;
    }

    public void onClick(View v) {
        Editable name = listName.getText();
        if(!TextUtils.isEmpty(name)) {
            // Return the new entered name to the calling activity
            callback.onNameSetted(name.toString());
            this.dismiss();
        }
        else
            Toast.makeText(getActivity(), "lol no text entered", Toast.LENGTH_LONG).show();
    }
}