package com.rowansenior.storegps;

/**
 * Created by Joseph on 3/27/2015.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import android.view.ContextMenu.ContextMenuInfo;


/**
 * ListListAdapter handles transmission and access of data from fragments associated with it.
 * In this case, LLA handles data access of IndividualListFragments and places the content
 * within views that the monitor is attached to.
 */
public class ListListAdapter extends RecyclerView.Adapter<ListListAdapter.ViewHolder> {

    public static FragmentManager fragmentManager;
    private Context context;
    private String originString;
    private List<ShoppingList> items;

    public ListListAdapter(Context context, List items, String originString) {
        this.items = items;
        this.context = context;
        this.originString = originString;
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
                logoUsed = context.getResources().getDrawable(R.drawable.icon_0);
                break;
            case 1:
                logoUsed = context.getResources().getDrawable(R.drawable.icon_1);
                break;
            case 2:
                logoUsed = context.getResources().getDrawable(R.drawable.icon_2);
                break;
            case 3:
                logoUsed = context.getResources().getDrawable(R.drawable.icon_3);
                break;
            case 4:
                logoUsed = context.getResources().getDrawable(R.drawable.icon_4);
                break;
            case 5:
                logoUsed = context.getResources().getDrawable(R.drawable.icon_5);
                break;
            case 6:
                logoUsed = context.getResources().getDrawable(R.drawable.icon_6);
                break;
            case 7:
                logoUsed = context.getResources().getDrawable(R.drawable.icon_7);
                break;
            case 8:
                logoUsed = context.getResources().getDrawable(R.drawable.icon_8);
                break;
            default:
                logoUsed = context.getResources().getDrawable(R.drawable.icon_0);
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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ContextMenuInfo, View.OnLongClickListener {
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
            alert.setMessage("Are you sure you want to delete?");
            alert.setButton(Dialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    DatabaseHelper db = new DatabaseHelper(vhContext);
                    db.removeList(vTitleText.getText().toString());

                    //Evaluate the origin of the list.
                    //If the origin for the Adapter is the home page, call 3 items.
                    //If the origin of the Adapter is the MyLists page, generate all items.
                    switch (originString) {
                        case "homePage":
                            items = db.getLast3Lists();
                            break;
                        case "myLists":
                            items = db.getAllLists();
                            break;
                    }
                    notifyDataSetChanged();
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