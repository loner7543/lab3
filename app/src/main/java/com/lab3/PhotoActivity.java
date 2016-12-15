package com.lab3;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.lab3.adapters.PhotoAdapter;
import com.lab3.db.DbBitmapUtility;
import com.lab3.db.DbUtils;
import com.lab3.domain.Photo;

import java.util.List;

public class PhotoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private final int CAMERA_RESULT_ADD = 0;
    private final int CAMERA_EDIT_PHOTO = 1;
    private Context context;
    private SQLiteDatabase database;
    private DbUtils utils;
    private List<Photo> allPhoto;
    private ListView phtoListView;
    private Photo selectedPhoto;
    private PhotoAdapter adapter;
    //context.getResources(), R.drawable.intellj

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
       // utils.initPhotoTable(database,R.drawable.intellj,context); //работает - фотка в бду же есть
       //utils.initPhotoTable(database,R.drawable.git,context); //работает - фотка в бду же есть

        allPhoto = utils.getAllPhoto(database);
        phtoListView.setOnItemClickListener(this);
        adapter = new PhotoAdapter(context,R.layout.photo_item,allPhoto);
        phtoListView.setAdapter(adapter);
    }

    public void onAddPhoto(View view){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_RESULT_ADD);
    }

    public void onEditPhoto(View view){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_EDIT_PHOTO);
    }

    public void onDeletePhoto(View view){
        utils.deleteCascadePhoto(database,selectedPhoto);
        allPhoto.remove(selectedPhoto);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedPhoto = adapter.getPhoto(i);
    }

    //TODO пока не установил ID - ксли надо будет менять фото через камеру - сделаю
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       switch (requestCode){
           case CAMERA_RESULT_ADD:{
               try{
                   Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
                   utils.insertCameraImage(database,thumbnailBitmap);
                   allPhoto.add(new Photo(thumbnailBitmap));
                   adapter.notifyDataSetChanged();
                   break;
               }
               catch (NullPointerException e){
                   Toast toast = Toast.makeText(this,"Добавление было отменено пользователем",Toast.LENGTH_LONG);
                   toast.show();
                   break;
               }
           }
           case CAMERA_EDIT_PHOTO:{
               try{
                   Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
                   Photo photo = new Photo(thumbnailBitmap,selectedPhoto.getId());
                   ContentValues contentValues = new ContentValues();
                   contentValues.put(utils.PHOTO_ID,photo.getId());
                   contentValues.put(utils.IMAGE,DbBitmapUtility.getBytes(thumbnailBitmap));
                   utils.update(database,utils.PHOTO_TABLE,contentValues,utils.PHOTO_ID,new String[]{String.valueOf(photo.getId())});
                   allPhoto.remove(selectedPhoto);
                   allPhoto.add(photo);
                   adapter.notifyDataSetChanged();
                   break;
               }
               catch (NullPointerException e){
                   Toast toast = Toast.makeText(this,"Редактирование было отменено пользователем",Toast.LENGTH_LONG);
                   toast.show();
                   break;
               }

           }

        }
    }
}
