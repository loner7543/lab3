package com.lab3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

public class StatisticsActivity extends AppCompatActivity {
    private ListView frequent_sessions;
    private ListView sum_time;
    private ListView time_from_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        frequent_sessions = (ListView) findViewById(R.id.frequent_sessions);
        sum_time = (ListView) findViewById(R.id.sum_time);
        time_from_category = (ListView) findViewById(R.id.time_from_category);
    }
}
