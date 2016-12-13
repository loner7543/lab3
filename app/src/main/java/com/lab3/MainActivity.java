package com.lab3;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lab3.db.DbUtils;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MainActivity";
    private Intent LaunchIntent;
    private Button StatButton;
    private DbUtils dbHelper;
    private SQLiteDatabase database;
    private ContentValues contentValues;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatButton = (Button) findViewById(R.id.StatisticsButton);
        context = getApplicationContext();
        dbHelper = new DbUtils(this,DbUtils.DATABASE_NAME,DbUtils.DATABASE_VERSION);
        database = dbHelper.getWritableDatabase();//вызввает onCreate() и бд на запись
        context.deleteDatabase(DbUtils.DATABASE_NAME);
        contentValues = new ContentValues();
    }

    public void onShowStat(View view){
        LaunchIntent = new Intent(this,StatisticsActivity.class);
        startActivity(LaunchIntent);
    }

    public void onShowCatigories(View view){
        LaunchIntent = new Intent(this,CatrgoryActivity.class);
        startActivity(LaunchIntent);
    }

    public void onShowPhoto(View view){
        LaunchIntent = new Intent(this,PhotoActivity.class);
        startActivity(LaunchIntent);
    }

    public void onShowTimeRecords(View view){
        LaunchIntent = new Intent(this,TimeRecordScrollingActivity.class);
        startActivity(LaunchIntent);
    }
}
