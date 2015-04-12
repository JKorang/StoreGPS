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
        ViewHolder vh = new ViewHolder(v, fragmentManager);
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
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView vTitleText;
        public TextView vDate;
        public CardView vCardView;
        public ImageView vImageView;
        public FragmentManager vFM;

        public ViewHolder(View v, FragmentManager FM) {
            super(v);
            vTitleText = (TextView) v.findViewById(R.id.titleText);
            vDate = (TextView) v.findViewById(R.id.date);
            vCardView = (CardView) v.findViewById(R.id.card_view);
            vImageView = (ImageView) v.findViewById(R.id.listIcon);
            vFM = FM;
            vTitleText.setOnClickListener(this);
            vDate.setOnClickListener(this);
            vCardView.setOnClickListener(this);
            vImageView.setOnClickListener(this);
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
    }
}