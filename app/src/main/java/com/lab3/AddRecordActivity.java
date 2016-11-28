package com.lab3;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.lab3.db.DbUtils;
import com.lab3.domain.Category;
import com.lab3.domain.TimeRecord;

import java.util.ArrayList;

public class AddRecordActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DbUtils utils;
    private SQLiteDatabase database;
    private Spinner spinner;

    private TimePicker startTime;
    private TimePicker endTime;
    private Button addDataBtn;
    private EditText descriptionEdit;
    private EditText segmentEdit;
    private ArrayList<Category> allCategories;
    private ArrayAdapter<Category> adapter;
    private Category selectedCategory;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;

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

        addDataBtn = (Button) findViewById(R.id.addRecord);
        startTime = (TimePicker) findViewById(R.id.startPicker);
        startTime.setIs24HourView(true); // формат 24 часа

        endTime = (TimePicker) findViewById(R.id.endPicker);
        endTime.setIs24HourView(true); // формат 24 часа
        descriptionEdit = (EditText) findViewById(R.id.addDescription_text);
        segmentEdit = (EditText) findViewById(R.id.segment_EditText);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
       selectedCategory = (Category) adapterView.getItemAtPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //TODO Проставить здесь и при добавлении в бд фотки
    public void onAddData(View view){
        startHour = startTime.getCurrentHour();
        startMinute = startTime.getCurrentMinute();
        String startTimeStr = startHour+":"+startMinute;
        endHour = endTime.getCurrentHour();
        endMinute = endTime.getCurrentMinute();
        String endTimeStr = endHour+":"+endMinute;
        String  description = descriptionEdit.getText().toString();
        String segment = segmentEdit.getText().toString();
        TimeRecord newDaata = new TimeRecord(startTimeStr,endTimeStr,description,selectedCategory,null,segment);
        utils.insertTimeRecord(database,newDaata);
    }
}
