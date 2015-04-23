package com.rowansenior.storegps;

/**
 * Created by Joseph on 3/27/2015.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;

import static android.app.AlertDialog.*;

/**
 * ListListAdapter handles transmission and access of data from fragments associated with it.
 * In this case, LLA handles data access of IndividualListFragments and places the content
 * within views that the monitor is attached to.
 */
public class ListListAdapter extends RecyclerView.Adapter<ListListAdapter.ViewHolder> {

    private Context context;
    private List<ShoppingList> items;
    public static FragmentManager fragmentManager;


    public ListListAdapter(Context context, List items) {
        this.items = items;
        this.context = context;
        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_individual_list, viewGroup, false);
        ViewHolder vh = new ViewHolder(v, fragmentManager, context);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ShoppingList item = items.get(i);
        viewHolder.vTitleText.setText(item.getName());
        viewHolder.vDate.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    /**
     * Capable of holding each item in the LLA.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ContextMenuInfo, View.OnLongClickListener {
        public TextView vTitleText;
        public TextView vDate;
        public CardView vCardView;
        public ImageView vImageView;
        public FragmentManager vFM;
        public Context vhContext;

        public ViewHolder(View v, FragmentManager FM, Context context) {
            super(v);

            vhContext = context;
            vTitleText = (TextView) v.findViewById(R.id.titleText);
            vDate = (TextView) v.findViewById(R.id.date);
            vCardView = (CardView) v.findViewById(R.id.card_view);
            vImageView = (ImageView) v.findViewById(R.id.listIcon);
            vFM = FM;
            vTitleText.setOnClickListener(this);
            vDate.setOnClickListener(this);
            vCardView.setOnClickListener(this);
            vImageView.setOnClickListener(this);

            vImageView.setOnLongClickListener(this);

        }

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.titleText:
                case R.id.date:
                case R.id.card_view:
                case R.id.listIcon:
            }


            String newFrag = vTitleText.getText().toString();
            vFM.beginTransaction().replace(R.id.container, new SingleListFragment().newInstance(newFrag)).addToBackStack(null).commit();
        }

        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Context Menu");
            menu.add(0, v.getId(), 0, "Delete");
        }

        public boolean onContextItemSelected(MenuItem item) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            switch (item.getItemId()) {
                case R.id.deleteItem:

                    return true;
                default:
                    return false;
            }
        }

        @Override
        public boolean onLongClick(View v) {


           final AlertDialog alert = new AlertDialog.Builder(vhContext).create();
            alert.setTitle("Alert Box");
            alert.setMessage("Are you sure you want to delete?");
            alert.setButton(Dialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener(){
                       public void onClick(DialogInterface dialog, int which){
                           DatabaseHelper db = new DatabaseHelper(vhContext);
                           db.removeList(vTitleText.getText().toString());
                           vFM.beginTransaction().replace(R.id.container, new MyListFragment().newInstance()).addToBackStack(null).commit();
                       }
                    });
            alert.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){

                }
            });

            alert.show();

            return true;
        }
    }
}