package com.rowansenior.storegps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by root on 3/29/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance;

    // Database Version
    private static final int DATABASE_VERSION = 14;

    // Database Name
    private static final String DATABASE_NAME = "storeGPS";

    // Table Names
    private static final String TABLE_LIST = "list";
    private static final String TABLE_ITEM = "_item";
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
            + " INTEGER," + KEY_COLOR + " INTEGER," + KEY_ACTIVE + " INTEGER," + KEY_DATE
            + " STRING" + ")";


    // ITEM Table - column names
    private static final String KEY_ITEM_NAME = "item_name";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_IF_FOUND = "if_found";

    // Table Create Statements
    private static final String CREATE_TABLE_ITEM = "CREATE TABLE "
            + KEY_NAME + TABLE_ITEM + "(" + KEY_NAME + " STRING," + KEY_ITEM_NAME
            + " STRING," + KEY_QUANTITY + " INTEGER," + KEY_IF_FOUND + " INTEGER" + ")";

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
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    /**
     * Create a new ShoppingList within the database
     *
     * NEED TO HANDLE ERRORS FOR EXISTING LISTS
     */
    public long createNewList(String name, int color, int icon) {
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String format = s.format(new Date());
        SQLiteDatabase dataBase = this.getReadableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_NAME, name);
        newValues.put(KEY_ICON, icon);
        newValues.put(KEY_COLOR, color);
        newValues.put(KEY_ACTIVE, 1);
        newValues.put(KEY_DATE, format);
        String itemTable = name + "" + TABLE_ITEM;
        dataBase.execSQL("CREATE TABLE " + '"' + itemTable + '"' + "(" + KEY_ITEM_NAME + " STRING PRIMARY KEY," + KEY_QUANTITY + " INTEGER," + KEY_IF_FOUND + " INTEGER" + ")");
        return dataBase.insert(TABLE_LIST, null, newValues);
    }

    /**
     * Add item to existing ShoppingList.
     *
     * NEED TO TEST FOR UNIQUE KEYS.
     * Not sure if sqlite will reject each item after the first for a list.
     * Might need to give item name column PK in the database, as well.
     *
     * NEED TO HANDLE ERRORS FOR EXISTING ITEMS
     * INCREMENT EXISTING
     */
    public void addNewItem(String listName, String itemName) {
        SQLiteDatabase dataBase = this.getReadableDatabase();

        String selectQuery = "INSERT INTO " + listName + TABLE_ITEM + " VALUES(" + '"' + itemName + '"' + ", 1, 0)";
        dataBase.execSQL(selectQuery);
    }

    /**
     * Removes row from the TABLE_ITEM.
     * Finds only rows with the same list name and item name.
     *
     * NEED ERROR HANDLING FOR ITEM THAT DOES NOT EXIST
     *
     * @param listName
     * @param itemName
     */
    public void removeItem(String listName, String itemName) {
        SQLiteDatabase dataBase = this.getReadableDatabase();
        String selectQuery = "DELETE FROM " + '"' +  listName  + TABLE_ITEM + '"' + " WHERE " + KEY_ITEM_NAME +
                " = " + '"' + itemName + '"';
        dataBase.execSQL(selectQuery);
    }

    /**
     * Removes a list by name.
     * On button press, get the name of the list and kill the table. Completely removes it.
     *
     * Will have to implement an archive button, which marks active as 0.
     * 
     * @param listName
     */
    public void removeList(String listName) {
        SQLiteDatabase dataBase = this.getReadableDatabase();
        String selectQuery = "DELETE FROM " + TABLE_LIST + " WHERE " + KEY_NAME + " = " + '"' + listName + '"';
        dataBase.execSQL(selectQuery);
        dataBase.execSQL("DROP TABLE IF EXISTS " + listName + TABLE_ITEM);
    }

    /**
     * Pulls in data from the database for ALL lists within the system.
     * Has to read out to getAllItems in order to fill an arraylist of items for each list.
     * @return
     */
    public ArrayList<ShoppingList> getAllLists() {
        SQLiteDatabase dataBase = this.getReadableDatabase();
        ArrayList<ShoppingList> allLists = new ArrayList<ShoppingList>();
        String selectQuery = "SELECT  * FROM " + TABLE_LIST;

        Cursor c = dataBase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToLast()) {
            do {
                ShoppingList sl = new ShoppingList(c.getString(c.getColumnIndex(KEY_NAME)),
                        c.getString(c.getColumnIndex(KEY_DATE)),
                        c.getInt(c.getColumnIndex(KEY_ICON)),
                        c.getInt(c.getColumnIndex(KEY_COLOR)),
                        c.getInt((c.getColumnIndex(KEY_ACTIVE))),
                        null);
                allLists.add(sl);
            } while (c.moveToPrevious());
        }
        return allLists;
    }

    public ArrayList<ShoppingList> getlast3Lists() {
        SQLiteDatabase dataBase = this.getReadableDatabase();
        ArrayList<ShoppingList> allLists = new ArrayList<ShoppingList>();
        String selectQuery = "SELECT  * FROM " + TABLE_LIST;

        Cursor c = dataBase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToLast()) {
            for(int i = 0; i < 3; i++){

                if(c.isNull(i)) {break;}

                else{
                    ShoppingList sl = new ShoppingList(c.getString(c.getColumnIndex(KEY_NAME)),
                            c.getString(c.getColumnIndex(KEY_DATE)),
                            c.getInt(c.getColumnIndex(KEY_ICON)),
                            c.getInt(c.getColumnIndex(KEY_COLOR)),
                            c.getInt((c.getColumnIndex(KEY_ACTIVE))),
                            null);
                    allLists.add(sl);
                    c.moveToPrevious();
                }
            }
        }
        return allLists;
    }

    /**
     * Grabs all items from the TABLE_ITEM that match the name of the list.
     * @param listName
     * @return
     */
    public ArrayList getAllItems(String listName) {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList allItems = new ArrayList();
        String selectQuery = "SELECT * FROM " + '"' + listName + TABLE_ITEM + '"';

        Cursor c = database.rawQuery(selectQuery, null);

        if (c.moveToLast()) {
            do {
                ShoppingListItem sli = new ShoppingListItem(c.getString(c.getColumnIndex(KEY_ITEM_NAME)),
                        c.getInt(c.getColumnIndex(KEY_QUANTITY)),
                        c.getInt(c.getColumnIndex(KEY_IF_FOUND)));
                allItems.add(sli);
            } while (c.moveToPrevious());
        }
        return allItems;
    }

    public void increaseQuantity(String listName, String itemName)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = " UPDATE " + '"' + listName + TABLE_ITEM + '"' + " SET " + KEY_QUANTITY + " = " + KEY_QUANTITY + " + 1 WHERE " + KEY_ITEM_NAME + " = " + '"' + itemName + '"';
        database.execSQL(selectQuery);
    }

    public void decreaseQuantity(String listName, String itemName)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = " UPDATE " + '"' + listName + TABLE_ITEM + '"' +  " SET " + KEY_QUANTITY + " = " + KEY_QUANTITY + " - 1 WHERE " + KEY_ITEM_NAME + " = " + '"' + itemName + '"';
        database.execSQL(selectQuery);
    }

    public void itemFound(String listName, String itemName)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        //String selectQuery = " SELECT " + KEY_IF_FOUND + " FROM " + listName + TABLE_ITEM + " WHERE " + KEY_ITEM_NAME + " = " + itemName;
        String selectQuery = " UPDATE " + '"' + listName + TABLE_ITEM + '"' + " SET " + KEY_IF_FOUND + " = 1 WHERE " + KEY_ITEM_NAME + " = " + '"' + itemName + '"';
        database.execSQL(selectQuery);
    }

    public void itemNotFound(String listName, String itemName)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        //String selectQuery = " SELECT " + KEY_IF_FOUND + " FROM " + listName + TABLE_ITEM + " WHERE " + KEY_ITEM_NAME + " = " + itemName;
        String selectQuery = " UPDATE " + '"' + listName + TABLE_ITEM + '"' + " SET " + KEY_IF_FOUND + " = 0 WHERE " + KEY_ITEM_NAME + " = " + '"' + itemName + '"';
        database.execSQL(selectQuery);
    }
}
