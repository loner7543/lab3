package com.lab3;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lab3.db.DbBitmapUtility;
import com.lab3.db.DbUtils;
import com.lab3.domain.Category;
import com.lab3.domain.Photo;
import com.lab3.domain.SerialPhoto;
import com.lab3.domain.TimeRecord;

import org.apache.commons.lang.math.NumberUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RecordScrollingActivity extends AppCompatActivity implements Comparable,AdapterView.OnItemClickListener {

    private DbUtils utils;
    private SQLiteDatabase database;
    private Spinner spinner;//для категорий
    private GridView gridView;
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
    private DatePicker fromDatePicker;
    private DatePicker toDatePicker;


    private int fromYear;
    private int toYear;
    private int toMonth;
    private int fromMonth;
    private  int toDay;
    private int fromDay;

    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;

    private Intent intent;
    private Intent dataIntent;
    private TimeRecord timeRecord;

    //данные для редактирования
    private TimeRecord editedData;
    private SerialPhoto serialPhoto;
    private Photo photo;
    private Bitmap bitmap;
    private Calendar fromCalendar;
    private Calendar toCalendar;
    private  Toast toast;
    private String [] userData;//то что введет пользователь = отправим на валиацию
    private TextView validateDescriptionText;
    private TextView ValidationSegmentText;
    private TextView ValidationDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataIntent = getIntent();
        setTitle(dataIntent.getStringExtra("title"));
        editedData = (TimeRecord) dataIntent.getSerializableExtra("data");
        serialPhoto = (SerialPhoto) dataIntent.getSerializableExtra("photo0");
        timeRecord = (TimeRecord) dataIntent.getSerializableExtra("data");
        utils = new DbUtils(this, DbUtils.DATABASE_NAME, DbUtils.DATABASE_VERSION);
        context = getApplicationContext();
        database = utils.getWritableDatabase();//дает бд на запись
        allCategories = utils.parseCursor(utils.getAllRecords(database,DbUtils.CATEGORY_TABLE));
        fromDatePicker = (DatePicker) findViewById(R.id.fromDp);
        toDatePicker = (DatePicker) findViewById(R.id.toDp);
        validateDescriptionText = (TextView) findViewById(R.id.validationDescription);
        ValidationSegmentText = (TextView) findViewById(R.id.validationSegment);
        ValidationDateText = (TextView) findViewById(R.id.dateValidation);

        userData = new String[2];
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

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setOnItemClickListener(this);
        customPhotoAdapter = new CustomPhotoAdapter(context,R.layout.galary_item,allPhoto);
        gridView.setAdapter(customPhotoAdapter);

        addDataBtn = (Button) findViewById(R.id.addRecord);
        addDataBtn.setText(dataIntent.getStringExtra("btn_text"));
        startTime = (TimePicker) findViewById(R.id.startPicker);
        startTime.setIs24HourView(true); // формат 24 часа

        endTime = (TimePicker) findViewById(R.id.endPicker);
        endTime.setIs24HourView(true); // формат 24 часа
        descriptionEdit = (EditText) findViewById(R.id.addDescription_text);
        segmentEdit = (EditText) findViewById(R.id.segment_EditText);

        selectedListPhotos = new LinkedList<>();

        //инициализируем даными при редактировании
        if (dataIntent.getIntExtra("Action",5)==TimeRecordActivity.EDIT_TIME_RECORD_CODE)
        {
            descriptionEdit.setText(timeRecord.getDescription());
            segmentEdit.setText(timeRecord.getOtr());
        }
    }

    public void onAddData(View view){
        int action = dataIntent.getIntExtra("Action",5);
        ValidationSegmentText.setText("");
        validateDescriptionText.setText("");
        ValidationDateText.setText("");
        fromCalendar = Calendar.getInstance();
        toCalendar = Calendar.getInstance();

        fromYear = fromDatePicker.getYear();
        fromMonth = fromDatePicker.getMonth();
        fromDay = fromDatePicker.getDayOfMonth();
        fromCalendar.set(fromYear,fromMonth,fromDay);
        Date fromDate = fromCalendar.getTime();

        toYear = toDatePicker.getYear();
        toMonth = toDatePicker.getMonth();
        toDay = toDatePicker.getDayOfMonth();
        toCalendar.set(toYear,toMonth,toDay);
        Date toDate = toCalendar.getTime();

        startHour = startTime.getCurrentHour();
        startMinute = startTime.getCurrentMinute();
        fromDate.setHours(startHour);
        fromDate.setMinutes(startMinute);

        endHour = endTime.getCurrentHour();
        endMinute = endTime.getCurrentMinute();
        toDate.setHours(endHour);
        toDate.setMinutes(endMinute);

        String  description = descriptionEdit.getText().toString();
        String segment = segmentEdit.getText().toString();
        userData[0] = segment;
        userData[1] = description;
        if (validate()){
            TimeRecord newDaata = new TimeRecord(fromDate.getTime(),toDate.getTime(),description,selectedCategory,selectedListPhotos,segment);
            switch (action){
                case TimeRecordScrollingActivity.REQUEST_CODE_REFRESH:{
                    utils.insertTimeRecord(database,newDaata);
                    break;
                }
                case TimeRecordScrollingActivity.EDIT_TIME_RECORD_CODE:{
                    int oldId = dataIntent.getIntExtra("OldId",0);
                    int photoCount = dataIntent.getIntExtra("count",100);
                    if (photoCount==0){
                        utils.updateTimeRecord(oldId,newDaata,database,true);
                    }
                    else {
                        utils.updateTimeRecord(oldId,newDaata,database,false);
                    }

                    break;
                }
            }
            intent = new Intent();
            newDaata.setPhoto(null);
            intent.putExtra("data",newDaata);
            int i = 0;
            for (Photo photo:selectedListPhotos){
                SerialPhoto serialPhoto = new SerialPhoto(photo.getId(),DbBitmapUtility.getBytes(photo.getImage()));
                intent.putExtra("photo"+i,serialPhoto);
                i++;
            }
            i=0;
            intent.putExtra("count",selectedListPhotos.size());
            setResult(RESULT_OK, intent);
            finish();
        }
        else {
            Toast.makeText(this,"Данные не прошли валидацию",Toast.LENGTH_LONG);
        }
    }

    public boolean validate(){
        boolean res = true;
        boolean f = NumberUtils.isDigits(userData[0]);
        if (!f){
            ValidationSegmentText.setText("Значение отрезка не явялется числом");
            res = false;
        }
        if (userData[0].isEmpty()){
            ValidationSegmentText.setText("Отрезок времени не введен");
            res = false;
        }
        if (userData[1].isEmpty()){
            validateDescriptionText.setText("Описание не введено");
            res = false;
        }
        if (userData[1].length()>20){
            validateDescriptionText.setText("Длина описания превышает 20 символов");
            res = false;
        }
        if ((fromYear>toYear)||(startHour>endHour)){
            ValidationDateText.setText("Дата или время  введены неверно");
            res = false;
        }
        return res;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    /*
    * Обрабтка клика по галерее
    * */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Photo item = (Photo) adapterView.getItemAtPosition(i);
        selectedListPhotos.add(item);
    }
}
