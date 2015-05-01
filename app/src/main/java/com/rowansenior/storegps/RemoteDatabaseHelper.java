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
import org.json.JSONObject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
            " STRING," + KEY_URL + " STRING," + KEY_HOUR_OPEN + " INTEGER," + KEY_HOUR_CLOSED + " INTEGER" + ")";

    public RemoteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void getNearbyStores() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDialog()
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
                .penaltyLog()
                .build());

        JSONObject json = null;
        String str = "";
        HttpResponse response;
        HttpClient myClient = new DefaultHttpClient();
        HttpPost myConnection = new HttpPost("http://www.jkorang.com/accessStores.php");

        try {
            response = myClient.execute(myConnection);
            str = EntityUtils.toString(response.getEntity(), "UTF-8");

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            JSONArray jArray = new JSONArray(str);
            json = jArray.getJSONObject(0);
            System.out.println(json);

        } catch ( JSONException e) {
            e.printStackTrace();
        }


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
}