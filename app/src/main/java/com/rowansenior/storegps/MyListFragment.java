package com.rowansenior.storegps;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;


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
public class MyListFragment extends Fragment implements AbsListView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;
    private ArrayList<IndividualListFragment> itemList;
    private ListListAdapter mAdapter;
    private RecyclerView gview;
    private GridLayoutManager mLayoutManager;

    /**
     * Creates a new MLF and establishes its Bundle file.
     */
    public static MyListFragment newInstance() {
        MyListFragment fragment = new MyListFragment();
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

/**
 * SAMPLE TEST CASE TO TEST DATABASE FUNCTIONALITY
 * ADD 7 LISTS, REMOVE LIST 4.
 * MyList SHOULD SHOW LIST 1, 2, 3, 5, 6, 7 NOW

        db.createNewList("list1", 5, 2);
        db.createNewList("list2", 5, 2);
        db.createNewList("list3", 5, 2);
        db.createNewList("list4", 5, 2);
        db.createNewList("list5", 5, 2);
        db.createNewList("list6", 5, 2);
        db.createNewList("list7", 5, 2);
        db.createNewList("list8", 5, 2);
        db.createNewList("list9", 5, 2);
        db.createNewList("list10", 5, 2);
        db.createNewList("list11", 5, 2);
        db.createNewList("list12", 5, 2);

        db.addNewItem("list4", "pizza");

        db.removeList("list4");

        db.addNewItem("list2", "shampoo");
        db.addNewItem("list2", "soap");
        db.addNewItem("list2", "coffee");
        db.addNewItem("list2", "gun");

        db.removeItem("list2", "gun");

        db.addNewItem("list2", "gum");
*/


        mAdapter = new ListListAdapter(getActivity(), db.getAllLists());

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
        return inflater.inflate(R.layout.fragment_my_list, container, false);
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
        gview = (RecyclerView) getView().findViewById(R.id.myListRecycler);
        gview.setLayoutManager(mLayoutManager);
        gview.setAdapter(mAdapter);
    }

    /**
     * Required empty public constructor
     */
    public MyListFragment() {
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
        IndividualListFragment listPicked = this.itemList.get(position);
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
