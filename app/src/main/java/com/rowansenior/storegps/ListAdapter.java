package com.rowansenior.storegps;

/**
 * Created by Joseph on 3/27/2015.
 */
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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

/**
 * ListAdapter handles transmission and access of data from fragments associated with it.
 * In this case, LLA handles data access of IndividualListFragments and places the content
 * within views that the monitor is attached to.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private Context context;
    private List<ShoppingListItem> items;
    public static FragmentManager fragmentManager;


    public ListAdapter(Context context, List items) {
        this.items = items;
        this.context = context;
        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_item, viewGroup, false);
        System.out.println(v);
        System.out.println(fragmentManager);
        ViewHolder vh = new ViewHolder(v, fragmentManager);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ShoppingListItem item = items.get(i);
        viewHolder.vTitleText.setText(item.getName());
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
        public CardView vCardView;
        public FragmentManager vFM;

        public ViewHolder(View v, FragmentManager FM) {
            super(v);
            vTitleText = (TextView) v.findViewById(R.id.itemText);
            vQuantity = (TextView) v.findViewById(R.id.QuantityText);
            vCardView = (CardView) v.findViewById(R.id.itemcardview);
            vFM = FM;
            vTitleText.setOnClickListener(this);
            vQuantity.setOnClickListener(this);
            vCardView.setOnClickListener(this);
        }



        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.titleText:
                    System.out.println("title text");
                case R.id.date:
                    System.out.println("Date");
                case R.id.card_view:
                    System.out.println("Card View");
                case R.id.listIcon:
                    System.out.println("Image");
            }
            //vFM.beginTransaction().replace(R.id.container, new IndividualListFragment().newInstance()).commit();
        }
    }
}