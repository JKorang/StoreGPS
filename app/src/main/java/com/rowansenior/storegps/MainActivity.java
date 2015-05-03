package com.rowansenior.storegps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.*;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

/**
 * THE core and MainActivity of the entire program.
 * Currently assuming that a check will be performed onCreate to verify a login, assuming
 * that we keep the login system at all.  May be pushed back to save time for minor functionality.
 * <p/>
 * Houses the NavigationDrawer and the main view of the entire program.
 * This activity runs as the base for every other fragment within the application,
 * with the exception of Settings and Help, which will be ran as separate Activities.
 * Those Activities will still be launched through this Activity.
 * <p/>
 * A switch statement is used within the activity in order to switch the current focus on the
 * application, by creating new fragments and pushing them to the View.
 * <p/>
 * By default, this will launch the HomeFragment on load.
 */

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        MyListFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,
        MyStoreFragment.OnFragmentInteractionListener,
        SingleStoreFragment.OnFragmentInteractionListener {

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
     * <p/>
     * Saves previous window state, if any.
     * Set the contentView.
     * Uses 'activity_main_page' from layout/activity_main_page.xml
     * <p/>
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

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show start activity
            startActivity(new Intent(MainActivity.this, HelpActivity.class));
            Toast.makeText(MainActivity.this, "First Run", Toast.LENGTH_LONG)
                    .show();
        }


        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

    }

    /**
     * Actions to be taken based on the items selected in the NavigationDrawerFragment.
     * <p/>
     * This function creates the FragmentManager which handles the actual switching of the
     * fragment within the activity_main_page(container).
     * The manager begins a transaction to replace the existing container (referenced by it's
     * ID) with a new instance of the specific Fragment.
     * Also sets the title of the page based on the fragment chosen.
     * <p/>
     * No name is set for Settings or Help, as they are separate Activities
     *
     * @param position
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment = null;
        switch (position + 1) {
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
                fragment = new MyStoreFragment().newInstance();
                break;
            case 4:
                //Settings
                MainActivity.this.startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case 5:
                //Help
                MainActivity.this.startActivity(new Intent(MainActivity.this, HelpActivity.class));
                break;
        }
        //After case selection, update the title of the ActionBar
        //Create a FragmentManager to handle the change.
        //Replace the currently reference container with the new fragment instance
        if (fragment != null) {
            restoreActionBar();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
        }
    }

    /**
     * Required to allow interaction with fragments loaded in the activity
     *
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

    public void changeActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
    }

}

