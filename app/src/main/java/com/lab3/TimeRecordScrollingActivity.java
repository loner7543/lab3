package com.lab3;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.lab3.adapters.TimeRecordAdapter;
import com.lab3.db.DbBitmapUtility;
import com.lab3.db.DbUtils;
import com.lab3.domain.Category;
import com.lab3.domain.Photo;
import com.lab3.domain.SerialPhoto;
import com.lab3.domain.TimeRecord;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TimeRecordScrollingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    public static final int REQUEST_CODE_REFRESH = 1;
    public static final int EDIT_TIME_RECORD_CODE=2;
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
        setContentView(R.layout.activity_time_record_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Отметки времени");

        context = getApplicationContext();
        addBtn = (Button) findViewById(R.id.addTime);
        editBtn = (Button) findViewById(R.id.editRecord);
        deleteBtn = (Button) findViewById(R.id.deleteTime);
        recordListView = (ListView) findViewById(R.id.timeRecord_list);
        utils = new DbUtils(this, DbUtils.DATABASE_NAME, DbUtils.DATABASE_VERSION);
        database = utils.getWritableDatabase();//дает бд на запись
       // utils.initTimeTable(null,database);//забиваю бд данными
        allRecords = utils.getAllTimes(database);
        adapter = new TimeRecordAdapter(context,R.layout.record_item,allRecords);
        recordListView.setOnItemClickListener(this);
        recordListView.setAdapter(adapter);

        //загружаю все категории
        allCategory = utils.parseCursor(utils.getAllRecords(database,DbUtils.CATEGORY_TABLE));
        categoryAdapter = new ArrayAdapter<Category>(this,
                android.R.layout.simple_list_item_1, allCategory);

        allPhoto = utils.getAllPhoto(database);
        photoAdapter = new CustomPhotoAdapter(context,R.layout.galary_item,allPhoto);
    }


    public void addRecord(View view){
        Intent intent= new Intent(context,RecordScrollingActivity.class);
        intent.putExtra("Action",REQUEST_CODE_REFRESH);
        intent.putExtra("title","Добавить отметку времени");
        intent.putExtra("btn_text","Добавить запись");
        startActivityForResult(intent,REQUEST_CODE_REFRESH);
    }

    public void EditRecord(View view){
        try
        {
            Intent editedIntent = new Intent(this,RecordScrollingActivity.class);
            editedIntent.putExtra("title","Изменить отметку времени");
            editedIntent.putExtra("btn_text","Изменить запись");
            editedIntent.putExtra("Action",EDIT_TIME_RECORD_CODE);
            int i = 0;
            List<Photo> photos = selectedRecord.getPhoto();
            for (Photo photo:photos){
                SerialPhoto serialPhoto = new SerialPhoto(photo.getId(), DbBitmapUtility.getBytes(photo.getImage()));
                editedIntent.putExtra("photo"+i,serialPhoto);
            }
            i=0;
            selectedRecord.setPhoto(null);
            editedIntent.putExtra("data",selectedRecord);
            editedIntent.putExtra("count",photos.size());
            startActivityForResult(editedIntent,EDIT_TIME_RECORD_CODE);
        }
        catch (NullPointerException e){
            Toast toast = Toast.makeText(this,"У записи отсутствуют фотографии",Toast.LENGTH_LONG);
            toast.show();
        }

    }

    public void deleteRecord(View view){
        utils.deleteTimeRecord(database,selectedRecord.getId());
        allRecords.remove(selectedRecord);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedRecord = (TimeRecord) adapterView.getItemAtPosition(i);
    }

    // TODO Дописать изменение запси времениб перерабоать бд
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG_KEY, "requestCode = " + requestCode + ", resultCode = " + resultCode);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_REFRESH:{//запись добавили в бд
                    int i = 0;
                    int photoCount = data.getIntExtra("count",0);
                    TimeRecord newRecord = (TimeRecord) data.getSerializableExtra("data");
                    List<Photo> newPhotos = new LinkedList<>();
                    for (int j = 0;j<photoCount;j++)
                    {
                        SerialPhoto f = (SerialPhoto) data.getSerializableExtra("photo"+i);
                        newPhotos.add(new Photo(DbBitmapUtility.getImage(f.getData()),f.getId()));
                        i++;
                    }
                    newRecord.setPhoto(newPhotos);
                    allRecords.add(newRecord);
                    adapter.notifyDataSetChanged();
                    break;
                }
                case EDIT_TIME_RECORD_CODE:{
                    int i = 0;
                    allRecords.remove(selectedRecord);
                    int photoCount = data.getIntExtra("count",0);
                    TimeRecord editedRecord = (TimeRecord) data.getSerializableExtra("data");
                    List<Photo> newPhotos = new LinkedList<>();
                    for (int j = 0;j<photoCount;j++)
                    {
                        SerialPhoto f = (SerialPhoto) data.getSerializableExtra("photo"+i);
                        newPhotos.add(new Photo(DbBitmapUtility.getImage(f.getData()),f.getId()));
                        i++;
                    }
                    editedRecord.setPhoto(newPhotos);
                    allRecords.remove(selectedRecord);
                    allRecords.add(editedRecord);
                    break;
                }
                default:{
                    Toast.makeText(this,"Ошибка при вставке или изменении отметки времени",Toast.LENGTH_LONG);
                }
            }

        }
    }
}
