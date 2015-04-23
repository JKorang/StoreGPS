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
 * In this case, LLA handles data access of IndividualListFragments and places the content
 * within views that the monitor is attached to.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private Context adapterContext;
    private List<ShoppingListItem> items;
    private String vhListName;
    public static FragmentManager fragmentManager;

    public ListAdapter(Context context, List items, String listName) {
        this.items = items;
        this.adapterContext = context;
        this.vhListName = listName;
        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(v, fragmentManager, vhListName, adapterContext);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ShoppingListItem item = items.get(i);
        viewHolder.vTitleText.setText(item.getName());
        viewHolder.vQuantityInt = item.getQuantity();
        viewHolder.vQuantity.setText("Quantity: " + String.valueOf(item.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Capable of holding each item in the LLA.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        public ViewHolder(View v, FragmentManager FM, String listName, Context context) {
            super(v);

            vTitleText = (TextView) v.findViewById(R.id.itemText);
            vQuantity = (TextView) v.findViewById(R.id.QuantityText);
            vCardView = (CardView) v.findViewById(R.id.itemcardview);
            vIncQuantity = (ImageButton) v.findViewById(R.id.incQuantity);
            vDecQuantity = (ImageButton) v.findViewById(R.id.decQuantity);
            vFoundItem = (ImageButton) v.findViewById(R.id.foundItem);
            vDeleteItem = (ImageButton) v.findViewById(R.id.deleteItem);

            vhContext = context;
            parentList = listName;
            vFM = FM;
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
                case R.id.incQuantity:
                    db.increaseQuantity(parentList, vTitleText.getText().toString());
                    break;
                case R.id.decQuantity:
                    if(vQuantityInt < 2)
                    {
                        CharSequence text = "Quantity must be at least 1!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(vhContext, text, duration);
                        toast.show();
                    }
                    else {
                        db.decreaseQuantity(parentList, vTitleText.getText().toString());
                        vQuantityInt--;
                    }
                    break;
                case R.id.foundItem:
                    db.itemFound(parentList, vTitleText.getText().toString());
                    break;
                case R.id.deleteItem:
                    db.removeItem(parentList, vTitleText.getText().toString());
                    break;
            }
            //vFM.beginTransaction().replace(R.id.container, new IndividualListFragment().newInstance()).commit();
        }
    }
}