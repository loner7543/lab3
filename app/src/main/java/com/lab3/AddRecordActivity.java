package com.lab3;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.lab3.db.DbUtils;
import com.lab3.domain.Category;

import java.util.ArrayList;

public class AddRecordActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DbUtils utils;
    private SQLiteDatabase database;
    private Spinner spinner;
    private ArrayList<Category> allCategories;
    private ArrayAdapter<Category> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        utils = new DbUtils(this, DbUtils.DATABASE_NAME, DbUtils.DATABASE_VERSION);
        database = utils.getWritableDatabase();//дает бд на запись
        allCategories = utils.parseCursor(utils.getAllRecords(database,DbUtils.CATEGORY_TABLE));
        spinner = (Spinner) findViewById(R.id.spinnerCategory);
        adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, allCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(1);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String s = "wcfdefefresf";
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
