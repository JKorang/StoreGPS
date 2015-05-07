package com.rowansenior.storegps;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.graphics.BitmapFactory.decodeStream;


/**
 * SingleStoreFragment acts as a fragment for a single store.
 * Eventually, data will be pulled in from a SQL query to a remote database to pull in
 * store data for a store that exists with the apps scope.
 * <p/>
 * At the moment, the ListStoreAdapter allows pulling the information from an existing
 * fragment of ISF.  This adapter may not be required once SQL queries are implemented,
 * as all data required to display in different modules can be pulled from the SQL database.
 * <p/>
 * ISF currently acts almost as a custom object to display the name of a store.
 * It will need to be reworked slightly to display all the information for the store it houses
 * when selected from the HomeFragment, MyStoreFragment, or NearbyStoresFragment.
 */
public class SingleStoreFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private String storeName;
    private Store storeInfo;
    private View storeView;
    private TextView nameOfStore;
    private TextView storeDistance;
    private TextView storeAddress;
    private TextView storePhone;
    private TextView storeURL;
    private TextView storeHours;
    private ImageView storeImage;
    private Button navigationButton;
    private Button searchButton;
    private DecimalFormat df = new DecimalFormat("#.##");
    private DatabaseHelper db;
    private Menu menuRef;


    /**
     * Required empty public constructor
     */
    public SingleStoreFragment(String store) {
        this.storeName = store;
    }

    /**
     * Creates a new ISF.
     * <p/>
     * Currently requires a single string that is being used to store the name of the store.
     * <p/>
     * This functionality will be expanded to pull in more info from a SQL database.
     * That information will need the:
     * name
     * location
     * phone number
     * store hours
     * ***More to be decided later.
     * <p/>
     * Currently, the string param1 is being passed into the Bundle for use by other fragments.
     *
     * @return
     */
    public static SingleStoreFragment newInstance(String store) {
        SingleStoreFragment fragment = new SingleStoreFragment(store);
        return fragment;
    }

    /**
     * onCreate triggers immediately after onAttach, at the start of fragment creation.
     * Saves previous window state.
     * If mParam1 is null, set it with the passed string from newInstance.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(getActivity());
        storeInfo = db.getStoreInfo(storeName);
        ((MainActivity) getActivity()).changeActionBarTitle("");
        setHasOptionsMenu(true);
    }

    /**
     * Inflates the content view of the fragment.
     * Triggers immediately after onCreate
     * Uses 'fragment_individual_store' from layout/fragment_individual_store.xml
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        storeView = inflater.inflate(R.layout.fragment_individual_store, container, false);

        nameOfStore = (TextView) storeView.findViewById(R.id.storeName);
        storeDistance = (TextView) storeView.findViewById(R.id.storeDistanceTo);
        storeAddress = (TextView) storeView.findViewById(R.id.storeAddress);
        storePhone = (TextView) storeView.findViewById(R.id.storePhoneNumber);
        storeURL = (TextView) storeView.findViewById(R.id.storeURL);
        storeHours = (TextView) storeView.findViewById(R.id.storeHours);
        storeImage = (ImageView) storeView.findViewById(R.id.storeImage);

        nameOfStore.setText(storeInfo.getName());
        storeAddress.setText(storeInfo.getLocation());
        storePhone.setText(storeInfo.getPhoneNumber());
        storeURL.setText(storeInfo.getURL());

        String openTime = String.valueOf(storeInfo.getHoursOpen());
        String closedTime = String.valueOf(storeInfo.getHoursClosed());
        storeHours.setText("Open from " + openTime + " to " + closedTime);

        try {
            UserLocation ul = new UserLocation(this.getActivity(), storeInfo.getLocation());
            Double location = ul.getDistances(ul.getUserLocation(), ul.getDestinationLocation());
            storeDistance.setText(df.format(location).toString() + " Miles Away");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String imageURL = "http://jkorang.com/sites/default/files/webform/" + storeInfo.getImage();
            new DownloadImage(storeImage).execute(imageURL);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        final DatabaseHelper db = new DatabaseHelper(getActivity());
        ArrayList<String> categories = new ArrayList<>();
        categories = db.getCategories(storeName);
        final ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, categories);
        ListView listView = (ListView) storeView.findViewById(R.id.category_list_view);
        final FragmentManager fm = (getActivity()).getSupportFragmentManager();
        listView.setAdapter(categoryAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<StoreItem> alSI = db.getItemsFromCategory(storeName, itemCategories.get(position));
                DialogChooseItem diagCI = new DialogChooseItem(alSI, getActivity());
                diagCI.show(fm, null);
            }
        });

        return storeView;
    }

    /**
     * First trigger point for the fragment.
     * Handles attaching the previous activity (for back/up functionality and runtime tree)
     * Verifies that the listener in the super activity exists.
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Very last method called upon the removal of a fragment.
     * Detaches the InteractionListener and breaks association with the super activity.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onClick(View v) {
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getFragmentManager();
        switch (item.getItemId()) {
            case R.id.individual_store_favorite:
                if(db.isStoreFavorited(storeInfo) == true) {
                    menuRef.findItem(R.id.individual_store_favorite).setIcon(R.drawable.action_bar_not_favorited);
                    db.removeFavoriteStore(storeName);
                }
                else {
                    menuRef.findItem(R.id.individual_store_favorite).setIcon(R.drawable.action_bar_favorited);
                    db.addNewFavoriteStore(storeInfo.getName(), storeInfo.getPhoneNumber(), storeInfo.getURL(), storeInfo.getHoursOpen(), storeInfo.getHoursClosed(), storeInfo.getLocation(), storeInfo.getImage(), storeInfo.getColor());
            }
            return true;

            case R.id.individual_store_navigate:
                DialogChooseList diagNL = new DialogChooseList(storeName);
                diagNL.show(fragmentManager, null);
                return true;

            case R.id.individual_store_search:
                DialogSingleItemSearch sIS = new DialogSingleItemSearch(storeName, getActivity());
                sIS.show(fragmentManager, null);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_individual_store, menu);
        menuRef = menu;
        if(db.isStoreFavorited(storeInfo) == true) {
            menu.findItem(R.id.individual_store_favorite).setIcon(R.drawable.action_bar_favorited);
        }
        else {
            menu.findItem(R.id.individual_store_favorite).setIcon(R.drawable.action_bar_not_favorited);
        }
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
