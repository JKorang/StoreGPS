package com.rowansenior.storegps;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.StrictMode;

public class RemoteDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "storeGPS";

    private static final int DATABASE_VERSION = 14;


    private static final String TABLE_NEARBY_STORE = "nearbystore";

    private static final String KEY_STORE_NAME = "store_name";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_STORE_ICON = "store_icon";
    private static final String KEY_STORE_COLOR = "store_color";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_URL = "url";
    private static final String KEY_HOUR_OPEN = "open";
    private static final String KEY_HOUR_CLOSED = "closed";

    private static final String CREATE_TABLE_NEARBY_STORE = "CREATE TABLE "
            + TABLE_NEARBY_STORE + "(" + KEY_STORE_NAME + " STRING PRIMARY KEY," + KEY_STORE_ICON
            + " INTEGER," + KEY_STORE_COLOR + " INTEGER," + KEY_LOCATION + " STRING," + KEY_PHONE_NUMBER +
            " STRING," + KEY_URL + " STRING," + KEY_HOUR_OPEN + " STRING," + KEY_HOUR_CLOSED + " STRING" + ")";

    Context dbContext;

    public RemoteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dbContext = context;
    }

    public void getNearbyStores() {
        new getStoresAsync().execute();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NEARBY_STORE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEARBY_STORE);
        onCreate(db);
    }

    private class getStoresAsync extends AsyncTask<String, String, JSONArray> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            String str = "";
            HttpResponse response;
            HttpClient client = new DefaultHttpClient();

            //LAN
            HttpPost address = new HttpPost("http://192.168.29.12:9050//accessStores.php");

            //WAN
            //HttpPost address = new HttpPost("http://www.jkorang.com/accessStores.php");

            try {
                response = client.execute(address);
                str = EntityUtils.toString(response.getEntity(), "UTF-8");

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONArray jArray;
            try {
                jArray = new JSONArray(str);
            } catch (JSONException e) {
                jArray = null;
                e.printStackTrace();
            }
            return jArray;
        }

        @Override
        protected void onPostExecute(JSONArray jArray) {
            SQLiteDatabase db = getReadableDatabase();
            db.execSQL(CREATE_TABLE_NEARBY_STORE);
            for (int i = 0; i < jArray.length(); i = i + 8) {
                try {
                String insertQuery = "INSERT INTO " + TABLE_NEARBY_STORE + " VALUES(" + '"' + jArray.get(i) + '"' + ", 1, 1, " + '"' + jArray.get(i+1) + '"' + ", " + '"' + jArray.get(i+2) + '"' + ", " + '"' + jArray.get(i+5) + '"' + ", "  + '"' + jArray.get(i+6) + '"' + ", " + '"' + jArray.get(i+7) + '"' +")";
                    db.execSQL(insertQuery);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}