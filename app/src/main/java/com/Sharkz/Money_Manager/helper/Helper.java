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

    private static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "moneydb";

    public Helper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE = "CREATE TABLE users (id INTEGER PRIMARY KEY autoincrement, name TEXT NOT NULL, jumlah TEXT NOT NULL, tanggal TEXT NOT NULL, label TEXT NOT NULL)";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS users");
        onCreate(sqLiteDatabase);
    }

    public ArrayList<HashMap<String, String>> getAll(){
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        String QUERY = "SELECT * FROM users";
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
                list.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void insert (String name, String jumlah, String tanggal, String label){
        SQLiteDatabase database = this.getWritableDatabase();
        String QUERY = "INSERT INTO users (name, jumlah, tanggal, label) VALUES ('"+name+"','"+jumlah+"','"+tanggal+"','"+label+"')";
        database.execSQL(QUERY);
    }

    public void update (int id, String name, String jumlah, String tanggal, String label){
        SQLiteDatabase database = this.getWritableDatabase();
        String QUERY = "UPDATE users SET name ='"+name+"', jumlah ='"+jumlah+"', tanggal ='"+tanggal+"', label ='"+label+"' WHERE id = "+id;
        database.execSQL(QUERY);
    }

    public void delete (int id){
        SQLiteDatabase database = this.getWritableDatabase();
        String QUERY = "DELETE FROM users WHERE id = "+id;
        database.execSQL(QUERY);
    }

}
