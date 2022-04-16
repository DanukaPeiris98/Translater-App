package com.example.languages;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mylist.db";
    public static final String TABLE_NAME = "mylist_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "ITEM1";

    public static final String TABLE_NAME2 = "lan_type";
    public static final String COL_1 = "LANG";
    public static final String COL_2 = "CODE";

    public static final String TABLE_NAME3 = "subscription_Lan";
    public static final String COLM_1 = "LANGUAGE";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + " ITEM1 TEXT)";
        db.execSQL(createTable);
        String createTable2 = "CREATE TABLE " + TABLE_NAME2 + "(LANG TEXT, " + " CODE TEXT)";
        db.execSQL(createTable2);
        String createTable3 = "CREATE TABLE " + TABLE_NAME3 + "(LANGUAGE TEXT )";
        db.execSQL(createTable3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME2);
        onCreate(db);
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME3);
        onCreate(db);
    }
//add words to database
    public boolean addData(String item1) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item1);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        return result != -1;
    }
//get word list from database
    public Cursor getWordList() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
//get language list from database
    public Cursor getLanList() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME2, null);
        return data;
    }
//get selected language from database
    public Cursor getSubLan() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME3, null);
        return data;
    }

//update edited words to database
    public boolean updateData(String oldItem, String updateItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, updateItem);
        db.update(TABLE_NAME, contentValues, "ITEM1 = ?", new String[]{oldItem});
        return true;
    }
//add languages to database
    public boolean addLang(String lan, String lanCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, lan);
        contentValues.put(COL_2, lanCode);
        long result = db.insert(TABLE_NAME2, null, contentValues);

        //if date as inserted incorrectly it will return -1
        return result != -1;
    }
//add subscribe languages to database
    public boolean addSubsLan(String subLan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLM_1, subLan);
        long result = db.insert(TABLE_NAME3, null, contentValues);

        //if date as inserted incorrectly it will return -1
        return result != -1;
    }
//check selected languages already in database
    public Cursor checkresult() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME3, null);
        return result;

    }
//delete selected languages before
    public void deleteSelected() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME3, null, null);
    }
//get language code from database
    public Cursor getCode(String language) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME2 + " WHERE LANG=?", new String[]{language});
        // db.execSQL("SELECT CODE FROM TABLE_NAME2 WHERE LANGUAGE=?",language);
        System.out.println(language);
        return data;
    }
}