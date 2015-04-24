package com.rowansenior.storegps;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
public class SingleListFragment extends Fragment implements AbsListView.OnItemClickListener, View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private ArrayList<ShoppingListItem> itemList;
    private ListAdapter mAdapter;
    private RecyclerView rview;
    private LinearLayoutManager mLayoutManager;
    private View view;
    private static String listName;
    private Button newList;
    private DatabaseHelper db;
    private EditText newItem;
    private static boolean isNavigated;
    private boolean exists = false;

    /**
     * Creates a new MLF and establishes its Bundle file.
     */
    public static SingleListFragment newInstance(String ln) {
        SingleListFragment fragment = new SingleListFragment();
        Bundle args = new Bundle();
        listName = ln;
        isNavigated = false;
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Creates a new MLF and establishes its Bundle file.
     */
    public static SingleListFragment newInstance(String ln, boolean isNav) {
        SingleListFragment fragment = new SingleListFragment();
        Bundle args = new Bundle();
        listName = ln;
        isNavigated = isNav;
        System.out.print(isNavigated);
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
        db = new DatabaseHelper(getActivity());
        itemList = db.getAllItems(listName);
        mAdapter = new ListAdapter(getActivity(), itemList, listName, isNavigated);
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
        view = inflater.inflate(R.layout.fragment_list_layout, container, false);
        newList = (Button) view.findViewById(R.id.addItemButton);
        newItem = (EditText) view.findViewById(R.id.newItemName);
        newList.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainPage) getActivity()).changeActionBarTitle(listName);
    }

    /**
     * Actions that occur AFTER the view has been created and attached.
     * <p/>
     * Here, rview is set by obtaining the view ID and then the ListListAdapter is set to it.
     */
    @Override
    public void onStart() {
        super.onStart();
        //GridLayoutManager MUST be ran BEFORE _ANY_ references are made to it.
        //RecyclerView does NOT check to see if the LayoutManager has been ran yet.
        //Because of this, calls to LM before creation will null error out.
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rview = (RecyclerView) getView().findViewById(R.id.activeList);
        rview.setLayoutManager(mLayoutManager);
        rview.setAdapter(mAdapter);
    }

    /**
     * Required empty public constructor
     */
    public SingleListFragment() {
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
        ShoppingListItem listPicked = this.itemList.get(position);
    }

    /**
     * add an item to the shopping list. If the name field is left blank promp a warning
     * if it has a name add it to the list
     * TODO if the item is already in the list, up the quanity of the item
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addItemButton:
                exists = false;
                Editable name = newItem.getText();

                //still crashes program error on line 219
                for (int i = 0; i < itemList.size(); i++) {
                    System.out.println("NEW ITEM: " + name);
                    System.out.println("COMPARING TO: " + itemList.get(i).getName());
                    if (name.equals((itemList.get(i).getName()))) {
                        System.out.println("Match");
                        CharSequence text = "Item already exists";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(getActivity(), text, duration);
                        toast.show();
                        exists = true;
                        break;
                    }
                }

                if (name.toString().trim().length() == 0) {
                    CharSequence text = "Item must have a name!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), text, duration);
                    toast.show();

                } else if (exists == false) {
                    db.addNewItem(listName, name.toString());
                    itemList = db.getAllItems(listName);
                    mAdapter = new ListAdapter(getActivity(), itemList, listName, isNavigated);
                    rview.setAdapter(mAdapter);
                    //mAdapter.notifyDataSetChanged();
                }

                //NEED TO HANDLE REFRESH OF ELEMENTS
                rview.refreshDrawableState();
                mAdapter.notifyDataSetChanged();
                newItem.clearFocus();
                newItem.setText("");
                break;
        }
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