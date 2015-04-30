package com.rowansenior.storegps;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
    String mOrigin;

    RadioGroup.OnCheckedChangeListener radioGroupOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {

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

    public DialogNewList(String origin) {
        mOrigin = origin;
        mContext = getActivity();
    }

    //Overriden to hide the title bar inside of the dialog.
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        Dialog dialog = super.onCreateDialog(savedInstance);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_new_list, container, false);
        listName = (EditText) v.findViewById(R.id.txt_name);
        cancel = (Button) v.findViewById(R.id.btn_cancel);
        accept = (Button) v.findViewById(R.id.btn_accept);
        colorGroup = (RadioGroup) v.findViewById(R.id.colorGroup);
        imageGroup = (RadioGroup) v.findViewById(R.id.imageGroup);

        cancel.setOnClickListener(this);
        accept.setOnClickListener(this);
        colorGroup.setOnCheckedChangeListener(radioGroupOnCheckedChangeListener);
        imageGroup.setOnCheckedChangeListener(radioGroupOnCheckedChangeListener);

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
                    CharSequence text = "Please enter a name for the list";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), text, duration);
                    toast.show();
                    break;
                }

                //Ensure that a color has been chosen.
                if (colorChosen == null) {
                    CharSequence text = "Please choose a color";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), text, duration);
                    toast.show();
                    break;
                }

                //Ensure that an image has been chosen.
                if (imageChosen == null) {
                    CharSequence text = "Please choose an icon";
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
                    CharSequence text = "List Exists: Please enter a unique name";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), text, duration);
                    toast.show();
                }
                getDialog().dismiss();
                FragmentManager fragmentManager = getFragmentManager();

                //Evaluate the origin to refresh its page.
                switch (mOrigin) {
                    case "homePage":
                        fragmentManager.beginTransaction().replace(R.id.container, new HomeFragment().newInstance()).addToBackStack(null).commit();
                        break;
                    case "myLists" :
                        fragmentManager.beginTransaction().replace(R.id.container, new MyListFragment().newInstance()).addToBackStack(null).commit();
                        break;
                    default:
                        fragmentManager.beginTransaction().replace(R.id.container, new MyListFragment().newInstance()).addToBackStack(null).commit();
                        break;
                }
                break;

            //Cancel list creation.
            case R.id.btn_cancel:
                getDialog().dismiss();
                break;
        }
    }
}