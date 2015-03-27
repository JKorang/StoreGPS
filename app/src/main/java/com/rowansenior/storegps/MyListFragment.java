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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyListFragment extends Fragment implements AbsListView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;
    private ArrayList<IndividualListFragment> itemList;
    private ListAdapter mAdapter;
    private GridView gview;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyListFragment.
     */
    public static MyListFragment newInstance() {
        MyListFragment fragment = new MyListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itemList = new ArrayList();
        itemList.add(new IndividualListFragment().newInstance("Example 1"));
        itemList.add(new IndividualListFragment().newInstance("Example 2"));
        itemList.add(new IndividualListFragment().newInstance("Example 3"));
        itemList.add(new IndividualListFragment().newInstance("Example 4"));
        itemList.add(new IndividualListFragment().newInstance("Example 5"));
        itemList.add(new IndividualListFragment().newInstance("Example 6"));
        itemList.add(new IndividualListFragment().newInstance("Example 7"));
        itemList.add(new IndividualListFragment().newInstance("Example 8"));




        mAdapter = new ListAdapter(getActivity(), itemList);
        mAdapter.setNotifyOnChange(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_list, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        gview = (GridView) getView().findViewById(R.id.gridView);
        gview.setAdapter(mAdapter);
    }

    public MyListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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
