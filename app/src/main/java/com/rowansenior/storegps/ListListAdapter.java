package com.rowansenior.storegps;

/**
 * Created by Joseph on 3/27/2015.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.app.Activity;
import java.util.List;

/**
 * ListListAdapter handles transmission and access of data from fragments associated with it.
 * In this case, LLA handles data access of IndividualListFragments and places the content
 * within views that the monitor is attached to.
 */
public class ListListAdapter extends RecyclerView.Adapter<ListListAdapter.ListViewHolder> {

    private Context context;
    private List<ShoppingList> items;

    public ListListAdapter(Context context, List items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_individual_list, viewGroup, false);
        ListViewHolder vh = new ListViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ListViewHolder viewHolder, int i) {
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
    public static class ListViewHolder extends RecyclerView.ViewHolder {
        public TextView vTitleText;
        public TextView vDate;

        public ListViewHolder(View v) {
            super(v);
            vTitleText = (TextView) v.findViewById(R.id.titleText);
            vDate = (TextView) v.findViewById(R.id.date);

        }
    }
}