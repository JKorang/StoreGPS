package com.rowansenior.storegps;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by root on 3/29/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "storeGPS";

    // Table Names
    private static final String TABLE_LIST = "list";
    private static final String TABLE_ITEM = "item";
    private static final String TABLE_STORE = "store";

    // Common column names
    private static final String KEY_NAME = "name";

    // LIST Table - column names
    private static final String KEY_ICON = "icon";
    private static final String KEY_COLOR = "color";
    private static final String KEY_ACTIVE = "active";
    private static final String KEY_DATE = "date";

    // LIST Table Create Statements
    private static final String CREATE_TABLE_LIST = "CREATE TABLE "
            + TABLE_LIST + "(" + KEY_NAME + " STRING PRIMARY KEY," + KEY_ICON
            + " INTEGER," + KEY_COLOR + " INTEGER," + KEY_ACTIVE + " BOOLEAN," + KEY_DATE
            + " DATETIME" + ")";


    // ITEM Table - column names
    private static final String KEY_ITEM_NAME = "item_name";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_IF_FOUND = "if_found";

    // Table Create Statements
    private static final String CREATE_TABLE_ITEM = "CREATE TABLE "
            + TABLE_ITEM + "(" + KEY_NAME + " STRING PRIMARY KEY," + KEY_ITEM_NAME
            + " STRING," + KEY_QUANTITY + " INTEGER," + KEY_IF_FOUND + " BOOLEAN" + ")";

    // STORE Table - column names
    private static final String KEY_STORE_NAME = "store_name";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_STORE_ICON = "store_icon";
    private static final String KEY_STORE_COLOR = "store_color";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_URL = "url";
    private static final String KEY_HOUR_OPEN = "open";
    private static final String KEY_HOUR_CLOSED = "closed";

    // Table Create Statements
    private static final String CREATE_TABLE_STORE = "CREATE TABLE "
            + TABLE_STORE + "(" + KEY_STORE_NAME + " STRING PRIMARY KEY," + KEY_STORE_ICON
            + " INTEGER," + KEY_STORE_COLOR + " INTEGER," + KEY_LOCATION + " STRING," + KEY_PHONE_NUMBER +
            " INTEGER," + KEY_URL + " STRING," + KEY_HOUR_OPEN + " INTEGER," + KEY_HOUR_CLOSED + " INTEGER" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_ITEM);
        db.execSQL(CREATE_TABLE_LIST);
        db.execSQL(CREATE_TABLE_STORE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE);

        // create new tables
        onCreate(db);
    }

    public static synchronized DatabaseHelper getHelper(Context context) {
        if(instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    public ArrayList<ShoppingList> getAllLists() {
        SQLiteDatabase dataBase = this.getReadableDatabase();
        ArrayList<ShoppingList> allLists = new ArrayList<ShoppingList>();
        String selectQuery = "SELECT  * FROM " + TABLE_LIST;

        Cursor c = dataBase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ShoppingList sl = new ShoppingList(c.getString(c.getColumnIndex(KEY_NAME));


                // adding to todo list
                allLists.add(sl);
            } while (c.moveToNext());
        }

        return allLists;
    }

}