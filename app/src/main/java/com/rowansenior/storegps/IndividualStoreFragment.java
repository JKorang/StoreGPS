package com.rowansenior.storegps;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * IndividualStoreFragment acts as a fragment for a single store.
 * Eventually, data will be pulled in from a SQL query to a remote database to pull in
 * store data for a store that exists with the apps scope.
 *
 * At the moment, the ListStoreAdapter allows pulling the information from an existing
 * fragment of ISF.  This adapter may not be required once SQL queries are implemented,
 * as all data required to display in different modules can be pulled from the SQL database.
 *
 * ISF currently acts almost as a custom object to display the name of a store.
 * It will need to be reworked slightly to display all the information for the store it houses
 * when selected from the HomeFragment, MyStoreFragment, or NearbyStoresFragment.
 */
public class IndividualStoreFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private OnFragmentInteractionListener mListener;

    /**
     * Creates a new ISF.
     *
     * Currently requires a single string that is being used to store the name of the store.
     *
     * This functionality will be expanded to pull in more info from a SQL database.
     * That information will need the:
     * name
     * location
     * phone number
     * store hours
     * ***More to be decided later.
     *
     * Currently, the string param1 is being passed into the Bundle for use by other fragments.
     *
     * @param param1 - Name of the list
     * @return
     */
    public static IndividualStoreFragment newInstance(String param1) {
        IndividualStoreFragment fragment = new IndividualStoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Required empty public constructor
     */
    public IndividualStoreFragment() {
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
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
        return inflater.inflate(R.layout.fragment_individual_store, container, false);
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

    /**
     * Makes a call to the fragment's Bundle in order to pull the appropriate String needed.
     *
     * @ARG_PARAM1 - The original key used to identify the item in the bundle
     * @mParam1 - Default value of the key
     * @return
     */
    public String getListTitle() {
        return getArguments().getString(ARG_PARAM1, mParam1);
    }
}
