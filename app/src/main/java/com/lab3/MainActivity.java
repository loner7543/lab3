package com.lab3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Intent LaunchIntent;
    private Button StatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatButton = (Button) findViewById(R.id.StatisticsButton);
    }

    public void onShowStat(View view){
        LaunchIntent = new Intent(this,StatisticsActivity.class);
        startActivity(LaunchIntent);
    }
}
