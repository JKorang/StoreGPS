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
        switch (v.getId()) {
            //Create a new list
            case R.id.btn_accept:
                Editable name = listName.getText();
                DatabaseHelper db = new DatabaseHelper(getActivity());

                //Ensure that the new list created has a name.
                //Trim to ensure that the name is not blank spaces.
                if (name.toString().trim().length() == 0) {
                    CharSequence text = "List must have a name!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), text, duration);
                    toast.show();
                    break;
                }

                //Add list to the database.
                try {
                    db.createNewList(name.toString(), 2, 1);
                }
                //Database addition failed. List exists.
                //Throw a loaf of bread.
                catch (Exception e) {
                    CharSequence text = "List already exists!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), text, duration);
                    toast.show();
                }
                getDialog().dismiss();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, new MyListFragment().newInstance()).addToBackStack(null).commit();
                break;

            //Cancel list creation.
            case R.id.btn_cancel:
                getDialog().dismiss();
                break;
        }
    }
}