package com.lab3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    //Константы для полей таблицы "Категория"
    public static final String CATEGORY_ID = "ID";
    public static final String CATEGORY_NAME = "CATEGORY_NAME";

    //таблица с фотками
    public static final String PHOTO_ID = "ID";
    public static final String IMAGE = "IMAGE";

    //таблица с отметками времени
    public static final String TIME_ID = "ID";//первичный ключ глав таблицы время
    public static final String PHOTO_ID_REF = "PHOTO_ID";//id  фотографии
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
            "\t`PHOTO_ID`\tINTEGER,\n" +
            "\t`CATEGORY_ID`\tINTEGER,\n" +
            "\t`DDESCRIPTION`\tTEXT,\n" +
            "\t`START_TIME`\tTEXT,\n" +
            "\t`END_TIME`\tTEXT,\n" +
            "\t`TIME_SEGMENT`\tINTEGER\n" +
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
        Log.d(LOG_TAG,"Table created sucs");
        insertCatigories(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+CATEGORY_TABLE);
        db.execSQL("drop table if exists "+PHOTO_TABLE);
        db.execSQL("drop table if exists "+TIME_RECORD_TABLE);
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
    public void insertCatigories(SQLiteDatabase database){
        ContentValues data = new ContentValues();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_NAME,"Работа");
        contentValues.put(CATEGORY_NAME,"Обед");
        contentValues.put(CATEGORY_NAME,"Отдых");
        contentValues.put(CATEGORY_NAME,"Уборка");
        contentValues.put(CATEGORY_NAME,"Сон");
        database.beginTransaction();
        long res =  database.insert(DbUtils.CATEGORY_TABLE, null, contentValues);
        Log.d(LOG_TAG,"InsertResult "+res);
        database.setTransactionSuccessful();
        database.endTransaction();
    }
}
