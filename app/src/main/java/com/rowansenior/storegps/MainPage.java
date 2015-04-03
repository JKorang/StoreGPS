package com.rowansenior.storegps;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.*;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import com.rowansenior.storegps.MyListFragment;
import com.rowansenior.storegps.MyStoreFragment;

/**
 * THE core and MainActivity of the entire program.
 * Currently assuming that a check will be performed onCreate to verify a login, assuming
 * that we keep the login system at all.  May be pushed back to save time for minor functionality.
 *
 * Houses the NavigationDrawer and the main view of the entire program.
 * This activity runs as the base for every other fragment within the application,
 * with the exception of Settings and Help, which will be ran as separate Activities.
 * Those Activities will still be launched through this Activity.
 *
 * A switch statement is used within the activity in order to switch the current focus on the
 * application, by creating new fragments and pushing them to the View.
 *
 * By default, this will launch the HomeFragment on load.
 */

public class MainPage extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        MyListFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,
        MyStoreFragment.OnFragmentInteractionListener,
        IndividualListFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title.
     */
    private CharSequence mTitle;

    /**
     * onCreate is the very first actions taken upon launch.
     * Unlike fragments, activities do not attach to anything and stand on their own.
     *
     * Saves previous window state, if any.
     * Set the contentView.
     * Uses 'activity_main_page' from layout/activity_main_page.xml
     *
     * Initializes and attaches the NavigationDrawerFragment to the Activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        DatabaseHelper dataBase = new DatabaseHelper(getApplicationContext());

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    /**
     * Actions to be taken based on the items selected in the NavigationDrawerFragment.
     *
     * This function creates the FragmentManager which handles the actual switching of the
     * fragment within the activity_main_page(container).
     * The manager begins a transaction to replace the existing container (referenced by it's
     * ID) with a new instance of the specific Fragment.
     * Also sets the title of the page based on the fragment chosen.
     *
     * No name is set for Settings or Help, as they are separate Activities
     * @param position
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment = null;
        switch (position+1) {
            case 1:
                //Home
                mTitle = getString(R.string.title_section1);
                fragment = new HomeFragment().newInstance();
                break;
            case 2:
                //MyList
                mTitle = getString(R.string.title_section2);
                fragment = new MyListFragment().newInstance();
                break;
            case 3:
                //MyStore
                mTitle = getString(R.string.title_section3);
               // fragment = new MyStoreFragment().newInstance();
                fragment = new SingleListFragment().newInstance("list2");
                break;
            case 4:
                //Settings
                MainPage.this.startActivity(new Intent(MainPage.this, SettingsActivity.class));
                break;
            case 5:
                //Help
                MainPage.this.startActivity(new Intent(MainPage.this, HelpActivity.class));
                break;
        }
        //After case selection, update the title of the ActionBar
        //Create a FragmentManager to handle the change.
        //Replace the currently reference container with the new fragment instance
        if (fragment != null) {
            restoreActionBar();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

    /**
     * Required to allow interaction with fragments loaded in the activity
     * @param uri
     */
    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    /**
     * Restores the ActionBar and sets the title of the ActionBar when a new item is chosen.
     * May need refactoring in order to support fragments not launched through navigation drawer.
     */
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void changeFragment(Fragment frag) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, frag).commit();
        }
    }

