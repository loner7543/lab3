package com.lab3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.lab3.db.DbUtils;
import com.lab3.domain.Category;
import com.lab3.domain.Photo;
import com.lab3.domain.TimeRecord;

import java.util.ArrayList;
import java.util.List;

public class TimeRecordActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final int REQUEST_CODE_REFRESH = 1;
    public static final String LOG_KEY = "TimeRecordActivity";

    private Button addBtn;
    private Button editBtn;
    private Button deleteBtn;
    private ListView recordListView;
    private Context context;
    private SQLiteDatabase database;
    private DbUtils utils;
    private List<TimeRecord> allRecords;
    private TimeRecordAdapter adapter;
    private TimeRecord selectedRecord;
    private ArrayList<Category> allCategory;
    private ArrayAdapter<Category> categoryAdapter;

    private List<Photo> allPhoto;//все фотки чтобы
    private CustomPhotoAdapter photoAdapter;//все доступные фото


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
       //utils.initTimeTable(null,database);//забиваю бд данными
        allRecords = utils.getAllTimes(database);
        adapter = new TimeRecordAdapter(context,R.layout.record_item,allRecords);
        recordListView.setOnItemClickListener(this);
        recordListView.setAdapter(adapter);

        //загружаю все категории
        allCategory = utils.parseCursor(utils.getAllRecords(database,DbUtils.CATEGORY_TABLE));
        categoryAdapter = new ArrayAdapter<Category>(this,
                android.R.layout.simple_list_item_1, allCategory);

        allPhoto = utils.getAllPhoto(database);
        photoAdapter = new CustomPhotoAdapter(context,R.layout.photo_spinner_item,allPhoto);
    }

    public void addRecord(View view){
        Intent intent= new Intent(context,AddRecordActivity.class);
        startActivityForResult(intent,REQUEST_CODE_REFRESH);
    }

    public void EditRecord(View view){
        onCreateEditDialog();
    }

    public void deleteRecord(View view){
        utils.geleteRasvFromTimerecord(database,selectedRecord.getId());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedRecord = (TimeRecord) adapterView.getItemAtPosition(i);
    }

    //тут обработать результат добавления данных на другой активити
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG_KEY, "requestCode = " + requestCode + ", resultCode = " + resultCode);
        if (resultCode==RESULT_OK){
            adapter.notifyDataSetChanged();//не срабатывает, данные не меняются
        }
    }

    public void onCreateEditDialog(){
        String[] startTimes ;
        String[] endTimes;
        LayoutInflater layoutInflater = LayoutInflater.from(TimeRecordActivity.this);
        Button button;
        final Category[] newCategory = new Category[1];

        View promptView = layoutInflater.inflate(R.layout.activity_add_record, null);
        button = (Button) promptView.findViewById(R.id.addRecord);
        button.setVisibility(View.INVISIBLE);
        final EditText descriptionEdit = (EditText) promptView.findViewById(R.id.addDescription_text);
        descriptionEdit.setText(selectedRecord.getDescription());

        final EditText segmentEdit = (EditText) promptView.findViewById(R.id.segment_EditText);
        segmentEdit.setText(selectedRecord.getOtr());

        final TimePicker fromDate = (TimePicker) promptView.findViewById(R.id.startPicker);
        startTimes = selectedRecord.getStartDate().split(":");
        fromDate.setCurrentHour(Integer.valueOf(startTimes[0]));
        fromDate.setCurrentMinute(Integer.valueOf(startTimes[1]));

        final TimePicker toDate = (TimePicker) promptView.findViewById(R.id.endPicker);
        endTimes = selectedRecord.getEndDate().split(":");
        toDate.setCurrentHour(Integer.valueOf(endTimes[0]));
        toDate.setCurrentMinute(Integer.valueOf(endTimes[1]));

        Spinner categorySpinner = (Spinner) promptView.findViewById(R.id.spinnerCategory);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                newCategory[0] = (Category) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner photoSpinner = (Spinner) promptView.findViewById(R.id.photoSpinner);
        photoSpinner.setAdapter(photoAdapter);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TimeRecordActivity.this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(false)
                .setNegativeButton(R.string.editBtn,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String newDescription = descriptionEdit.getText().toString();
                                String newSegment = segmentEdit.getText().toString();
                                String startDate = getDate(fromDate.getCurrentHour(),fromDate.getCurrentMinute());
                                String endDate = getDate(toDate.getCurrentHour(),toDate.getCurrentMinute());

                                    dialog.cancel();

                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public String getDate(int hour,int minute ){
        String s = String.valueOf(hour)+":"+String.valueOf(minute);
        return s;
    }
}
