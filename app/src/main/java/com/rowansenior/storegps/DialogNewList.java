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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by root on 3/31/15.
 */
public class DialogNewList extends DialogFragment implements View.OnClickListener {
    Context mContext;
    Button cancel;
    Button accept;
    EditText listName;
    RadioGroup colorGroup;
    RadioGroup imageGroup;
    Integer colorChosen;
    Integer imageChosen;

    public DialogNewList() {
        mContext = getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_new_list, container, false);
        listName = (EditText) v.findViewById(R.id.txt_name);
        cancel = (Button) v.findViewById(R.id.btn_cancel);
        accept = (Button) v.findViewById(R.id.btn_accept);
        colorGroup = (RadioGroup) v.findViewById(R.id.colorGroup);
        imageGroup = (RadioGroup) v.findViewById(R.id.imageGroup);

        getDialog().setTitle("Create A New List");
        cancel.setOnClickListener(this);
        accept.setOnClickListener(this);
        colorGroup.setOnCheckedChangeListener(radioGroupOnCheckedChangeListener);
        imageGroup.setOnCheckedChangeListener(radioGroupOnCheckedChangeListener);

        return v;
    }

    RadioGroup.OnCheckedChangeListener radioGroupOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            //Get the color from the
            switch (group.getId()) {
                case R.id.colorGroup:
                    RadioButton clr = (RadioButton) group.findViewById(checkedId);
                    colorChosen = group.indexOfChild(clr);
                    break;

                case R.id.imageGroup:
                    RadioButton img = (RadioButton) group.findViewById(checkedId);
                    imageChosen = group.indexOfChild(img);
                    break;
            }
        }
    };

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

                //Ensure that a color has been chosen.
                if(colorChosen == null) {
                    CharSequence text = "Please choose a color.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), text, duration);
                    toast.show();
                    break;
                }

                //Ensure that an image has been chosen.
                if(imageChosen == null) {
                    CharSequence text = "Please choose an icon.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), text, duration);
                    toast.show();
                    break;
                }

                //Add list to the database.
                try {
                    db.createNewList(name.toString(), colorChosen, imageChosen);
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