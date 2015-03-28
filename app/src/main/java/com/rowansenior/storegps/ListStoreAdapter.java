package com.rowansenior.storegps;

/**
 * Created by Joseph on 3/27/2015.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.app.Activity;
import java.util.List;

/**
 * ListStoreAdapter handles transmission and access of data from fragments associated with it.
 * In this case, LSA handles data access of IndividualStoreFragment and places the content
 * within views that the monitor is attached to.
 */

public class ListStoreAdapter extends ArrayAdapter {

    private Context context;

    //simple_gallery_item is a predefined list style in Android
    public ListStoreAdapter(Context context, List items) {
        super(context, android.R.layout.simple_gallery_item, items);
        this.context = context;
    }

    /**
     * Capable of holding each item in the LSA.
     */
    private class ViewHolder{
        TextView titleText;
    }

    /**
     * Inflates the view for the items within the IndividualStoreFragments and returns the
     * extracted items as a view.
     *
     * View/layout to use is hardcoded to be fragment_individual_store
     * Sets the text value to return based on getListTitle() found in ISF.
     *
     * Gets the relevant information about the view that the adapter is attached to.
     * Has to create a ISF fragment, thus exclusivity to only work on ISF.
     * However, the class can be copied and modified relatively easily in order to
     * work on other fragment types.
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        IndividualStoreFragment item = (IndividualStoreFragment)getItem(position);
        // Inflates the layout based on the fragment_individual_list
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View viewToUse = mInflater.inflate(R.layout.fragment_individual_store, null);
        ViewHolder holder = new ViewHolder();
        holder.titleText = (TextView)viewToUse.findViewById(R.id.indivStoreGridView);
        viewToUse.setTag(holder);
        holder.titleText.setText(item.getListTitle());
        return viewToUse;
    }
}