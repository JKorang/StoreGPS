package com.rowansenior.storegps;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

public class RemoteDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "storeGPS";

    private static final int DATABASE_VERSION = 14;

    private static final String TABLE_NEARBY_STORE = "nearbystore";
    private static final String TABLE_STORE_ITEM = "storeitems";

    private static final String KEY_STORE_NAME = "store_name";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_STORE_ICON = "store_icon";
    private static final String KEY_STORE_COLOR = "store_color";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_URL = "url";
    private static final String KEY_HOUR_OPEN = "open";
    private static final String KEY_HOUR_CLOSED = "closed";

    private static final String KEY_ITEM_NAME = "item_name";
    private static final String KEY_ITEM_TAG = "tags";
    private static final String KEY_ITEM_PRICE = "price";
    private static final String KEY_ITEM_LOCATION = "location";
    private static final String KEY_STORE = "store";

    private static final String CREATE_TABLE_NEARBY_STORE = "CREATE TABLE "
            + TABLE_NEARBY_STORE + "(" + KEY_STORE_NAME + " STRING PRIMARY KEY," + KEY_STORE_ICON
            + " INTEGER," + KEY_STORE_COLOR + " INTEGER," + KEY_LOCATION + " STRING," + KEY_PHONE_NUMBER +
            " STRING," + KEY_URL + " STRING," + KEY_HOUR_OPEN + " STRING," + KEY_HOUR_CLOSED + " STRING" + ")";

    private static final String CREATE_TABLE_STORE_ITEM = "CREATE TABLE " + TABLE_STORE_ITEM + "(" + KEY_ITEM_NAME
            + " STRING NOT NULL, " + KEY_STORE + " STRING NOT NULL, " + KEY_ITEM_TAG + " STRING, " + KEY_ITEM_PRICE + " STRING, " +
            KEY_ITEM_LOCATION + " STRING, PRIMARY KEY(" + KEY_ITEM_NAME + ", " + KEY_STORE + "))";

    Context dbContext;

    public RemoteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dbContext = context;
    }

    public void getNearbyStores() {
        new getStoresAsync().execute();
    }

    public void getNearbyItems() {
        new getItemsAsync().execute();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STORE_ITEM);
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
            //HttpPost address = new HttpPost("http://192.168.29.12:9050//accessStores.php");

            //WAN
            HttpPost address = new HttpPost("http://www.jkorang.com/accessStores.php");

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
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEARBY_STORE);
            db.execSQL(CREATE_TABLE_NEARBY_STORE);
            for (int i = 0; i < jArray.length(); i = i + 8) {
                try {
                    System.out.println("Store Name: " + jArray.get(i));
                    String insertQuery = "INSERT INTO " + TABLE_NEARBY_STORE + " VALUES(" + '"' + jArray.get(i) + '"' + ", 1, 1, " + '"' + jArray.get(i + 1) + '"' + ", " + '"' + jArray.get(i + 2) + '"' + ", " + '"' + jArray.get(i + 5) + '"' + ", " + '"' + jArray.get(i + 6) + '"' + ", " + '"' + jArray.get(i + 7) + '"' + ")";
                    db.execSQL(insertQuery);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class getItemsAsync extends AsyncTask<String, String, JSONArray> {

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
            //HttpPost address = new HttpPost("http://192.168.29.12:9050//accessItems.php");

            //WAN
            HttpPost address = new HttpPost("http://www.jkorang.com/accessItems.php");

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
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE_ITEM);
            db.execSQL(CREATE_TABLE_STORE_ITEM);
            for (int i = 0; i < jArray.length(); i = i + 5) {
                try {
                    System.out.println("Item Name: " + jArray.get(i).toString());
                    String insertQuery = "INSERT INTO " + TABLE_STORE_ITEM + " VALUES(" + '"' + jArray.get(i) + '"' + ", " + '"' + jArray.get(i + 2) + '"' + ", " + '"' + jArray.get(i + 3) + '"' + ", " + '"' + jArray.get(i + 4) + '"' + ", " + '"' + jArray.get(i + 1) + '"' + ")";
                    db.execSQL(insertQuery);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}