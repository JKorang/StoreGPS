package com.rowansenior.storegps;

import android.app.Activity;
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
 * IndividualListFragment acts as a fragment for a single list.
 * Eventually, data will be pulled in from a JSON file and stored within the ILF.
 *
 * At the moment, the ListListAdapter allows pulling the information from an existing
 * fragment of ILF.  This adapter may not be required once JSON files are implemented,
 * as all data required to display in different modules can be pulled from the JSON.
 *
 * ILF currently acts almost as a custom object to simply store the name of a list.
 * It will need to be reworked slightly to display all the contents of the list it houses
 * when selected from the HomeFragment or MyListFragment.
 */
public class IndividualListFragment extends Fragment implements AbsListView.OnItemClickListener, View.OnClickListener {

    private static final String ARG_NAME = "NAME";
    private static final String ARG_DATE = "DATE";
    private String mName;
    private String mDate;
    private OnFragmentInteractionListener mListener;

    /**
     * Creates a new ILF.
     *
     * Currently requires a single string that is being used to store the name of the list.
     *
     * This functionality will be expanded to pull in more info from a JSON file.
     * That information will need the:
     * name
     * date of creation
     * listed items
     *      status of each item (checked off/found yet)
     * ***More to be decided later.
     *
     * Currently, the string param1 is being passed into the Bundle for use by other fragments.
     *
     * @param param1 - Name of the list
     * @return
     */
    public static IndividualListFragment newInstance(String param1) {
        IndividualListFragment fragment = new IndividualListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, param1);
        args.putString(ARG_DATE, "Date");
        fragment.setArguments(args);
        return fragment;
    }

    public static IndividualListFragment newInstance() {
        IndividualListFragment fragment = new IndividualListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, "temp");
        args.putString(ARG_DATE, "Date");
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Required empty public constructor
     */
    public IndividualListFragment() {
    }

    /**
     * onCreate triggers immediately after onAttach, at the start of fragment creation.
     * Saves previous window state.
     * If mName is null, set it with the passed string from newInstance.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(ARG_NAME);
            mDate = getArguments().getString(ARG_DATE);
        }
    }

    /**
     * Inflates the content view of the fragment.
     * Triggers immediately after onCreate
     * Uses 'fragment_individual_list' from layout/fragment_individual_list.xml
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // return inflater.inflate(R.layout.fragment_individual_list, container, false);
        return inflater.inflate(R.layout.fragment_list_layout, container, false);
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

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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

    /**
     * Makes a call to the fragment's Bundle in order to pull the appropriate String needed.
     *
     * @ARG_PARAM1 - The original key used to identify the item in the bundle
     * @mParam1 - Default value of the key
     * @return
     */
    public String getListTitle() {

        return getArguments().getString(ARG_NAME, mName);
    }
    public String getListDate() {

        return getArguments().getString(ARG_DATE, mDate);
    }
}
