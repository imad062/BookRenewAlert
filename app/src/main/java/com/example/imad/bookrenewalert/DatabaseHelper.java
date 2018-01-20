package com.example.imad.bookrenewalert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class DatabaseHelper extends SQLiteOpenHelper {

    private static final String databaseName = "BookRenewAlert";
    private static final String databaseTableName = "Books";
    private static final String databaseColOne = "id";
    private static final String databaseColTwo = "name";
    private static final String databaseColThree = "issueDate";
    private static final String databaseColFour = "renewDate";
    private static final String databaseColFive = "lastRenewed";


    DatabaseHelper(Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + databaseTableName + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + "NAME TEXT, " + "ISSUEDATE TEXT, " + "RENEWDATE TEXT, " + "LASTRENEWED TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + databaseTableName);
    }

    boolean insertData(String bookName, String issueDate, String renewDate)
    {
        long result;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(databaseColTwo, bookName);
        contentValues.put(databaseColThree, issueDate);
        contentValues.put(databaseColFour, renewDate);
        contentValues.put(databaseColFive, "1");
        result = db.insert(databaseTableName, null, contentValues);

        return result != -1;

    }

    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor data = db.rawQuery(" SELECT * FROM " + databaseTableName, null);
        return data;
    }

    public void deleteData(String bookName)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(databaseTableName, " name = ? ", new String[]{bookName});
    }
}
