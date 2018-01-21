package com.example.imad.bookrenewalert;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper database;
    ArrayAdapter<String> arrayAdapter;
    int currDate,currMonth,currYear, setDate, setMonth,setYear;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new DatabaseHelper(this);
        listView = (ListView) findViewById(R.id.listview);

        showCurrentDate();
        updateListView(database.getAllData());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_add_button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.button_addBook_menu:

                clickedOnAddBookMenuItem();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Add book in the database
    public void clickedOnAddBookMenuItem()
    {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        TextView titleText = new TextView(getApplicationContext());
        titleText.setText("Add Book and Renew Date");
        titleText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        titleText.setGravity(Gravity.CENTER);
        titleText.setPadding(10,10,10,10);
        titleText.setTypeface(null, Typeface.BOLD);

        alert.setCustomTitle(titleText)
                .setView(R.layout.alertdialog_view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_LONG).show();

                        Dialog dialog1 = (Dialog) dialog;
                        TextView setDate = dialog1.findViewById(R.id.txt_currentDate_alertdialog);
                        EditText bookname = dialog1.findViewById(R.id.edit_bookname_alertdialog);

                        String bookName = bookname.getText().toString();
                        String issueDate = getCurrentDate();
                        String renewDate = setDate.getText().toString();

                        DateTime start = new DateTime(currYear, currMonth+1, currDate, 0, 0, 0);
                        DateTime end = new DateTime(setYear,setMonth+1,currDate, 0,0,0);
                        String difference = Days.daysBetween(start,end).toString();

                        Log.d("AlertOnClick", "Bookname = " + bookName + " issueDate = " + issueDate + " renewdate = " + renewDate + " difference = " + difference);
                        database.insertData(bookName, issueDate, renewDate);
                        updateListView(database.getAllData());


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
                    }
                })
                .setNeutralButton("Default", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Dialog dialog1 = (Dialog) dialog;
                        EditText bookname = dialog1.findViewById(R.id.edit_bookname_alertdialog);


                        Toast.makeText(getApplicationContext(), "Default Selceted", Toast.LENGTH_SHORT).show();
                    }
                });


        final AlertDialog alertDialog = alert.create();
        alertDialog.show();

        //Get the textview and datepicker inside the alertdialog
        final TextView currentDateSet = alertDialog.findViewById(R.id.txt_currentDate_alertdialog);
        final DatePicker datePicker = alertDialog.findViewById(R.id.datePicker_alertdialog);

        //get current date to initialize the datepicker
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int dmonth = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);

        //datepicker initializer
        datePicker.init(year, month, dmonth, new DatePicker.OnDateChangedListener() {

            //set the textview on the current set date
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                setDate = dayOfMonth;
                setYear = year;
                setMonth = monthOfYear;

                int month = monthOfYear+1;
                currentDateSet.setText("" + dayOfMonth + "-" + month + "-" + year);

            }
        });
    }

    //TODO: Renew has to do automatic renew,meaning it has to find the (renew-issue) and add it to renew,update last renewed
    //click listener for book renewed button
    public void clickedRenew(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        TextView textView = new TextView(getApplicationContext());
        textView.setText("Have You renewed the Book?");
        textView.setPadding(10,10,10,10);
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(Color.rgb(0,153,51));

        //TODO: show the bookname, added on, last renewed in a CustomView
        builder.setCustomTitle(textView)
                .setView(R.layout.renew_book_alertdialog)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //TODO: have to get current date in string format (Hint: getCurrentDate() )

                        //TODO: add difference with current date to get next renew date (diff is in database)

                        //TODO: update the last Renew date with todays date

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();


    }

    //delete book from database on button delete
    public void clickedDelete(View view)
    {
        final View view1 = view;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        TextView textView = new TextView(getApplicationContext());
        textView.setText("Have You retured the book?");
        textView.setPadding(10,10,10,10);
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(Color.RED);

        builder.setCustomTitle(textView)
                .setView(R.layout.delete_book_alertdialog)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        View parent = (View) view1.getParent();
                        TextView bookname = (TextView) parent.findViewById(R.id.txt_bookname_listview);
                        String bookName = bookname.getText().toString();
                        database.deleteData(bookName);
                        updateListView(database.getAllData());

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //showing the bookname and addedOn and renewedOn in the alertdialog
        final TextView book = alertDialog.findViewById(R.id.bookname_deleteBook_alertDialog);
        final TextView addedOn = alertDialog.findViewById(R.id.txt_addedOn_delete_alertdialog);
        final TextView renewedOn = alertDialog.findViewById(R.id.txt_lastRenewed_delete_alertdialog);

        //TODO: give warning in help to not give duplicate booknames
        View parent = (View) view1.getParent();
        final TextView bookName = (TextView) parent.findViewById(R.id.txt_bookname_listview);
        String bookname = bookName.getText().toString();

        //As i dont no SQL, i have to use brute force
        Cursor data = database.getAllData();
        while (data.moveToNext())
        {
            if(data.getString(1).equals(bookname))
            {
                book.setText(bookname);
                addedOn.setText(data.getString(2));
                renewedOn.setText(data.getString(3));
                Log.d("deleteBook", "name = " + bookname + " added = " + data.getString(2) + " renewed = " + data.getString(3));
                break;
            }
        }

        //addedOn.setText(data.getString(2));
        //renewedOn.setText(data.getString(3));

    }

    //gets current date in String form
    public String getCurrentDate()
    {
        int curDate;
        int curMonth;
        int curYear;

        Calendar calendar = Calendar.getInstance();
        curDate = calendar.get(Calendar.DAY_OF_MONTH);
        curMonth = calendar.get(Calendar.MONTH);
        curYear = calendar.get(Calendar.YEAR);

        //setting the global variables for use in difference in database
        currDate = curDate;
        currMonth = curMonth;
        currYear = curYear;

        //month is 0 indexed in calendar class
        int month = curMonth + 1;

        String string = "" + curDate + "-" + month + "-" + curYear;
        //Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
        Log.d("getCurrentDate", string);

        return string;
    }

    //shows current date with name of day and name of month in mainactivity
    public void showCurrentDate()
    {
        String curDate;
        String longDay, longMonth;
        String longDate;
        String longYear;

        Calendar calendar = Calendar.getInstance();
        longDay = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        longMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        longDate = dateFormat.format(calendar.getTime());

        dateFormat.applyPattern("yyyy");
        longYear = dateFormat.format(calendar.getTime());

        Log.d("currentDate", ""+longDay+longDate+longMonth+longYear);

        curDate = longDay + ", " + longDate + " " + longMonth + " " + longYear;

        TextView txtShowCurDate = (TextView) findViewById(R.id.textView2);
        txtShowCurDate.setText(curDate);
    }

    //get name of day from given input number of day
    public String getDayFromInput(int day)
    {
        String nameOfDay = null;

        if(day == 1)
            nameOfDay = "Sunday";
        else if(day == 2)
            nameOfDay = "Monday";
        else if(day == 3)
            nameOfDay = "Tuesday";
        else if(day == 4)
            nameOfDay = "Wednesday";
        else if(day == 5)
            nameOfDay = "Thursday";
        else if(day == 6)
            nameOfDay = "Friday";
        else if(day == 7)
            nameOfDay = "Saturday";

        return nameOfDay;
    }

    //get name of month from given number of month
    public String getMonthFromInput(int month)
    {
        String nameOfMonth = null;

        if(month == 0)
            nameOfMonth = "January";
        else if(month == 1)
            nameOfMonth = "February";
        else if(month == 2)
            nameOfMonth = "March";
        else if(month == 3)
            nameOfMonth = "April";
        else if(month == 4)
            nameOfMonth = "May";
        else if(month == 5)
            nameOfMonth = "June";
        else if(month == 6)
            nameOfMonth = "July";
        else if(month == 7)
            nameOfMonth = "August";
        else if(month == 8)
            nameOfMonth = "September";
        else if (month == 9)
            nameOfMonth = "October";
        else if (month == 10)
            nameOfMonth = "November";
        else if(month == 11)
            nameOfMonth = "December";

        return nameOfMonth;
    }

    //updates listview in mainactivity
    public void updateListView(Cursor data)
    {
        ArrayList<String> arrayList = new ArrayList<>();

        while (data.moveToNext())
        {
            arrayList.add(data.getString(1));
        }
        if(arrayAdapter == null)
        {
            arrayAdapter = new ArrayAdapter<String>(MainActivity.this,R.layout.item_book_name,R.id.txt_bookname_listview, arrayList);
            listView.setAdapter(arrayAdapter);
        }
        else
        {
            arrayAdapter.clear();
            arrayAdapter.addAll();
            arrayAdapter.notifyDataSetChanged();
            arrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.item_book_name, R.id.txt_bookname_listview, arrayList);
            listView.setAdapter(arrayAdapter);
        }

        data.close();
        database.close();
    }

}
