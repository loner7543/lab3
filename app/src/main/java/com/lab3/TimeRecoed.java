package com.lab3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TimeRecoed extends AppCompatActivity {
    private Button addBtn;
    private Button deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_recoed);
        addBtn = (Button) findViewById(R.id.addTime);
        deleteBtn = (Button) findViewById(R.id.deleteTime);
    }

    public void addRecord(View view){

    }

    public void EditRecord(View view){}

    public void deleteRecord(View view){

    }
}
