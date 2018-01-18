package com.example.imad.bookrenewalert;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                TextView titleText = (TextView) new TextView(getApplicationContext());
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
                                Toast.makeText(getApplicationContext(), "ADDED", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "CANCELLED", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });

                AlertDialog alertDialog = alert.create();
                Log.d("BeforShow", "HEREEEE");
                alertDialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
