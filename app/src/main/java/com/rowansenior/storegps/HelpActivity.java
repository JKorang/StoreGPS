package com.rowansenior.storegps;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.io.IOException;

/**
 * The 'Help' menu option.
 * Will be a separate activity, not yet developed at all.
 */
public class HelpActivity extends ActionBarActivity {

    /**
     * onCreate triggers immediately upon runtime.
     * Saves previous window state and sets the content view.
     * Uses 'activity_help' from layout/activity_help.xml
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ImageButton brb = (ImageButton) findViewById(R.id.bigRedButton);
        brb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bRB = new Intent(Intent.ACTION_VIEW);
                bRB.setData(Uri.parse("http://i.imgur.com/YXclEpw.jpg"));
                startActivity(bRB);
            }
        });
        try {
            UserLocation ul = new UserLocation(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Inflates the action bar with menu items.
     * Not sure just yet if we are using the actionbar for anything beyond title.
     * <p/>
     * ActionBar is the top bar with the title, icons, drop down menu, etc.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }

    /**
     * Handle action bar item clicks here.
     * The action bar will automatically handle clicks on the Home/Up button,
     * so long as you specify a parent activity in AndroidManifest.xml.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
