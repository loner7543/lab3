package com.lab3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.lab3.DbBitmapUtility;
import com.lab3.R;
import com.lab3.domain.Category;
import com.lab3.domain.Photo;
import com.lab3.domain.TimeRecord;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by alma0516 on 11/25/2016.
 */

public class DbUtils extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    private static final String LOG_TAG = "DbHelper";

    //названия таблиц
    public static final String DATABASE_NAME = "TimeTracker";
    public static final String CATEGORY_TABLE = "Category";
    public static final String PHOTO_TABLE = "Photo";
    public  static final String TIME_RECORD_TABLE = "TimeRecord";
    public static final String TIME_PHOTO_TABLE= "time_photo";

    //Константы для полей таблицы "Категория"
    public static final String CATEGORY_ID = "ID";
    public static final String CATEGORY_NAME = "CATEGORY_NAME";

    //таблица развязка между временем и категорией
    public static final String TIME_ID_REF = "time_id_ref";
    public static final String PHOTO_ID_REF="photo_id_ref";

    //таблица с фотками
    public static final String PHOTO_ID = "ID";
    public static final String IMAGE = "IMAGE";

    //таблица с отметками времени
    public static final String TIME_ID = "ID";//первичный ключ глав таблицы время
    public static final String CATEGORY_ID_REF = "CATEGORY_ID";// id категории
    public static final String DDESCRIPTION = "DDESCRIPTION";
    public static final String START_TIME = "START_TIME";
    public static final String END_TIME = "END_TIME";
    public static final String TIME_SEGMENT = "TIME_SEGMENT";

    //Запросы на создание таблиц, взятые из sqliteBrowser(я не хочу чтоб у меня что то слетело -брал оттуда)
    public static final String CREATE_CATEGORY_QUERY = "CREATE TABLE `Category` (\n" +
            "\t`ID`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`CATEGORY_NAME`\tINTEGER\n" +
            ");";
    public static final String CREATE_PHOTO_QUERY = "CREATE TABLE `Photo` (\n" +
            "\t`ID`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`IMAGE`\tBLOB\n" +
            ");";
    public static final String CREATE_TIMERECORD_QUERY = "CREATE TABLE `TimeRecord` (\n" +
            "\t`ID`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`CATEGORY_ID`\tINTEGER,\n" +
            "\t`DDESCRIPTION`\tTEXT,\n" +
            "\t`START_TIME`\tTEXT,\n" +
            "\t`END_TIME`\tTEXT,\n" +
            "\t`TIME_SEGMENT`\tINTEGER\n" +
            ");";
    public static final String CREATE_REFERENCE_TABLE="CREATE TABLE `time_photo` (\n" +
            "\t`time_id_ref`\tINTEGER,\n" +
            "\t`photo_id_ref`\tINTEGER\n" +
            ");";

    public String sqlQuery = "";//cтрока для запросов

    public DbUtils(Context context, String name,  int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//вызывается при созщдании бд
        Log.d(LOG_TAG,"Databse create called");
        db.execSQL(CREATE_CATEGORY_QUERY);
        db.execSQL(CREATE_PHOTO_QUERY);
        db.execSQL(CREATE_TIMERECORD_QUERY);
        db.execSQL(CREATE_REFERENCE_TABLE);
        Log.d(LOG_TAG,"Table created sucs");
        insertCatigories(db,new Category("Coн"));
        insertCatigories(db,new Category("Уборка"));
        insertCatigories(db,new Category("Работа"));
        insertCatigories(db,new Category("Гялял с котом"));

        //инициализируем развязку
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME_ID_REF,1);
        contentValues.put(PHOTO_ID_REF,1);
        db.insert(CREATE_REFERENCE_TABLE,null,contentValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+CATEGORY_TABLE);
        db.execSQL("drop table if exists "+PHOTO_TABLE);
        db.execSQL("drop table if exists "+TIME_RECORD_TABLE);
        db.execSQL("drop table if exists "+TIME_PHOTO_TABLE);
        Log.d(LOG_TAG,"Drop tables sucs");
        onCreate(db);

    }

    public Cursor getAllRecords(SQLiteDatabase database, String tableName) {
        Cursor cursor = database.query(tableName, null,null, null, null, null, null);
        return  cursor;
    }

    public long insertData(SQLiteDatabase database, ContentValues values, String table){
        long res =  database.insert(table,null,values);
        return res;
    }

    //content values вставляет щас лишь одну запись
    public void insertCatigories(SQLiteDatabase database,Category data){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_NAME,data.getCategoryName());
        database.beginTransaction();
        long res =  database.insert(DbUtils.CATEGORY_TABLE, null, contentValues);
        Log.d(LOG_TAG,"InsertResult "+res);
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public int update(SQLiteDatabase database,String tableName,ContentValues contentValues,String fieldCause, String[] whereArgs){
        int updCount = database.update(tableName, contentValues, fieldCause+"= ?", whereArgs);
        return updCount;
    }

    public int getIdByName(String name,SQLiteDatabase database){
        int res = 0;
        for (Category c:getall(database,CATEGORY_TABLE)){
            if(c.getCategoryName().equals(name)){
                res = c.getId();
            }
        }
        return res;
    }

    public List<Category> getall(SQLiteDatabase database,String table){
        List<Category> result = new LinkedList<>();
        Cursor cursor = database.query(table, null,null, null, null, null, null);
        int i = 0;
        String name;
        int id;
        Category category;
        if (cursor != null && cursor.moveToFirst()) {
            int idIdx = cursor.getColumnIndex(DbUtils.CATEGORY_ID);
            int categoryIdx = cursor.getColumnIndex(DbUtils.CATEGORY_NAME);
            do {
                id = cursor.getInt(idIdx);
                name = cursor.getString(categoryIdx);
                category = new Category(id,name);
                result.add(category);
                i++;
            }
            while (cursor.moveToNext());
        }
        return result;
    }

    public void initPhotoTable(SQLiteDatabase database,Context context){
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.intellj);
        byte[] image = DbBitmapUtility.getBytes(icon);
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGE,image);
        insertData(database,contentValues,PHOTO_TABLE);
    }

    public List<Photo> getAllPhoto(SQLiteDatabase database){
        List<Photo> res = new LinkedList<>();
        Cursor cursor = getAllRecords(database,PHOTO_TABLE);
        int IdIdx;
        int i = 0;
        int imageIdx;
        Photo photo;
        int id;
        Bitmap bitmap;
        if (cursor != null && cursor.moveToFirst()) {
            IdIdx = cursor.getColumnIndex(PHOTO_ID);
            imageIdx = cursor.getColumnIndex(IMAGE);
            do {
                id = cursor.getInt(IdIdx);
                bitmap =DbBitmapUtility.getImage(cursor.getBlob(imageIdx));
                photo = new Photo(bitmap,id);
                res.add(photo);
                i++;
            }
            while (cursor.moveToNext());
        }
        return  res;
    }

    public int deleteEntity(SQLiteDatabase database,String table,String causeColumn,String[] causeArgs){
        int res =  database.delete (table, causeColumn+"=?", causeArgs);
        return res;
    }

    public void initTimeTable(TimeRecord timeRecord,SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_ID_REF,1);
        contentValues.put(START_TIME,"11.12.12");
        contentValues.put(END_TIME,"11.12.13");
        contentValues.put(TIME_SEGMENT,10);
        contentValues.put(DDESCRIPTION,"asdf");
        insertData(database,contentValues,TIME_RECORD_TABLE);
    }

    //TODO фотки пока пустые надо делать развязку
    public List<TimeRecord> getAllTimes(SQLiteDatabase database){
        List<TimeRecord> res = new LinkedList<>();
        TimeRecord timeRecord;
        List<Photo> photoRecord = new LinkedList<>();
        int i = 0;
        int IdIdx,descriptionIndex,startDateIdx,endDateIdx,segmentIdx,categoryIdx;
        int iDval,categoryId;
        String startDate,endDate,segment,descValue;
        Cursor cursor = getAllRecords(database,TIME_RECORD_TABLE);
        if (cursor != null && cursor.moveToFirst()) {
            IdIdx = cursor.getColumnIndex(CATEGORY_ID);
            descriptionIndex = cursor.getColumnIndex(DDESCRIPTION);
            startDateIdx = cursor.getColumnIndex(START_TIME);
            endDateIdx = cursor.getColumnIndex(END_TIME);
            segmentIdx = cursor.getColumnIndex(TIME_SEGMENT);
            categoryIdx = cursor.getColumnIndex(CATEGORY_ID_REF);
            do {
                iDval = cursor.getInt(IdIdx);
                descValue = cursor.getString(descriptionIndex);
                startDate = cursor.getString(startDateIdx);
                endDate = cursor.getString(endDateIdx);
                segment = cursor.getString(segmentIdx);
                categoryId = cursor.getInt(categoryIdx);
                Category c = getCategoryById(database,categoryId);
                timeRecord = new TimeRecord(iDval,startDate,endDate,descValue,c,segment,photoRecord);
                res.add(timeRecord);
                i++;
            }
            while (cursor.moveToNext());
        }
        return res;
    }

    //TODO не тестировался
    public Category getCategoryById(SQLiteDatabase database,int id){
        sqlQuery = "select * from"+CATEGORY_TABLE+"where"+CATEGORY_ID+"=?";
        Category category = null;
        int i = 0;
        String name;
        int idCat;
        Cursor cursor = database.query(CATEGORY_TABLE,null,CATEGORY_ID+"=?",new String[]{String.valueOf(id)},null,null,null);
        if (cursor != null && cursor.moveToFirst()) {
            int idIdx = cursor.getColumnIndex(DbUtils.CATEGORY_ID);
            int categoryIdx = cursor.getColumnIndex(DbUtils.CATEGORY_NAME);
            do {
                idCat = cursor.getInt(idIdx);
                name = cursor.getString(categoryIdx);
                category = new Category(idCat,name);
                i++;
            }
            while (cursor.moveToNext());
        }
        return category;
    }

    public Bitmap getPhotoById(SQLiteDatabase database,int id){
        Cursor cursor = database.query(PHOTO_TABLE,null,PHOTO_ID+"=?",new String[]{String.valueOf(id)},null,null,null);
        int IdIdx;
        int i = 0;
        int imageIdx;
        Photo photo;
        int idVal;
        Bitmap bitmap = null;
        if (cursor != null && cursor.moveToFirst()) {
            IdIdx = cursor.getColumnIndex(PHOTO_ID);
            imageIdx = cursor.getColumnIndex(IMAGE);
                idVal = cursor.getInt(IdIdx);
                bitmap =DbBitmapUtility.getImage(cursor.getBlob(imageIdx));
                photo = new Photo(bitmap,id);
        }
        return bitmap;
    }
}
