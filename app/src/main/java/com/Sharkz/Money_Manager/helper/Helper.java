package com.Sharkz.Money_Manager.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class Helper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    static final String DATABASE_NAME = "moneydb";

    public Helper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE = "CREATE TABLE Records (id INTEGER PRIMARY KEY autoincrement, name TEXT NOT NULL, jumlah TEXT NOT NULL, tanggal TEXT NOT NULL, label TEXT NOT NULL, type TEXT NOT NULL, aset TEXT NOT NULL)";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
        final String SQL_CREATE_TABLE_ASET = "CREATE TABLE Aset (id INTEGER PRIMARY KEY autoincrement, name_aset TEXT NOT NULL, create_date TEXT NOT NULL, label TEXT NOT NULL, total TEXT NOT NULL)";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_ASET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Records");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Aset");
        onCreate(sqLiteDatabase);
    }

    public ArrayList<HashMap<String, String>> getAll(){
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        String QUERY = "SELECT * FROM Records ORDER BY tanggal DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(QUERY, null);
        if (cursor.moveToFirst()){
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(0));
                map.put("name", cursor.getString(1));
                map.put("jumlah", cursor.getString(2));
                map.put("tanggal", cursor.getString(3));
                map.put("label", cursor.getString(4));
                map.put("type", cursor.getString(5));
                map.put("aset", cursor.getString(6));
                list.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void insertRecords (String name, String jumlah, String tanggal, String label, String type, String aset){
        SQLiteDatabase database = this.getWritableDatabase();
        String QUERY = "INSERT INTO Records (name, jumlah, tanggal, label, type, aset) VALUES ('"+name+"','"+jumlah+"','"+tanggal+"','"+label+"','"+type+"','"+aset+"')";
        database.execSQL(QUERY);
    }

    public void updateRecords (int id, String name, String jumlah, String tanggal, String label, String type, String aset){
        SQLiteDatabase database = this.getWritableDatabase();
        String QUERY = "UPDATE Records SET name ='"+name+"', jumlah ='"+jumlah+"', tanggal ='"+tanggal+"', label ='"+label+"', type ='"+type+"', aset ='"+aset+"' WHERE id = "+id;
        database.execSQL(QUERY);
    }

    public void deleteRecords (int id){
        SQLiteDatabase database = this.getWritableDatabase();
        String QUERY = "DELETE FROM Records WHERE id = "+id;
        database.execSQL(QUERY);
    }

}
