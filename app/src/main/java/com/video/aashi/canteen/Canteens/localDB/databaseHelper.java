package com.video.aashi.canteen.Canteens.localDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class databaseHelper extends SQLiteOpenHelper

{

    public static final String DB_NAME = "OrderItems";
    public static final String TABLE_NAME = "items";
    public static final String COLUMN_ID = "id";
    public static final String ITEM_ID = "itemId";
    public  static final String GROUP_NAME = "groupName";
    public  static final String PRICE = "price";
    public  static final String ITEM_PRICE = "total";
    public  static final String ITEM_NAME = "itemName";
    public static final String COLUMN_STATUS = "status";
    public static final String COUNT = "count";

    private static final int DB_VERSION = 1;
    public databaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql = "CREATE TABLE " + TABLE_NAME
                + "(" + COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + ITEM_ID +
                " INTEGER, " + COLUMN_STATUS +
                " TINYINT, " + GROUP_NAME +
                " VARCHAR, " + PRICE +
                " VARCHAR, " + ITEM_PRICE +
                " INTEGER, " + ITEM_NAME +
                " VARCHAR, " + COUNT +
                 " INTEGER " +
                  ");";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String sql = "DROP TABLE IF EXISTS Persons";
        db.execSQL(sql);
        onCreate(db);
    }

    public boolean addItems(String id, int status,String groupname,String itemName,
                            String price,String itemprice, String count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_ID,id);
        contentValues.put(COLUMN_STATUS, status);
        contentValues.put(GROUP_NAME ,groupname);
        contentValues.put(PRICE,price);
        contentValues.put(ITEM_PRICE,itemprice);
        contentValues.put(ITEM_NAME,itemName);
        contentValues.put(COUNT, count);
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }
    public  void update(String count,String id,String total)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME + " SET "+ COUNT + "=" + count  + " , " + PRICE + " = " + total + " WHERE "+ ITEM_ID +" = " + id;
        db.execSQL(sql);
        db.close();
    }
    public void updateData(String count,String id,String price,String marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COUNT,count);
        db.update(TABLE_NAME, contentValues, "itemId = ?",new String[] { id });
        db.close();
    }
    public Cursor getNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=  "DELETE FROM "+ TABLE_NAME ;
        db.execSQL(sql);
        db.close();
    }
    public int dbSyncCount()
    {
        int count = 0;
        String selectQuery = "SELECT  * FROM items where udpateStatus = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        database.close();
        return count;

    }
    public Cursor getUnsyncedNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public Cursor getId(String  id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM items WHERE itemId= " + id;
        Cursor  cursor = db.rawQuery(query,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public  void deleteRow(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "DELETE FROM items WHERE itemId= " + id;
        db.execSQL(query);
        db.close();
    }




}
