package com.example.shiza.muslimmemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    CheckBox checkBox;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Muslim Memo");
        checkBox = (CheckBox)findViewById(R.id.notification_check);

        sharedPreferences = this.getSharedPreferences("SETTINGS",MODE_PRIVATE);

        if ( sharedPreferences.getBoolean("NOTIFICATIONS",true))
        {
            checkBox.setChecked(true);
        }

    }

    public void itemClicked(View v) {
        //code to check if this checkbox is checked!
        if(((CheckBox)v).isChecked()){

            sharedPreferences.edit().putBoolean("NOTIFICATIONS",true).apply();

            Toast.makeText(this,"You will get notifications from MuslimMemo.",Toast.LENGTH_LONG).show();
        }
        else
        {
            sharedPreferences.edit().putBoolean("NOTIFICATIONS",false).apply();

            Toast.makeText(this,"You will not get any notifications from MuslimMemo.",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.back) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
