package com.rowansenior.storegps;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * MyStoreFragment exists to manage ALL favorited stores contained in the system.
 *
 * When opening the MyStoreFragment, the fragment should populate based on a JSON file
 * The resulting view will be a 3 wide grid, dictacted by the XML file.
 *
 * At the moment, the fragment populates based on an ArrayList of type IndividualStoreFragment.
 * It displays these fragments as simple text boxes, based on the String name passed at creation.
 *
 * This functionality needs to be tweaked, however.  While the ArrayList implementation
 * can handle the operation, the content that the ArrayList contains must be modified.
 * Once the JSON file has been constructed to store all of the favorite store information,
 * we must pull the name and store location data.
 *
 * GPS location data will also need to be evaluated to calculate the distance to each store
 * from the users current location.
 *
 * The fragment? or block of XML code that dictates the card-style view of each store
 * will also need to contain its own small menu button.
 *
 */
public class MyStoreFragment extends Fragment implements AbsListView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;
    private ArrayList<IndividualStoreFragment> storeList;
    private ListStoreAdapter mAdapter;
    private GridView gview;

    /**
     * Creates a new MSF and establishes its Bundle file.
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
     * example Stores, a collection of Stores pulled from the JSON file.
     *
     * These Stores do not need to exist as separate fragments upon creation in this list.
     * The fragment creation will be located in the onClick, which will reference the
     * location of the JSON file and create a new IndividualStoreFragment.
     *
     * After creating the list, mAdapter is created and set to monitor the list.
     * This adapter currently acts as a way to access the information of each
     * fragment within the ArrayList, for use on this existing fragment.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storeList = new ArrayList();
        storeList.add(new IndividualStoreFragment().newInstance("Example 1"));
        storeList.add(new IndividualStoreFragment().newInstance("Example 2"));
        storeList.add(new IndividualStoreFragment().newInstance("Example 3"));
        storeList.add(new IndividualStoreFragment().newInstance("Example 4"));
        storeList.add(new IndividualStoreFragment().newInstance("Example 5"));
        storeList.add(new IndividualStoreFragment().newInstance("Example 6"));
        storeList.add(new IndividualStoreFragment().newInstance("Example 7"));
        storeList.add(new IndividualStoreFragment().newInstance("Example 8"));

        mAdapter = new ListStoreAdapter(getActivity(), storeList);
        mAdapter.setNotifyOnChange(true);
    }

    /**
     * Inflates the content view of the fragment.
     * Triggers immediately after onCreate
     * Uses 'fragment_my_store' from layout/fragment_my_store.xml
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_store, container, false);
    }

    /**
     * Actions that occur AFTER the view has been created and attached.
     *
     * Here, gview is set by obtaining the view ID and then the ListStoreAdapter is set to it.
     */
    @Override
    public void onStart(){
        super.onStart();
        gview = (GridView) getView().findViewById(R.id.storeGridView);
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
     */    public void onButtonPressed(Uri uri) {
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
        Toast.makeText(getActivity(), listPicked.getListTitle() + " Clicked lolol", Toast.LENGTH_SHORT).show();
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
