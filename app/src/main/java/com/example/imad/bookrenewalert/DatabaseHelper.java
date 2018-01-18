package com.example.imad.bookrenewalert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    static final String databaseName = "BookRenewAlert";
    static final String databaseTableName = "Books";
    static final String databaseColOne = "id";
    static final String databaseColTwo = "name";
    static final String databaseColThree = "issueDate";
    static final String databaseColFour = "renewDate";


    public DatabaseHelper(Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + databaseTableName + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + "NAME TEXT, " + "ISSUEDATE TEXT, " + "RENEWDATE TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + databaseTableName);
    }

    public boolean insertData(String bookName, String issueDate, String renewDate)
    {
        long result;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(databaseColTwo, bookName);
        contentValues.put(databaseColThree, issueDate);
        contentValues.put(databaseColFour, renewDate);

        result = db.insert(databaseTableName, null, contentValues);

        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }

    }

    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor data = db.rawQuery(" GET * FROM " + databaseTableName, null);
        return data;
    }
}
