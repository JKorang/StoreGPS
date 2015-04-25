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
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
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

import java.lang.reflect.Field;
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
        setBackgroundColor(viewHolder, item.getColor());
        setListIcon(viewHolder, item.getIcon());
    }

    public void setListIcon(ViewHolder viewHolder, int logoInt) {
        ImageView imgVw = viewHolder.vImageView;
        Drawable logoUsed;
        switch (logoInt) {
            case 0:
                logoUsed = context.getResources().getDrawable(R.drawable.applications);
                break;
            case 1:
                logoUsed = context.getResources().getDrawable(R.drawable.computer);
                break;
            case 2:
                logoUsed = context.getResources().getDrawable(R.drawable.coffee_badge);
                break;
            case 3:
                logoUsed = context.getResources().getDrawable(R.drawable.music);
                break;
            case 4:
                logoUsed = context.getResources().getDrawable(R.drawable.pictures);
                break;
            case 5:
                logoUsed = context.getResources().getDrawable(R.drawable.toolbox_badge);
                break;
            default:
                logoUsed = context.getResources().getDrawable(R.drawable.favorites);
                break;
        }
        imgVw.setBackgroundDrawable(logoUsed);
    }

    public void setBackgroundColor(ViewHolder viewHolder, int colorInt) {
        String colorHex;
        switch (colorInt) {
            case 0:
                colorHex = context.getString(R.string.color_0);
                break;
            case 1:
                colorHex = context.getString(R.string.color_1);
                break;
            case 2:
                colorHex = context.getString(R.string.color_2);
                break;
            case 3:
                colorHex = context.getString(R.string.color_3);
                break;
            case 4:
                colorHex = context.getString(R.string.color_4);
                break;
            case 5:
                colorHex = context.getString(R.string.color_5);
                break;
            case 6:
                colorHex = context.getString(R.string.color_6);
                break;
            default:
                colorHex = context.getString(R.string.color_7);
                break;
        }
        viewHolder.vhCard.setCardBackgroundColor(Color.parseColor(colorHex));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    /**
     * Capable of holding each item in the LLA.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ContextMenuInfo, View.OnLongClickListener {
        private TextView vTitleText;
        private TextView vDate;
        private CardView vCardView;
        private ImageView vImageView;
        private FragmentManager vFM;
        private Context vhContext;
        private CardView vhCard;

        public ViewHolder(View v, FragmentManager FM, Context context) {
            super(v);

            vhContext = context;
            vTitleText = (TextView) v.findViewById(R.id.titleText);
            vDate = (TextView) v.findViewById(R.id.date);
            vCardView = (CardView) v.findViewById(R.id.card_view);
            vImageView = (ImageView) v.findViewById(R.id.listIcon);
            vhCard = (CardView) v.findViewById(R.id.card_view);

            vFM = FM;
            vTitleText.setOnClickListener(this);
            vDate.setOnClickListener(this);
            vCardView.setOnClickListener(this);
            vImageView.setOnClickListener(this);

            vImageView.setOnLongClickListener(this);

        }

        /**
         * on a click open the shopping list window and allow for list editing
         */
        public void onClick(View v) {
            String newFrag = vTitleText.getText().toString();
            vFM.beginTransaction().replace(R.id.container, new SingleListFragment().newInstance(newFrag)).addToBackStack(null).commit();
        }

        /**
         * On a long press prompt a window to confirm deletion, if the confirmed the item is deleted
         * and the store list is refreshed
         *
         * @param v
         * @return true
         */
        @Override
        public boolean onLongClick(View v) {


            final AlertDialog alert = new AlertDialog.Builder(vhContext).create();
            alert.setTitle("Alert Box");
            alert.setMessage("Are you sure you want to delete?");
            alert.setButton(Dialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    DatabaseHelper db = new DatabaseHelper(vhContext);
                    db.removeList(vTitleText.getText().toString());
                    vFM.beginTransaction().replace(R.id.container, new MyListFragment().newInstance()).addToBackStack(null).commit();
                }
            });
            alert.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alert.show();

            return true;
        }
    }
}