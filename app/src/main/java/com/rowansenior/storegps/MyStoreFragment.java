package com.rowansenior.storegps;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


/**
 * MyListFragment exists to manage ALL lists contained in the system.
 *
 * When opening the MyListFragment, the fragment should populate based on a JSON file
 * The resulting view will be a 3 wide grid, dictacted by the XML file.
 *
 * At the moment, the fragment populates based on an ArrayList of type IndividualListFragments
 * It displays these fragments as simple text boxes, based on the String name passed at creation.
 *
 * This functionality needs to be tweaked, however.  While the ArrayList implementation
 * can handle the operation, the content that the ArrayList contains must be modified.
 * Once the JSON file has been constructed to store all of the individual list information,
 * we must pull the date of creation of a list as well as the name.
 *
 * The fragment? or block of XML code that dictates the card-style view of each list
 * will also need to contain its own small menu button.
 *
 */
public class MyStoreFragment extends Fragment implements AbsListView.OnItemClickListener, View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private ArrayList<IndividualStoreFragment> storeList;
    private ListStoreAdapter mAdapter;
    private RecyclerView gview;
    private GridLayoutManager mLayoutManager;
    private View view;

    /**We talk cars, I can talk cars.
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
     *
     * Creates an ArrayList and populates the ArrayList.
     * This functionality will need to be updated to, instead of filling the ArrayList with
     * example Lists, a collection of Lists pulled from the JSON file.
     *
     * These Lists do not need to exist as separate fragments upon creation in this list.
     * The fragment creation will be located in the onClick, which will reference the
     * location of the JSON file and create a new IndividualListFragment
     *
     * After creating the list, mAdapter is created and set to monitor the list.
     * This adapter currently acts as a way to access the information of each
     * fragment within the ArrayList, for use on this existing fragment.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper db = new DatabaseHelper(getActivity());


        /**  TEST CASES*
         db.addNewFavoriteStore("Target", 609123523, "http://www.target.com", 6, 18, "BHP");
         db.addNewFavoriteStore("Sears", 85666666, "http://www.sears.com", 6, 18, "Nearest Empty Building");
         db.addNewFavoriteStore("Obama Rama", 696969, "http://www.potus.gov", 6, 18, "White House, Bitch");
         db.addNewFavoriteStore("This is an obnoxiously long store name but you'll shop here anyway", "856-256-4942", "http://www.americaneagles.com", 6, 18, "You can find this place at the corner of 711 and Auschwitz");
         db.addNewFavoriteStore("Shop Rite of Williamstown", "(856) 728-5600", "http://www.shoprite.com", 7, 23, "100 S Black Horse Pike Williamstown, NJ 08094");
         */
        mAdapter = new ListStoreAdapter(getActivity(), db.getAllStores());


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
     *
     * Here, gview is set by obtaining the view ID and then the ListListAdapter is set to it.
     */
    @Override
    public void onStart(){
        super.onStart();
        //GridLayoutManager MUST be ran BEFORE _ANY_ references are made to it.
        //RecyclerView does NOT check to see if the LayoutManager has been ran yet.
        //Because of this, calls to LM before creation will null error out.
        mLayoutManager = new GridLayoutManager(getActivity(),3);
        gview = (RecyclerView) getView().findViewById(R.id.myStoreRecycler);
        gview.setLayoutManager(mLayoutManager);
        gview.setAdapter(mAdapter);
    }

    /**
     * Required empty public constructor
     */
    public MyStoreFragment() {
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
     * Not yet working, but should display a Toast message.
     * Toast exists entirely to test functionality.
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        IndividualStoreFragment listPicked = this.storeList.get(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.listIcon:
                System.out.println("Clicked a card");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainPage) getActivity()).changeActionBarTitle("My Stores");
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}