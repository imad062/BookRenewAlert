package com.example.imad.bookrenewalert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.Date;

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String databaseName = "BookRenewAlert";
    private static final String databaseTableName = "Books";
    private static final String databaseColOne = "id";
    private static final String databaseColTwo = "name";
    private static final String databaseColThree = "issueDate";
    private static final String databaseColFour = "renewDate";
    private static final String databaseColFive = "lastRenewed";
    private static final String databaseColSix = "difference";

    DateTime prevRenew;
    int curDay, curMonth, curYear, renDay, renMon, renYear;


    DatabaseHelper(Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + databaseTableName + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + "NAME TEXT, " + "ISSUEDATE TEXT, " + "RENEWDATE TEXT, " + "LASTRENEWED TEXT, " + "DIFFERENCE TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + databaseTableName);
    }

    boolean insertData(String bookName, String issueDate, String renewDate, String difference) {
        long result;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(databaseColTwo, bookName);
        contentValues.put(databaseColThree, issueDate);
        contentValues.put(databaseColFour, renewDate);
        contentValues.put(databaseColFive, "1");
        contentValues.put(databaseColSix, difference);

        result = db.insert(databaseTableName, null, contentValues);

        return result != -1;

    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor data = db.rawQuery(" SELECT * FROM " + databaseTableName, null);
        return data;
    }

    public void deleteData(String bookName) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(databaseTableName, " name = ? ", new String[]{bookName});
    }

    public void getCurrentDateInInt(String current)
    {
        String date = "", month = "", year = "";
        int nDate, nMonth, nYear;
        for(int i = 0; i < current.length(); i++)
        {
            if(current.charAt(i) != '-')
            {
                date = date + current.charAt(i);
            }
            else
            {
                for(int j = i + 1; j < current.charAt(j); j++)
                {
                    if(current.charAt(j) != '-')
                    {
                        month = month + current.charAt(j);
                    }
                    else
                    {
                        for(int k = j + 1; k < current.charAt(k); k++)
                        {
                            year = year + current.charAt(k);
                        }
                        nDate = Integer.parseInt(date);
                        nMonth = Integer.parseInt(month);
                        nYear = Integer.parseInt(year);

                        curDay = nDate;
                        curMonth = nMonth - 1;
                        curYear = nYear;

                        Log.d("integerCur", "day = "+nDate + " month = " + curMonth + " year = " + nYear);
                    }
                }
            }
        }
    }

    public void getRenewDateInInt(String renew)
    {
        String date = "", month = "", year = "";
        int nDate, nMonth, nYear;
        for(int i = 0; i < renew.length(); i++)
        {
            if(renew.charAt(i) != '-')
            {
                date = date + renew.charAt(i);
            }
            else
            {
                for(int j = i + 1; j < renew.charAt(j); j++)
                {
                    if(renew.charAt(j) != '-')
                    {
                        month = month + renew.charAt(j);
                    }
                    else
                    {
                        for(int k = j + 1; k < renew.charAt(k); k++)
                        {
                            year = year + renew.charAt(k);
                        }
                        nDate = Integer.parseInt(date);
                        nMonth = Integer.parseInt(month);
                        nYear = Integer.parseInt(year);

                        renDay = nDate;
                        renMon = nMonth - 1;
                        renYear = nYear;

                        Log.d("integer Renew", "day = "+nDate + " month = " + renMon + " year = " + nYear);
                    }
                }
            }
        }
    }

    public void getDateFromLastRenew(String lastRenew)
    {
        String date = "", month = "", year = "";
        int nDate, nMonth, nYear;
        for(int i = 0; i < lastRenew.length(); i++)
        {
            if(lastRenew.charAt(i) != '-')
            {
                date = date + lastRenew.charAt(i);
            }
            else
            {
                for(int j = i + 1; j < lastRenew.charAt(j); j++)
                {
                    if(lastRenew.charAt(j) != '-')
                    {
                        month = month + lastRenew.charAt(j);
                    }
                    else
                    {
                        for(int k = j + 1; k < lastRenew.charAt(k); k++)
                        {
                            year = year + lastRenew.charAt(k);
                        }
                        nDate = Integer.parseInt(date);
                        nMonth = Integer.parseInt(month);
                        nYear = Integer.parseInt(year);
                        DateTime time = new DateTime(nYear, nMonth, nDate, 0, 0, 0);
                        prevRenew = time;

                        Log.d("getDataFromRenew", "day = "+nDate + " month = " + nMonth + " year = " + nYear);
                    }
                }
            }
        }
    }

    public void updateRenew(String bookName, String todayDate, DateTime todayDatee)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content = new ContentValues();

        Cursor allData = getAllData();
        while(allData.moveToNext())
        {
            if(allData.getString(1).equals(bookName))
            {
                String lastRenewDate = allData.getString(4);
                String difference = allData.getString(5);

                //renewDate 3, lastRenewDate 4
                //renewDate = toDate + difference
                //lastRenewDate = toDate

                //first truncating the ending and beginning characters of diff and converting string to int
                String diff = difference.substring(1, difference.length()-1);
                int addDays = Integer.parseInt(diff);//got difference


                //adding the days with the todayDatee
                DateTime dateTime = todayDatee.plusDays(addDays);




                break;
            }
        }
    }
}
