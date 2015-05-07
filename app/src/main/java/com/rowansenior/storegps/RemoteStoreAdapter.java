package com.rowansenior.storegps;

/**
 * Created by Joseph on 3/27/2015.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
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
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

import static android.graphics.BitmapFactory.*;

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
    private int storeCounter;

    public RemoteStoreAdapter(Context context, List stores, int nearby) {
        this.storeCounter = 0;
        this.stores = stores;
        this.context = context;
        this.isNearby = nearby;
        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_individual_list, viewGroup, false);
        ViewHolder vh = new ViewHolder(v, fragmentManager, context, stores.get(storeCounter));
        storeCounter++;
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Store store = stores.get(i);
        viewHolder.vTitleText.setText(store.getName());
        viewHolder.vLocation.setText(df.format(store.getvDistanceTo()) + " miles");
        viewHolder.vCardView.setCardBackgroundColor(Color.parseColor(store.getColor()));
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

        public ViewHolder(View v, FragmentManager FM, Context context, Store store) {
            super(v);
            vTitleText = (TextView) v.findViewById(R.id.titleText);
            vLocation = (TextView) v.findViewById(R.id.date);
            vCardView = (CardView) v.findViewById(R.id.card_view);
            vImageView = (ImageView) v.findViewById(R.id.listIcon);
            vStore = store;
            try {
                String imageURL = "http://jkorang.com/sites/default/files/webform/" + vStore.getImage();
                new DownloadImage(vImageView).execute(imageURL);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

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
            return true;
        }

        private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;

            public DownloadImage(ImageView bmImage) {
                this.bmImage = bmImage;
            }

            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = decodeStream(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return mIcon11;
            }

            protected void onPostExecute(Bitmap result) {
                bmImage.setImageBitmap(result);
            }
        }

    }
}