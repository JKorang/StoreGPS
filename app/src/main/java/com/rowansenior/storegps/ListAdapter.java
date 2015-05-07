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

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import java.util.ArrayList;
import java.util.List;

/**
 * ListAdapter handles transmission and access of data from fragments associated with it.
 * In this case, LLA handles data access of ShoppingListItem and places the content
 * within views that the monitor is attached to.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    public static FragmentManager fragmentManager;
    public static boolean isNavigated;
    public List<ShoppingListItem> items;
    private Context adapterContext;
    private String vhListName;
    private Store storeNav;

    public ListAdapter(Context context, List items, String listName, boolean isNav) {
        this.items = items;
        this.adapterContext = context;
        this.vhListName = listName;
        this.isNavigated = isNav;
        this.storeNav = null;
        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
    }

    public ListAdapter(Context context, List items, String listName, boolean isNav, Store store) {
        this.items = items;
        this.adapterContext = context;
        this.vhListName = listName;
        this.isNavigated = isNav;
        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        this.storeNav = store;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(v, fragmentManager, vhListName, adapterContext, isNavigated, items.get(i).getFound());

        if (vh.vIsNavigated == true) {
            try {
                System.out.println(storeNav.getName());
                System.out.println(vh.vTitleText.getText().toString());
                DatabaseHelper db = DatabaseHelper.getHelper(adapterContext);
                ArrayList<StoreItem> alSI = db.navigateStore(storeNav.getName(), vh.vTitleText.getText().toString());
                System.out.println(alSI.get(0).getvLocation());
                vh.vItemLocation.setText("Location: " + alSI.get(0).getvLocation());
            }
            catch (Exception e) {
                vh.vItemLocation.setText("Not found in this store");
            }
        }

        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ShoppingListItem item = items.get(i);

        if (items.get(i).getFound() == 1) {
            viewHolder.vCardView.setCardBackgroundColor(adapterContext.getResources().getColor(R.color.tint_color));
        }
        else {
            viewHolder.vCardView.setCardBackgroundColor(adapterContext.getResources().getColor(R.color.white_color));
        }

        viewHolder.vTitleText.setText(item.getName());
        viewHolder.vQuantityInt = item.getQuantity();

        //Evaluate the state of the found status
        //If the item is considered found, change it's background color

        viewHolder.vQuantity.setText("Quantity: " + String.valueOf(viewHolder.vQuantityInt));
        viewHolder.thisItem = item;

        //If the list is generated from a navigation, set its location here.
        if (viewHolder.vIsNavigated == true) {
            try {
                System.out.println(storeNav.getName());
                System.out.println(viewHolder.vTitleText.getText().toString());
                DatabaseHelper db = DatabaseHelper.getHelper(adapterContext);
                ArrayList<StoreItem> alSI = db.navigateStore(storeNav.getName(), viewHolder.vTitleText.getText().toString());
                System.out.println(alSI.get(0).getvLocation());
                viewHolder.vItemLocation.setText("Location: " + alSI.get(0).getvLocation());
            }
            catch (Exception e) {
                viewHolder.vItemLocation.setText("Not found in this store");
            }
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
        public ImageButton vItemInfo;
        public String parentList;
        public Context vhContext;
        public TextView vItemLocation;
        public boolean vIsNavigated;
        public ShoppingListItem thisItem;
        public StoreItem vStoreItem;

        public ViewHolder(View v, FragmentManager FM, String listName, Context context, boolean isNav, int isFound) {
            super(v);

            vTitleText = (TextView) v.findViewById(R.id.itemText);
            vQuantity = (TextView) v.findViewById(R.id.QuantityText);
            vItemLocation = (TextView) v.findViewById(R.id.locationText);
            vCardView = (CardView) v.findViewById(R.id.itemcardview);
            vIncQuantity = (ImageButton) v.findViewById(R.id.incQuantity);
            vDecQuantity = (ImageButton) v.findViewById(R.id.decQuantity);
            vItemInfo = (ImageButton) v.findViewById(R.id.itemInfo);

            vhContext = context;
            parentList = listName;
            vFM = FM;
            vIsNavigated = isNav;
            vTitleText.setOnClickListener(this);
            vCardView.setOnClickListener(this);
            vIncQuantity.setOnClickListener(this);
            vDecQuantity.setOnClickListener(this);
            vItemInfo.setOnClickListener(this);
        }


        public void onClick(View v) {
            DatabaseHelper db = new DatabaseHelper(vhContext);
            switch (v.getId()) {
                //Increase quantity of an item
                //Writes to the database, increase local counter and reset textView
                case R.id.incQuantity:
                    db.increaseQuantity(parentList, vTitleText.getText().toString());
                    thisItem.increaseQuantity();
                    vQuantity.setText("Quantity: " + thisItem.getQuantity());
                    break;

                //Decrease the quantity of an item
                //Verification performed, then decreased in the database.
                //Decrease local counter and reset textView.
                case R.id.decQuantity:
                    //Ensure that quantity can not drop below 1.
                    if (thisItem.getQuantity() < 2) {
                        SnackbarManager.show(Snackbar.with(vhContext).color(R.color.material_deep_teal_500).text("Quantity is already 1"));
                    } else {
                        db.decreaseQuantity(parentList, vTitleText.getText().toString());
                        thisItem.decreaseQuantity();
                        vQuantity.setText("Quantity: " + thisItem.getQuantity());
                    }
                    break;

                case R.id.itemInfo:
                    ArrayList<StoreItem> alSI = db.nonStoreSearch(thisItem.getName());
                    if (alSI.size() == 0) {
                        CharSequence noText = "Sorry, no matches found.";
                        int noDuration = Toast.LENGTH_SHORT;
                        Toast foundToast = Toast.makeText(vhContext, noText, noDuration);
                        foundToast.show();
                    }
                    else if(alSI.size() == 1) {
                        DialogSearchResults diagSI = new DialogSearchResults(alSI.get(0), vhContext);
                        diagSI.show(fragmentManager, null);
                    }
                    else if(alSI.size() > 1) {
                        DialogChooseItem diagCI = new DialogChooseItem(alSI, vhContext);
                        diagCI.show(fragmentManager, null);
                    }
                    break;
            }
        }
    }
}