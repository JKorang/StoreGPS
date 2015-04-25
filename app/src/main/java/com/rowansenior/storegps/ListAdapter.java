package com.rowansenior.storegps;

/**
 * Created by Joseph on 3/27/2015.
 */

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * ListAdapter handles transmission and access of data from fragments associated with it.
 * In this case, LLA handles data access of ShoppingListItem and places the content
 * within views that the monitor is attached to.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private Context adapterContext;
    public List<ShoppingListItem> items;
    private String vhListName;
    public static FragmentManager fragmentManager;
    public static boolean isNavigated;

    public ListAdapter(Context context, List items, String listName, boolean isNav) {
        this.items = items;
        this.adapterContext = context;
        this.vhListName = listName;
        this.isNavigated = isNav;
        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(v, fragmentManager, vhListName, adapterContext, isNavigated);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ShoppingListItem item = items.get(i);
        viewHolder.vTitleText.setText(item.getName());
        viewHolder.vQuantityInt = item.getQuantity();
        viewHolder.vQuantity.setText("Quantity: " + String.valueOf(viewHolder.vQuantityInt));
        viewHolder.thisItem = item;

        //If the list is generated from a navigation, set its location here.
        if (viewHolder.vIsNavigated == true) {
            viewHolder.vItemLocation.setText("Yeah this works?");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Object that holds the contents to generate a list view.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView vTitleText;
        public TextView vQuantity;
        public int vQuantityInt;
        public CardView vCardView;
        public FragmentManager vFM;
        public ImageButton vIncQuantity;
        public ImageButton vDecQuantity;
        public ImageButton vFoundItem;
        public ImageButton vDeleteItem;
        public String parentList;
        public Context vhContext;
        public TextView vItemLocation;
        public boolean vIsNavigated;
        public ShoppingListItem thisItem;

        public ViewHolder(View v, FragmentManager FM, String listName, Context context, boolean isNav) {
            super(v);

            vTitleText = (TextView) v.findViewById(R.id.itemText);
            vQuantity = (TextView) v.findViewById(R.id.QuantityText);
            vItemLocation = (TextView) v.findViewById(R.id.locationText);
            vCardView = (CardView) v.findViewById(R.id.itemcardview);
            vIncQuantity = (ImageButton) v.findViewById(R.id.incQuantity);
            vDecQuantity = (ImageButton) v.findViewById(R.id.decQuantity);
            vFoundItem = (ImageButton) v.findViewById(R.id.foundItem);
            vDeleteItem = (ImageButton) v.findViewById(R.id.deleteItem);

            vhContext = context;
            parentList = listName;
            vFM = FM;
            vIsNavigated = isNav;
            vTitleText.setOnClickListener(this);
            vQuantity.setOnClickListener(this);
            vCardView.setOnClickListener(this);
            vIncQuantity.setOnClickListener(this);
            vDecQuantity.setOnClickListener(this);
            vFoundItem.setOnClickListener(this);
            vDeleteItem.setOnClickListener(this);
        }


        public void onClick(View v) {
            DatabaseHelper db = new DatabaseHelper(vhContext);
            switch (v.getId()) {
                //Increase quantity of an item
                //Writes to the database, increase local counter and reset textView
                case R.id.incQuantity:
                    db.increaseQuantity(parentList, vTitleText.getText().toString());
                    vQuantityInt++;
                    vQuantity.setText("Quantity: " + vQuantityInt);
                    break;

                //Decrease the quantity of an item
                //Verification performed, then decreased in the database.
                //Decrease local counter and reset textView.
                case R.id.decQuantity:
                    //Ensure that quantity can not drop below 1.
                    if (vQuantityInt < 2) {
                        CharSequence text = "Quantity must be at least 1!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(vhContext, text, duration);
                        toast.show();
                    } else {
                        db.decreaseQuantity(parentList, vTitleText.getText().toString());
                        vQuantityInt--;
                        vQuantity.setText("Quantity: " + vQuantityInt);
                    }
                    break;

                //The user has located the item.
                //Set the value in the database, update the dataSet
                //TODO: Move item to the lower RecyclerView once established
                case R.id.foundItem:
                    db.itemFound(parentList, vTitleText.getText().toString());
                    CharSequence foundText = "Item Found!";
                    int foundDuration = Toast.LENGTH_SHORT;
                    Toast foundToast = Toast.makeText(vhContext, foundText, foundDuration);
                    notifyDataSetChanged();
                    foundToast.show();
                    break;

                //Delete item from the list
                //Once item is removed from the database, remove from the List
                //Update data set
                case R.id.deleteItem:
                    db.removeItem(parentList, vTitleText.getText().toString());
                    items.remove(thisItem);
                    CharSequence delText = "Item Deleted!";
                    int delDuration = Toast.LENGTH_SHORT;
                    Toast delToast = Toast.makeText(vhContext, delText, delDuration);
                    delToast.show();
                    notifyDataSetChanged();
                    break;
            }
        }
    }
}