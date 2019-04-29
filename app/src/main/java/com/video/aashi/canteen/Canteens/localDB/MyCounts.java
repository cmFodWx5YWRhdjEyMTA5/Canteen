package com.video.aashi.canteen.Canteens.localDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyCounts extends SQLiteOpenHelper {

    public static final String DB_NAME = "Counters";
    public static final String TABLES = "mycount";
    public static final String COLUMN_IDS = "ids";
    public static final String MYCOUNTS = "coun";
    public static final String POSITION = "pos";
    private static final int DB_VERSION = 1;
    public MyCounts(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLES
                + "(" + COLUMN_IDS +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + MYCOUNTS +
                " INTEGER, " + POSITION +
                " INTEGER " +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS mycount";
        db.execSQL(sql);
        onCreate(db);
    }
    public void addDatas(int pos, int counts)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(POSITION,pos);
        contentValues.put(MYCOUNTS, counts);
        db.insert(TABLES, null, contentValues);
        db.close();
    }
    public  void deleteTables()
    {

        SQLiteDatabase db = this.getWritableDatabase();
        String sql=  "DELETE FROM "+ TABLES ;
        db.execSQL(sql);
        db.close();

    }
    public Cursor getNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLES + " ORDER BY " + COLUMN_IDS + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public  void updateDatas(int id,int total)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLES + " SET "+ MYCOUNTS + "=" + total  + " WHERE "+ POSITION +" = " + id;
        db.execSQL(sql);
        db.close();
    }
    public Cursor getPosition(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLES + " WHERE " + POSITION + " = " + id;
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public Cursor getId(int  id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM mycount WHERE pos =" + id;
        Cursor  cursor = db.rawQuery(query,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

}
