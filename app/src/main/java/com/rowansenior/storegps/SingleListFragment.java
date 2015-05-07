package com.rowansenior.storegps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


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

    private static String listName;
    private static boolean isNavigated;
    private static Store storeNav;
    private OnFragmentInteractionListener mListener;
    private ArrayList<ShoppingListItem> itemList;
    private ListAdapter mAdapter;
    private RecyclerView rview;
    private LinearLayoutManager mLayoutManager;
    private View view;
    private Button newList;
    private DatabaseHelper db;
    private EditText newItem;
    private boolean exists = false;

    /**
     * Required empty public constructor
     */
    public SingleListFragment() {
    }

    /**
     * Creates a new MLF and establishes its Bundle file.
     */
    public static SingleListFragment newInstance(String ln) {
        SingleListFragment fragment = new SingleListFragment();
        Bundle args = new Bundle();
        listName = ln;
        isNavigated = false;
        storeNav = null;
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Creates a new MLF and establishes its Bundle file.
     */
    public static SingleListFragment newInstance(String ln, boolean isNav, Store store) {
        SingleListFragment fragment = new SingleListFragment();
        Bundle args = new Bundle();
        listName = ln;
        isNavigated = isNav;
        storeNav = store;
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
        setHasOptionsMenu(true);
        db = new DatabaseHelper(getActivity());
        itemList = db.getAllItems(listName);

        //Sort the list so that found items are at the bottom
        //Sadly, not 100% state saving, but does reflect the status of the lists contents
        Collections.sort(itemList, new Comparator<ShoppingListItem>() {
            @Override
            public int compare(ShoppingListItem lhs, ShoppingListItem rhs) {
                return lhs.getFound() - rhs.getFound();
            }
        });
        mAdapter = new ListAdapter(getActivity(), itemList, listName, isNavigated, storeNav);
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
        if (storeNav == null) {
            ((MainActivity) getActivity()).changeActionBarTitle(listName);
        } else {
            ((MainActivity) getActivity()).changeActionBarTitle(listName + " : " + storeNav.getName());
        }
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

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(rview,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            //Mark the item found/not found.
                            //Readd the item to the set.
                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                    ShoppingListItem tempItem = itemList.get(position);
                                    if (tempItem.getFound() == 0) {
                                        db.itemFound(listName, tempItem.getName());

                                        //Move item to the bottom of the list
                                        //Set the found status
                                        itemList.remove(tempItem);
                                        tempItem.setFound();
                                        itemList.add(tempItem);
                                    } else {
                                        db.itemNotFound(listName, tempItem.getName());

                                        //Move item to the bottom of the list
                                        //Set the found status
                                        itemList.remove(tempItem);
                                        tempItem.setFound();
                                        itemList.add(0, tempItem);
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                            //Delete the item from the list
                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (final int position : reverseSortedPositions) {
                                    final ShoppingListItem tempItem = itemList.get(position);
                                    final int size = itemList.size()
;                                    itemList.remove(tempItem);
                                    db.removeItem(listName, tempItem.getName());
                                    SnackbarManager.show(Snackbar.with(getActivity()).color(R.color.material_deep_teal_500)
                                            .text(tempItem.getName() + " has been deleted")
                                            .actionLabel("Undo")
                                            .actionListener(new ActionClickListener() {
                                                                @Override
                                                                public void onActionClicked(Snackbar snackbar) {
                                                                    if (!itemList.contains(tempItem)) {
                                                                        //itemList.add(position, tempItem);
                                                                        try {
                                                                            db.undoDeleteItem(listName, tempItem.getName(), tempItem.getQuantity(), tempItem.getFound());
                                                                            if (itemList.size() == size) {
                                                                                itemList.add(position + 1, tempItem);
                                                                            } else {
                                                                                itemList.add(position, tempItem);
                                                                            }
                                                                        } catch (Exception e) {

                                                                        }
                                                                        System.out.println("its reaching this");
                                                                        mAdapter.notifyDataSetChanged();
                                                                    }
                                                                }
                                                            }
                                            ));
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });
        rview.addOnItemTouchListener(swipeTouchListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Delete the currently viewed list.
            //Returns to the location used to access the list (can be home, MyList, or from a store)
            case R.id.deleteThisList:
                final AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
                alert.setMessage("Are you sure you want to delete this list?");
                alert.setButton(Dialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.removeList(listName);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.popBackStack();
                    }
                });
                alert.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alert.show();
                return true;
            //Refreshes the list and pulls in information from the selected store.
            case R.id.navigateAStore:
                FragmentManager fm = getFragmentManager();
                DialogChooseStore diagCS = new DialogChooseStore(listName);
                diagCS.show(fm, null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_single_list_fragment, menu);
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
        ShoppingListItem listPicked = this.itemList.get(position);
    }

    /**
     * add an item to the shopping list. If the name field is left blank promp a warning
     * if it has a name add it to the list
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        exists = false;
        switch (v.getId()) {
            case R.id.addItemButton:

                Editable name = newItem.getText();

                for (int i = 0; i < itemList.size(); i++) {

                    if (name.toString().equals((itemList.get(i).getName().toString()))) {
                        SnackbarManager.show(Snackbar.with(getActivity()).color(R.color.material_deep_teal_500).text(name.toString() + " already exists"));
                        exists = true;
                        break;
                    }
                }
                if (exists == false) {
                    if (name.toString().trim().length() == 0) {
                        SnackbarManager.show(Snackbar.with(getActivity()).color(R.color.material_deep_teal_500).text("Please enter an item name"));
                    } else {
                        db.addNewItem(listName, name.toString());
                        itemList = db.getAllItems(listName);

                        Collections.sort(itemList, new Comparator<ShoppingListItem>() {
                            @Override
                            public int compare(ShoppingListItem lhs, ShoppingListItem rhs) {
                                return lhs.getFound() - rhs.getFound();
                            }
                        });

                        mAdapter = new ListAdapter(getActivity(), itemList, listName, isNavigated);
                        rview.setAdapter(mAdapter);
                    }
                }
                if (exists == false) {
                    //NEED TO HANDLE REFRESH OF ELEMENTS
                    rview.refreshDrawableState();
                    mAdapter.notifyDataSetChanged();
                    newItem.clearFocus();
                    newItem.setText("");
                    break;
                }
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
        public void onFragmentInteraction(Uri uri);
    }

}