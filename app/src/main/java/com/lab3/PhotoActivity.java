package com.lab3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lab3.db.DbUtils;
import com.lab3.domain.Photo;

import java.util.List;

public class PhotoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private SQLiteDatabase database;
    private DbUtils utils;
    private List<Photo> allPhoto;
    private ListView phtoListView;
    private Photo selectedPhoto;
    private PhotoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        setTitle("Фотографии");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        phtoListView = (ListView) findViewById(R.id.photoList);
        utils = new DbUtils(this, DbUtils.DATABASE_NAME, DbUtils.DATABASE_VERSION);
        database = utils.getWritableDatabase();//дает бд на запись
        //utils.initPhotoTable(database,context); //работает - фотка в бду же есть
        allPhoto = utils.getAllPhoto(database);
        phtoListView.setOnItemClickListener(this);
        adapter = new PhotoAdapter(context,R.layout.photo_item,allPhoto);
        phtoListView.setAdapter(adapter);
    }

    public void onAddPhoto(View view){

    }

    public void onDeletePhoto(View view){
        utils.deleteEntity(database,DbUtils.PHOTO_TABLE,DbUtils.PHOTO_ID,new String[]{String.valueOf(selectedPhoto.getId())});
        allPhoto.remove(selectedPhoto);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String s = "";
        selectedPhoto = adapter.getPhoto(i);
    }
}
