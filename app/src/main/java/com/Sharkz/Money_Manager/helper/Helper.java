package com.Sharkz.Money_Manager.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class Helper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 9;
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
        final String SQL_CREATE_TABLE_LABEL = "CREATE TABLE Label (id INTEGER PRIMARY KEY autoincrement, name_label TEXT NOT NULL, type TEXT NOT NULL, drawable TEXT NOT NULL, category TEXT NOT NULL)";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_LABEL);
        String QUERY1 = "INSERT INTO Aset (name_aset, create_date, label, total) VALUES ('Cash','24/09/1999','Food','12400')";
        sqLiteDatabase.execSQL(QUERY1);
        String QUERY2 = "INSERT INTO Aset (name_aset, create_date, label, total) VALUES ('Bank Jp','24/09/1999','Transport','42300')";
        sqLiteDatabase.execSQL(QUERY2);
        String QUERY3 = "INSERT INTO Aset (name_aset, create_date, label, total) VALUES ('Invest','24/09/1999','Sport','56300')";
        sqLiteDatabase.execSQL(QUERY3);
        String QUERY_label1 = "INSERT INTO Label (name_label, type, drawable, category) VALUES ('Def','EXP','Food','Food')";
        sqLiteDatabase.execSQL(QUERY_label1);
        String QUERY_label2 = "INSERT INTO Label (name_label, type, drawable, category) VALUES ('Def','EXP','Tax','Money')";
        sqLiteDatabase.execSQL(QUERY_label2);
        String QUERY_label3 = "INSERT INTO Label (name_label, type, drawable, category) VALUES ('Def','EXP','Sport','Sport & Health')";
        sqLiteDatabase.execSQL(QUERY_label3);
        String QUERY_label4 = "INSERT INTO Label (name_label, type, drawable, category) VALUES ('Def','EXP','Transport','Transportation')";
        sqLiteDatabase.execSQL(QUERY_label4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Records");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Aset");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Label");
        onCreate(sqLiteDatabase);
    }

    public ArrayList<HashMap<String, String>> getAll(String Sort){
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        String QUERY = "SELECT * FROM Records ORDER BY tanggal "+Sort+" ";
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
    public ArrayList<HashMap<String, String>> getAllAset(){
        ArrayList<HashMap<String, String>> list2 = new ArrayList<>();
        String QUERY = "SELECT * FROM Aset ORDER BY name_aset DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(QUERY, null);
        if (cursor.moveToFirst()){
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(0));
                map.put("name_aset", cursor.getString(1));
                map.put("create_date", cursor.getString(2));
                map.put("label", cursor.getString(3));
                map.put("total", cursor.getString(4));
                list2.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list2;
    }
    public ArrayList<HashMap<String, String>> getAll_Exp_Inc(String TypeEI){
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        String QUERY = "SELECT * FROM Records WHERE type ='"+TypeEI+"' ";
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

    public ArrayList<HashMap<String, String>> getAllLabel_Exp_Inc(String TypeEI){
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        String QUERY = "SELECT * FROM Label WHERE type ='"+TypeEI+"' ORDER BY category DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(QUERY, null);
        if (cursor.moveToFirst()){
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(0));
                map.put("name_label", cursor.getString(1));
                map.put("type", cursor.getString(2));
                map.put("drawable", cursor.getString(3));
                map.put("category", cursor.getString(4));
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


    public void updateTotalAsets (String name_aset, String total){
        SQLiteDatabase database = this.getWritableDatabase();
        String QUERY = "UPDATE Aset SET total ='"+total+"' WHERE name_aset ='"+name_aset+"'";
        database.execSQL(QUERY);
    }

    public void insertLabel (String nama_label, String type, String drawable, String category){
        SQLiteDatabase database = this.getWritableDatabase();
        String QUERY = "INSERT INTO Label (name_label, type, drawable, category) VALUES ('"+nama_label+"','"+type+"','"+drawable+"','"+category+"')";
        database.execSQL(QUERY);
    }

}
