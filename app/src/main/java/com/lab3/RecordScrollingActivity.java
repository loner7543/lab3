package com.lab3;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.lab3.db.DbUtils;
import com.lab3.domain.Category;
import com.lab3.domain.Photo;
import com.lab3.domain.TimeRecord;

import org.apache.commons.lang.math.NumberUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RecordScrollingActivity extends AppCompatActivity implements Comparable {

    private DbUtils utils;
    private SQLiteDatabase database;
    private Spinner spinner;//для категорий
    private Spinner photoSpinner;
    private CustomPhotoAdapter customPhotoAdapter;
    private Context context;
    private List<Photo> allPhoto;

    private TimePicker startTime;
    private TimePicker endTime;
    private Button addDataBtn;
    private EditText descriptionEdit;
    private EditText segmentEdit;
    private ArrayList<Category> allCategories;
    private ArrayAdapter<Category> adapter;
    private Category selectedCategory;
    private Photo selectedPhoto;
    private List<Photo> selectedListPhotos;//фотки которые пользователь наберет из спинера
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        utils = new DbUtils(this, DbUtils.DATABASE_NAME, DbUtils.DATABASE_VERSION);
        context = getApplicationContext();
        database = utils.getWritableDatabase();//дает бд на запись
        allCategories = utils.parseCursor(utils.getAllRecords(database,DbUtils.CATEGORY_TABLE));
        spinner = (Spinner) findViewById(R.id.spinnerCategory);
        adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, allCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = (Category) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        allPhoto = utils.getAllPhoto(database);

        photoSpinner = (Spinner) findViewById(R.id.photoSpinner);
        customPhotoAdapter = new CustomPhotoAdapter(context,R.layout.photo_spinner_item,allPhoto);
        photoSpinner.setAdapter(customPhotoAdapter);
        photoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPhoto = (Photo) adapterView.getItemAtPosition(i);
                selectedListPhotos.add(selectedPhoto);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        addDataBtn = (Button) findViewById(R.id.addRecord);
        startTime = (TimePicker) findViewById(R.id.startPicker);
        startTime.setIs24HourView(true); // формат 24 часа

        endTime = (TimePicker) findViewById(R.id.endPicker);
        endTime.setIs24HourView(true); // формат 24 часа
        descriptionEdit = (EditText) findViewById(R.id.addDescription_text);
        segmentEdit = (EditText) findViewById(R.id.segment_EditText);

        selectedListPhotos = new LinkedList<>();

    }

    public void onAddData(View view){
        startHour = startTime.getCurrentHour();
        startMinute = startTime.getCurrentMinute();
        String startTimeStr = startHour+":"+startMinute;
        endHour = endTime.getCurrentHour();
        endMinute = endTime.getCurrentMinute();
        String endTimeStr = endHour+":"+endMinute;
        String  description = descriptionEdit.getText().toString();
        String segment = segmentEdit.getText().toString();
        validate(segment);
        TimeRecord newDaata = new TimeRecord(startTimeStr,endTimeStr,description,selectedCategory,selectedListPhotos,segment);
        utils.insertTimeRecord(database,newDaata);
        intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public boolean validate(String segmnt){
        boolean f = NumberUtils.isDigits(segmnt);
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
