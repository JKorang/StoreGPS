package com.rowansenior.storegps;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * HomeFragment is the "home page" of the application, and the default fragment.
 * This fragment is loaded when clicking "Home" from the Navigation Drawer,
 * or when booting the app.
 *
 * HomeFragment will implement a GridView, displaying MyLists, MyStores, and NearbyStores.
 * Each section of HomeFragment will feature 3 items from each of the 3 categories.
 *
 * In the event of the system having less than 3 items for a particular category,
 * blank space or an Add button will be shown in its place.
 *
 * Each section will also have its own "More" Button to view more items for a particular section.
 */
public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ListListAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private View view;
    private RecyclerView gview;

    /**
     * Creates a new unique instance of the fragment.
     * Bundle is used to store all unique data specific to this new instance of the fragment.
     * Bundles allow the data to be passed to other activities/fragments.
     *
     * @return
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Required empty public constructorR
     */
    public HomeFragment() {
    }

    /**
     * onCreate triggers immediately after onAttach, at the start of fragment creation.
     * Saves previous window state.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        DatabaseHelper db = new DatabaseHelper(getActivity());
        mAdapter = new ListListAdapter(getActivity(), db.getLast3Lists());
    }

    /**
     * Inflates the content view of the fragment.
     * Triggers immediately after onCreate
     * Uses 'fragment_home' from layout/fragment_home.xml
     *
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        //GridLayoutManager MUST be ran BEFORE _ANY_ references are made to it.
        //RecyclerView does NOT check to see if the LayoutManager has been ran yet.
        //Because of this, calls to LM before creation will null error out.
        mLayoutManager = new GridLayoutManager(getActivity(),3);
        gview = (RecyclerView) getView().findViewById(R.id.homeMyListRecycler);
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
