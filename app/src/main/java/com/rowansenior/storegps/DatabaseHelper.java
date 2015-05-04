package com.rowansenior.storegps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Debug;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by root on 3/29/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    Context myContext;

    // Database Version
    private static final int DATABASE_VERSION = 14;
    // Database Name
    private static final String DATABASE_NAME = "storeGPS";
    // Table Names
    private static final String TABLE_LIST = "list";
    private static final String TABLE_ITEM = "_item";
    private static final String TABLE_STORE = "store";
    private static final String TABLE_NEARBY_STORE = "nearbystore";
    private static final String TABLE_STORE_ITEM = "storeitems";

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

    private static final String KEY_ITEM_TAG = "tags";
    private static final String KEY_ITEM_PRICE = "price";
    private static final String KEY_ITEM_LOCATION = "location";
    private static final String KEY_STORE = "store";

    // Table Create Statements
    private static final String CREATE_TABLE_STORE = "CREATE TABLE "
            + TABLE_STORE + "(" + KEY_STORE_NAME + " STRING PRIMARY KEY," + KEY_STORE_ICON
            + " INTEGER," + KEY_STORE_COLOR + " INTEGER," + KEY_LOCATION + " STRING," + KEY_PHONE_NUMBER +
            " STRING," + KEY_URL + " STRING," + KEY_HOUR_OPEN + " INTEGER," + KEY_HOUR_CLOSED + " INTEGER" + ")";

    private static final String CREATE_TABLE_NEARBY_STORE = "CREATE TABLE "
            + TABLE_NEARBY_STORE + "(" + KEY_STORE_NAME + " STRING PRIMARY KEY," + KEY_STORE_ICON
            + " INTEGER," + KEY_STORE_COLOR + " INTEGER," + KEY_LOCATION + " STRING," + KEY_PHONE_NUMBER +
            " STRING," + KEY_URL + " STRING," + KEY_HOUR_OPEN + " STRING," + KEY_HOUR_CLOSED + " STRING" + ")";

    private static final String CREATE_TABLE_STORE_ITEM = "CREATE TABLE " + TABLE_STORE_ITEM + "(" + KEY_ITEM_NAME
            + " STRING NOT NULL, " + KEY_STORE + " STRING NOT NULL, " + KEY_ITEM_TAG + " STRING, " + KEY_ITEM_PRICE + " STRING, " +
            KEY_ITEM_LOCATION + " STRING, PRIMARY KEY(" + KEY_ITEM_NAME + ", " + KEY_STORE + "))";

    private static DatabaseHelper instance;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;
    }

    public static synchronized DatabaseHelper getHelper(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_LIST);
        db.execSQL(CREATE_TABLE_STORE);
        db.execSQL(CREATE_TABLE_STORE_ITEM);
        db.execSQL(CREATE_TABLE_NEARBY_STORE);
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

    /**
     * Create a new ShoppingList within the database
     * <p/>
     * NEED TO HANDLE ERRORS FOR EXISTING LISTS
     */
    public long createNewList(String name, int color, int icon) {
        SimpleDateFormat s = new SimpleDateFormat("MM/dd/yyyy");
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
     * <p/>
     * NEED TO TEST FOR UNIQUE KEYS.
     * Not sure if sqlite will reject each item after the first for a list.
     * Might need to give item name column PK in the database, as well.
     * <p/>
     * NEED TO HANDLE ERRORS FOR EXISTING ITEMS
     * INCREMENT EXISTING
     */
    public void addNewItem(String listName, String itemName) {
        SQLiteDatabase dataBase = this.getReadableDatabase();

        String selectQuery = "INSERT INTO " + '"' + listName + TABLE_ITEM + '"' + " VALUES(" + '"' + itemName + '"' + ", 1, 0)";
        dataBase.execSQL(selectQuery);
    }

    /**
     * Removes row from the TABLE_ITEM.
     * Finds only rows with the same list name and item name.
     * <p/>
     * NEED ERROR HANDLING FOR ITEM THAT DOES NOT EXIST
     *
     * @param listName
     * @param itemName
     */
    public void removeItem(String listName, String itemName) {
        SQLiteDatabase dataBase = this.getReadableDatabase();
        String selectQuery = "DELETE FROM " + '"' + listName + TABLE_ITEM + '"' + " WHERE " + KEY_ITEM_NAME +
                " = " + '"' + itemName + '"';
        dataBase.execSQL(selectQuery);
    }

    /**
     * Removes a list by name.
     * On button press, get the name of the list and kill the table. Completely removes it.
     * <p/>
     * Will have to implement an archive button, which marks active as 0.
     *
     * @param listName
     */
    public void removeList(String listName) {
        SQLiteDatabase dataBase = this.getReadableDatabase();
        String selectQuery = "DELETE FROM " + TABLE_LIST + " WHERE " + KEY_NAME + " = " + '"' + listName + '"';
        dataBase.execSQL(selectQuery);
        dataBase.execSQL("DROP TABLE IF EXISTS " + '"' + listName + TABLE_ITEM + '"');
    }

    /**
     * Pulls in data from the database for ALL lists within the system.
     * Has to read out to getAllItems in order to fill an arraylist of items for each list.
     *
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

    public ArrayList<ShoppingList> getLast3Lists() {
        SQLiteDatabase dataBase = this.getReadableDatabase();
        ArrayList<ShoppingList> allLists = new ArrayList<ShoppingList>();
        String selectQuery = "SELECT  * FROM " + TABLE_LIST;

        Cursor c = dataBase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToLast()) {
            int lastPosition = c.getPosition();
            for (int i = lastPosition; i > (lastPosition - 3); i--) {

                if (i < 0) {
                    break;
                } else {
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

    public ArrayList<Store> get3ClosestStores() throws IOException {
        ArrayList<Store> allStores;
        allStores = getAllStores();
        StoreMergeSort sms = new StoreMergeSort(myContext, false);
        sms.mergeSort(allStores);
        ArrayList<Store> top3 = new ArrayList<>();
        if(allStores.size() < 3)
        {
            for(int i = 0; i < allStores.size(); i++)
            {
                top3.add(i, allStores.get(i));
            }
        }
        else
        {
            for(int i = 0; i < 3; i++)
            {
                top3.add(i, allStores.get(i));
            }
        }
        
        return top3;
    }

    public ArrayList<Store> get3NearbyStores() throws IOException {
        ArrayList<Store> allNearbyStores;
        allNearbyStores = getNearbyStores();
        StoreMergeSort sms = new StoreMergeSort(myContext, false);
        sms.mergeSort(allNearbyStores);
        ArrayList<Store> nearby3 = new ArrayList<>();
        if(allNearbyStores.size() < 3)
        {
            for(int i = 0; i < allNearbyStores.size(); i++)
            {
                nearby3.add(i, allNearbyStores.get(i));
            }
        }
        else
        {
            for(int i = 0; i < 3; i++)
            {
                nearby3.add(i, allNearbyStores.get(i));
            }
        }

        return nearby3;
    }
    public ArrayList<Store> getNearbyStores() {
        SQLiteDatabase dataBase = this.getReadableDatabase();
        ArrayList<Store> allStores = new ArrayList<Store>();
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_NEARBY_STORE;

            Cursor c = dataBase.rawQuery(selectQuery, null);

            if (c.moveToLast()) {
                do {
                    Store sl = new Store(c.getString(c.getColumnIndex(KEY_STORE_NAME)),
                            c.getString(c.getColumnIndex(KEY_LOCATION)),
                            c.getString(c.getColumnIndex(KEY_PHONE_NUMBER)),
                            c.getString(c.getColumnIndex(KEY_URL)),
                            c.getInt(c.getColumnIndex(KEY_HOUR_OPEN)),
                            c.getInt(c.getColumnIndex(KEY_HOUR_CLOSED)));
                    allStores.add(sl);
                } while (c.moveToPrevious());
            }
        }
        catch (Exception e) {

        }
        return allStores;
    }

    /**
     * Grabs all items from the TABLE_ITEM that match the name of the list.
     *
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

    public void increaseQuantity(String listName, String itemName) {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = " UPDATE " + '"' + listName + TABLE_ITEM + '"' + " SET " + KEY_QUANTITY + " = " + KEY_QUANTITY + " + 1 WHERE " + KEY_ITEM_NAME + " = " + '"' + itemName + '"';
        database.execSQL(selectQuery);
    }

    public void decreaseQuantity(String listName, String itemName) {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = " UPDATE " + '"' + listName + TABLE_ITEM + '"' + " SET " + KEY_QUANTITY + " = " + KEY_QUANTITY + " - 1 WHERE " + KEY_ITEM_NAME + " = " + '"' + itemName + '"';
        database.execSQL(selectQuery);
    }

    public void itemFound(String listName, String itemName) {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = " UPDATE " + '"' + listName + TABLE_ITEM + '"' + " SET " + KEY_IF_FOUND + " = 1 WHERE " + KEY_ITEM_NAME + " = " + '"' + itemName + '"';
        database.execSQL(selectQuery);
    }

    public void itemNotFound(String listName, String itemName) {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = " UPDATE " + '"' + listName + TABLE_ITEM + '"' + " SET " + KEY_IF_FOUND + " = 0 WHERE " + KEY_ITEM_NAME + " = " + '"' + itemName + '"';
        database.execSQL(selectQuery);
    }

    public void addNewFavoriteStore(String storeName, String phoneNumber, String uRL, int open, int closed, String location) {
        SQLiteDatabase dataBase = this.getReadableDatabase();
        String selectQuery = "INSERT INTO " + TABLE_STORE + " VALUES(" + '"' + storeName + '"' + ", 1, 1, " + '"' + location + '"' + ", " + '"' + phoneNumber + '"' + ", " + '"' + uRL + '"' + ", " + open + ", " + closed + ")";
        dataBase.execSQL(selectQuery);
    }

    public void removeFavoriteStore(String storeName) {
        SQLiteDatabase dataBase = this.getReadableDatabase();
        String selectQuery = "DELETE FROM " + TABLE_STORE + " WHERE " + KEY_STORE_NAME + " = " + '"' + storeName + '"';
        dataBase.execSQL(selectQuery);
    }

    public ArrayList<Store> getAllStores() {
        SQLiteDatabase dataBase = this.getReadableDatabase();
        ArrayList<Store> allStores = new ArrayList<Store>();
        String selectQuery = "SELECT  * FROM " + TABLE_STORE;

        Cursor c = dataBase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToLast()) {
            do {
                Store sl = new Store(c.getString(c.getColumnIndex(KEY_STORE_NAME)),
                        c.getString(c.getColumnIndex(KEY_LOCATION)),
                        c.getString(c.getColumnIndex(KEY_PHONE_NUMBER)),
                        c.getString(c.getColumnIndex(KEY_URL)),
                        c.getInt(c.getColumnIndex(KEY_HOUR_OPEN)),
                        c.getInt(c.getColumnIndex(KEY_HOUR_CLOSED)));
                allStores.add(sl);
            } while (c.moveToPrevious());
        }
        return allStores;
    }

    public boolean isStoreFavorited(Store store) {
        boolean exitState;
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT EXISTS(SELECT 1 FROM " + TABLE_STORE + " WHERE " + KEY_STORE_NAME + "=" + '"' + store.getName() + '"' + " LIMIT 1)";
        Cursor c = database.rawQuery(selectQuery, null);
        c.moveToFirst();
        if(c.getInt(0) == 0) {
            exitState = false;
        }
        else {
            exitState = true;
        }
        return exitState;
    }

    public Store getStoreInfo(String storeName) {
        SQLiteDatabase dataBase = this.getReadableDatabase();
        String selectQuery;
        Cursor c = null;
        try {
            selectQuery = "SELECT  * FROM " + TABLE_STORE + " WHERE " + '"' + KEY_STORE_NAME + '"' + " = " + '"' + storeName + '"';
            c = dataBase.rawQuery(selectQuery, null);
            if(c.getCount() == 0) {
                selectQuery = "SELECT  * FROM " + TABLE_NEARBY_STORE + " WHERE " + '"' + KEY_STORE_NAME + '"' + " = " + '"' + storeName + '"';
                c = dataBase.rawQuery(selectQuery, null);
            }
        }
        catch (Exception e) {
        }
        c.moveToFirst();
        Store storeToReturn = new Store(c.getString(c.getColumnIndex(KEY_STORE_NAME)),
                c.getString(c.getColumnIndex(KEY_LOCATION)),
                c.getString(c.getColumnIndex(KEY_PHONE_NUMBER)),
                c.getString(c.getColumnIndex(KEY_URL)),
                c.getInt(c.getColumnIndex(KEY_HOUR_OPEN)),
                c.getInt(c.getColumnIndex(KEY_HOUR_CLOSED)));

        return storeToReturn;
    }
}