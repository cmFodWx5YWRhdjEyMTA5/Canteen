package com.video.aashi.canteen.Canteens.localDB;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class ItemLocal extends SQLiteOpenHelper {

    public static final String DB_NAME = "Products";
    private static final int DB_VERSION = 3;
    public static final String TABLE_NAME = "items";
    public static final String COLUMN_ID = "id";
    public static final String ITEM_ID = "itemId";
    public  static final String GROUP_NAME = "groupName";
    public  static final String PRICE = "itemPrice";
    public  static final String ITEM_NAME = "itemName";

    public ItemLocal(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + TABLE_NAME
                + "(" + COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + ITEM_ID +
                " VARCHAR, " + GROUP_NAME +
                " VARCHAR, " + PRICE +
                " VARCHAR, " + ITEM_NAME +
                " VARCHAR " +
                ");";



        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS items";
        db.execSQL(sql);


        onCreate(db);

    }
    public boolean addItems(String id, String groupname,String itemName, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_ID,id);
        contentValues.put(GROUP_NAME ,groupname);
        contentValues.put(PRICE,price);
        contentValues.put(ITEM_NAME,itemName);
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }
    public  void update(String id, String groupname,String itemName, String price)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME + " SET " +  GROUP_NAME + " = " + "'" +  groupname +  "'" + " , " + PRICE + " = " + price  + " , " + ITEM_NAME + " = " +
                "'" + itemName + "'" +" WHERE " + ITEM_ID + " = " + id;
        db.execSQL(sql);
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
    public Cursor getId(String  id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM items WHERE itemId = " + id;
        Cursor  cursor = db.rawQuery(query,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void   updateTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE items SET groupName = BREVERAGES , itemPrice = 15 , itemName = COKE WHERE itemId = 30";
        db.execSQL(sql);

    }

    public Cursor  getitemBygroup(String groupname)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + GROUP_NAME + " = " + "'" +groupname+"'" ;
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

}
