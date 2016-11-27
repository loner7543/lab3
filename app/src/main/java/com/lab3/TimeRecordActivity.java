package com.lab3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.lab3.db.DbUtils;
import com.lab3.domain.TimeRecord;

import java.util.List;

public class TimeRecordActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Button addBtn;
    private Button editBtn;
    private Button deleteBtn;
    private ListView recordListView;
    private Context context;
    private SQLiteDatabase database;
    private DbUtils utils;
    private List<TimeRecord> allRecords;
    private TimeRecordAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_recoed);
        context = getApplicationContext();
        addBtn = (Button) findViewById(R.id.addTime);
        editBtn = (Button) findViewById(R.id.editRecord);
        deleteBtn = (Button) findViewById(R.id.deleteTime);
        recordListView = (ListView) findViewById(R.id.timeRecord_list);
        utils = new DbUtils(this, DbUtils.DATABASE_NAME, DbUtils.DATABASE_VERSION);
        database = utils.getWritableDatabase();//дает бд на запись
     //  utils.initTimeTable(null,database);//забиваю бд данными
        allRecords = utils.getAllTimes(database);
        adapter = new TimeRecordAdapter(context,R.layout.record_item,allRecords);
        recordListView.setOnItemClickListener(this);
        recordListView.setAdapter(adapter);
    }

    public void addRecord(View view){

    }

    public void EditRecord(View view){}

    public void deleteRecord(View view){

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String s = "defefefe";
    }
}
