package com.rowansenior.storegps;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import java.util.ArrayList;


/**
 * MyListFragment exists to manage ALL lists contained in the system.
 * <p/>
 * When opening the MyListFragment, the fragment should populate based on a JSON file
 * The resulting view will be a 3 wide grid, dictacted by the XML file.
 * <p/>
 * At the moment, the fragment populates based on an ArrayList of type IndividualListFragments
 * It displays these fragments as simple text boxes, based on the String name passed at creation.
 * <p/>
 * This functionality needs to be tweaked, however.  While the ArrayList implementation
 * can handle the operation, the content that the ArrayList contains must be modified.
 * Once the JSON file has been constructed to store all of the individual list information,
 * we must pull the date of creation of a list as well as the name.
 * <p/>
 * The fragment? or block of XML code that dictates the card-style view of each list
 * will also need to contain its own small menu button.
 */
public class MyStoreFragment extends Fragment implements AbsListView.OnItemClickListener, View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private ArrayList<SingleStoreFragment> storeList;
    private ListStoreAdapter mAdapter;
    private RecyclerView gview;
    private GridLayoutManager mLayoutManager;
    private View view;

    /**
     * Required empty public constructor
     */
    public MyStoreFragment() {
    }

    /**
     * We talk cars, I can talk cars.
     * Creates a new MLF and establishes its Bundle file.
     */
    public static MyStoreFragment newInstance() {
        MyStoreFragment fragment = new MyStoreFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * onCreate triggers immediately after onAttach, at the start of fragment creation.
     * Saves previous window state.
     * <p/>
     * Creates an ArrayList and populates the ArrayList.
     * This functionality will need to be updated to, instead of filling the ArrayList with
     * example Lists, a collection of Lists pulled from the JSON file.
     * <p/>
     * These Lists do not need to exist as separate fragments upon creation in this list.
     * The fragment creation will be located in the onClick, which will reference the
     * location of the JSON file and create a new IndividualListFragment
     * <p/>
     * After creating the list, mAdapter is created and set to monitor the list.
     * This adapter currently acts as a way to access the information of each
     * fragment within the ArrayList, for use on this existing fragment.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        DatabaseHelper db = new DatabaseHelper(getActivity());



         /*db.addNewFavoriteStore("This is an obnoxiously long store name but you only care about our prices", "856-256-4942", "http://www.americaneagles.com", 6, 18, "1 Mullica Hill Road Glassboro, NJ 08028");
         db.addNewFavoriteStore("Shop Rite of Williamstown", "(856) 728-5600", "http://www.shoprite.com", 7, 23, "100 S Black Horse Pike Williamstown, NJ 08094");
         db.addNewFavoriteStore("Shop Rite of Turnersville", "(856) 728-5600", "http://www.shoprite.com", 7, 23, "382 Egg Harbor Road Sewell, NJ 08080");
         db.addNewFavoriteStore("CVS Pharmacy", "856-582-0020", "http://www.cvs.com", 7, 22, "1050 N Delsea Dr, Glassboro, NJ 08028");
        */
        mAdapter = new ListStoreAdapter(getActivity(), db.getAllStores(), 0);
    }

    /**
     * Inflates the content view of the fragment.
     * Triggers immediately after onCreate
     * Uses 'fragment_my_list' from layout/fragment_my_list.xml
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_store, container, false);

        return view;
    }

    /**
     * Actions that occur AFTER the view has been created and attached.
     * <p/>
     * Here, gview is set by obtaining the view ID and then the ListListAdapter is set to it.
     */
    @Override
    public void onStart() {
        super.onStart();
        //GridLayoutManager MUST be ran BEFORE _ANY_ references are made to it.
        //RecyclerView does NOT check to see if the LayoutManager has been ran yet.
        //Because of this, calls to LM before creation will null error out.
        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        gview = (RecyclerView) getView().findViewById(R.id.myStoreRecycler);
        gview.setLayoutManager(mLayoutManager);
        gview.setAdapter(mAdapter);
    }

    /**
     * Actions that take place when a button is pressed.
     *
     * @param uri
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    /**
     * Actions to be taken when an item in the fragment is clicked.
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SingleStoreFragment listPicked = this.storeList.get(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.listIcon:
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).changeActionBarTitle("My Stores");
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

}