package com.rowansenior.storegps;

/**
 * Created by Joseph on 3/27/2015.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * ListListAdapter handles transmission and access of data from fragments associated with it.
 * In this case, LLA handles data access of IndividualListFragments and places the content
 * within views that the monitor is attached to.
 */
public class RemoteStoreAdapter extends RecyclerView.Adapter<RemoteStoreAdapter.ViewHolder> {

    public static FragmentManager fragmentManager;
    private Context context;
    private List<Store> stores;
    private DecimalFormat df = new DecimalFormat("#.##");
    private int isNearby;

    public RemoteStoreAdapter(Context context, List stores, int nearby) {
        this.stores = stores;
        this.context = context;
        this.isNearby = nearby;
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
        Store store = stores.get(i);
        viewHolder.vStore = store;
        viewHolder.vTitleText.setText(store.getName());
        viewHolder.vLocation.setText(df.format(store.getvDistanceTo()) + " miles");
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    /**
     * Capable of holding each item in the LLA.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ContextMenu.ContextMenuInfo, View.OnLongClickListener {
        public TextView vTitleText;
        public TextView vLocation;
        public CardView vCardView;
        public ImageView vImageView;
        private Context vhContext;
        public FragmentManager vFM;
        private Store vStore;

        public ViewHolder(View v, FragmentManager FM, Context context) {
            super(v);
            vTitleText = (TextView) v.findViewById(R.id.titleText);
            vLocation = (TextView) v.findViewById(R.id.date);
            vCardView = (CardView) v.findViewById(R.id.card_view);
            vImageView = (ImageView) v.findViewById(R.id.listIcon);
            
            vFM = FM;
            vhContext = context;
            vTitleText.setOnClickListener(this);
            vLocation.setOnClickListener(this);
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
            if (isNearby == 0) {
                vFM.beginTransaction().replace(R.id.container, new SingleStoreFragment(newFrag).newInstance(newFrag)).addToBackStack(null).commit();
            }
            else {
                vFM.beginTransaction().replace(R.id.container, new SingleStoreFragment(newFrag).newInstance(newFrag)).addToBackStack(null).commit();
            }
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
            /**
            final AlertDialog alert = new AlertDialog.Builder(vhContext).create();
            alert.setMessage("Would you like to add this store to your favorites?");
            alert.setButton(Dialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    DatabaseHelper db = new DatabaseHelper(vhContext);
                    db.addNewFavoriteStore(vStore.getName(), vStore.getPhoneNumber(), vStore.getURL(), vStore.getHoursOpen(), vStore.getHoursClosed(), vStore.getLocation());
                    notifyDataSetChanged();
                }
            });
            alert.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.show();
             */
            return true;
        }
    }
}