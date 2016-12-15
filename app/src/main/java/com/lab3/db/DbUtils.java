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
import com.lab3.domain.Category;
import com.lab3.domain.Photo;
import com.lab3.domain.TimeCategory;
import com.lab3.domain.TimeRecord;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
            "\t`START_TIME`\tNUMERIC,\n" +
            "\t`END_TIME`\tNUMERIC,\n" +
            "\t`TIME_SEGMENT`\tINTEGER,\n" +
            " FOREIGN KEY(CATEGORY_ID) REFERENCES Category(id) ON UPDATE CASCADE\n"+
            ");";
    public static final String CREATE_REFERENCE_TABLE="CREATE TABLE `time_photo` (\n" +
            "\t`time_id_ref`\tINTEGER,\n" +
            "\t`photo_id_ref`\tINTEGER, \n" +
            " FOREIGN KEY(time_id_ref) REFERENCES Category(id) ON UPDATE CASCADE on delete cascade,\n"+
            "FOREIGN KEY(photo_id_ref) REFERENCES Photo(id) ON UPDATE CASCADE on delete cascade"+
            ");";


    public String sqlQuery = "";//cтрока для запросов
    private Calendar calendar;

    public DbUtils(Context context, String name,  int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//вызывается при созщдании бд
        Log.d(LOG_TAG,"Databse create called");
        db.execSQL("PRAGMA foreign_keys=ON");
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
        db.insert(TIME_PHOTO_TABLE,null,contentValues);

        ContentValues cv = new ContentValues();
        cv.put(TIME_ID_REF,1);
        cv.put(PHOTO_ID_REF,2);
        db.insert(TIME_PHOTO_TABLE,null,cv);

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
            cursor.close();
        }
        return result;
    }

    public void initPhotoTable(SQLiteDatabase database,int resID,Context ctx){
        Bitmap icon = BitmapFactory.decodeResource(ctx.getResources(), resID);
        byte[] image = DbBitmapUtility.getBytes(icon);
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGE,image);
        insertData(database,contentValues,PHOTO_TABLE);
    }

    public void insertCameraImage(SQLiteDatabase database, Bitmap bitmap){
        byte[] image = DbBitmapUtility.getBytes(bitmap);
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
            cursor.close();
        }
        return  res;
    }

    public int deleteEntity(SQLiteDatabase database,String table,String causeColumn,String[] causeArgs){
        int res =  database.delete (table, causeColumn+"=?", causeArgs);
        return res;
    }

    //
    public void deleteRazvByPhoto(Photo photo){
        sqlQuery = "";
    }

    public void initTimeTable(TimeRecord timeRecord,SQLiteDatabase database){
        calendar = Calendar.getInstance();
        calendar.set(2016,0,5,1,0);
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_ID_REF,1);
        contentValues.put(START_TIME,calendar.getTimeInMillis());
        calendar.set(2016,0,31);
        contentValues.put(END_TIME,calendar.getTimeInMillis());
        contentValues.put(TIME_SEGMENT,10);
        contentValues.put(DDESCRIPTION,"asdf");
        database.insert(TIME_RECORD_TABLE,null,contentValues);

        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(CATEGORY_ID_REF,1);
        calendar.set(2016,1,1,1,0);
        contentValues1.put(START_TIME,calendar.getTimeInMillis());
        calendar.set(2016,4,1,1,0);
        contentValues1.put(END_TIME,calendar.getTimeInMillis());
        contentValues1.put(TIME_SEGMENT,10);
        contentValues1.put(DDESCRIPTION,"efef");
        database.insert(TIME_RECORD_TABLE,null,contentValues1);

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(CATEGORY_ID_REF,1);
        calendar.set(2016,2,21,1,0);
        contentValues2.put(START_TIME,calendar.getTimeInMillis());
        calendar.set(2016,3,21,1,0);
        contentValues2.put(END_TIME,calendar.getTimeInMillis());
        contentValues2.put(TIME_SEGMENT,10);
        contentValues2.put(DDESCRIPTION,"rgrg");
        database.insert(TIME_RECORD_TABLE,null,contentValues2);

        ContentValues contentValues3 = new ContentValues();
        contentValues3.put(CATEGORY_ID_REF,2);
        calendar.set(2016,2,21,1,0);
        contentValues3.put(START_TIME,calendar.getTimeInMillis());
        calendar.set(2016,3,21,1,0);
        contentValues3.put(END_TIME,calendar.getTimeInMillis());
        contentValues3.put(TIME_SEGMENT,50);
        contentValues3.put(DDESCRIPTION,"rgrg");
        database.insert(TIME_RECORD_TABLE,null,contentValues3);

        ContentValues contentValues4 = new ContentValues();
        contentValues4.put(CATEGORY_ID_REF,3);
        calendar.set(2016,2,21,1,0);
        contentValues4.put(START_TIME,calendar.getTimeInMillis());
        calendar.set(2016,3,21,1,0);
        contentValues4.put(END_TIME,calendar.getTimeInMillis());
        contentValues4.put(TIME_SEGMENT,5);
        contentValues4.put(DDESCRIPTION,"rgrg");
        database.insert(TIME_RECORD_TABLE,null,contentValues4);

       /* //для фильтра по месяцу январь
        ContentValues contentValues5  = new ContentValues();
        contentValues5.put(CATEGORY_ID_REF,3);
        calendar.set(2016,0,21,1,0);
        contentValues5.put(START_TIME,calendar.getTimeInMillis());
        calendar.set(2016,3,21,1,0);
        contentValues5.put(END_TIME,calendar.getTimeInMillis());
        contentValues5.put(TIME_SEGMENT,5);
        contentValues5.put(DDESCRIPTION,"rgrg");
        database.insert(TIME_RECORD_TABLE,null,contentValues5);

        ContentValues contentValues6 = new ContentValues();
        contentValues6.put(CATEGORY_ID_REF,3);
        calendar.set(2016,0,19,1,0);
        contentValues6.put(START_TIME,calendar.getTimeInMillis());
        calendar.set(2016,3,21,1,0);
        contentValues6.put(END_TIME,calendar.getTimeInMillis());
        contentValues6.put(TIME_SEGMENT,5);
        contentValues6.put(DDESCRIPTION,"rgrg");
        database.insert(TIME_RECORD_TABLE,null,contentValues6);*/
    }

    public List<TimeRecord> getAllTimes(SQLiteDatabase database){
        List<TimeRecord> res = new LinkedList<>();
        TimeRecord timeRecord;
        List<Photo> photoRecord = new LinkedList<>();
        int i = 0;
        int IdIdx,descriptionIndex,startDateIdx,endDateIdx,segmentIdx,categoryIdx;
        int iDval,categoryId;
        long startDate,endDate;
        String segment,descValue;
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
                startDate = cursor.getLong(startDateIdx);
                endDate = cursor.getLong(endDateIdx);
                segment = cursor.getString(segmentIdx);
                categoryId = cursor.getInt(categoryIdx);
                Category c = getCategoryById(database,categoryId);
                photoRecord = getPhotoListByCatogory(database,c);
                timeRecord = new TimeRecord(iDval,startDate,endDate,descValue,c,segment,photoRecord);
                res.add(timeRecord);
                i++;
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return res;
    }

    public Category getCategoryById(SQLiteDatabase database,int id){
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
            cursor.close();
        }
        return category;
    }

    public List<Photo> getPhotoListByCatogory(SQLiteDatabase database, Category category) {
        List<Photo> photosByCategory = new LinkedList<>();
        Cursor cursor = database.query(TIME_PHOTO_TABLE, null, TIME_ID_REF + "=?", new String[]{String.valueOf(category.getId())}, null, null, null);
        int idCategoryIdx;
        int idPhotoIdx;
        int i = 0;
        Photo photo;
        int idValCategory,idValProtoID;
        if (cursor != null && cursor.moveToFirst()) {
            idCategoryIdx = cursor.getColumnIndex(DbUtils.TIME_ID_REF);
            idPhotoIdx = cursor.getColumnIndex(DbUtils.PHOTO_ID_REF);
            do {
                idValCategory = cursor.getInt(idCategoryIdx);
                idValProtoID = cursor.getInt(idPhotoIdx);
                photo = getPhotoById(database,idValProtoID);
                photosByCategory.add(photo);
                i++;
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return photosByCategory;
    }

    public Photo getPhotoById(SQLiteDatabase database,int id){
        Photo photo = null;
        int i = 0;
        Bitmap bmp;
        int idCat;
        Cursor cursor = database.query(PHOTO_TABLE,null,PHOTO_ID+"=?",new String[]{String.valueOf(id)},null,null,null);
        if (cursor != null && cursor.moveToFirst()) {
            int idIdx = cursor.getColumnIndex(DbUtils.PHOTO_ID);
            int photoIdx = cursor.getColumnIndex(DbUtils.IMAGE);
            do {
                idCat = cursor.getInt(idIdx);
                bmp = DbBitmapUtility.getImage(cursor.getBlob(photoIdx));
                photo = new Photo(bmp,idCat);
                i++;
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return photo;
    }

    public ArrayList<Category> parseCursor(Cursor cursor) {
        ArrayList<Category> lst = new ArrayList<>();
        String name;
        int id;
        Category category;
        int i = 0;
        if (cursor != null && cursor.moveToFirst()) {
            int idIdx = cursor.getColumnIndex(DbUtils.CATEGORY_ID);
            int categoryIdx = cursor.getColumnIndex(DbUtils.CATEGORY_NAME);
            do {
                id = cursor.getInt(idIdx);
                name = cursor.getString(categoryIdx);
                category = new Category(id,name);
                lst.add(category);
                i++;
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        i = 0;
        return lst;
    }

    public void insertTimeRecord(SQLiteDatabase database,TimeRecord record){
        ContentValues contentValues = new ContentValues();
        ContentValues cvR;
        contentValues.put(CATEGORY_ID_REF,record.getCategory().getId());
        contentValues.put(DDESCRIPTION,record.getDescription());
        contentValues.put(START_TIME,record.getStartDate());
        contentValues.put(END_TIME,record.getEndDate());
        contentValues.put(TIME_SEGMENT,record.getOtr());
        //добавляем данные в табл развязку
        List<Photo> photos = record.getPhoto();
        for (Photo p:photos){
            cvR = new ContentValues();
            cvR.put(TIME_ID_REF,record.getCategory().getId());
            cvR.put(PHOTO_ID_REF,p.getId());
            database.insert(TIME_PHOTO_TABLE,null,cvR);
        }
        database.insert(TIME_RECORD_TABLE,null,contentValues);
    }

    public int deleteTimeRecord(SQLiteDatabase database, int recordId){
        int res =  database.delete (TIME_RECORD_TABLE, TIME_ID+"=?", new String[] {String.valueOf(recordId)});
        return res;
    }

    public int updateTimeRecord(TimeRecord oldRecord,TimeRecord newRecord,SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DDESCRIPTION,newRecord.getDescription());
        contentValues.put(START_TIME,newRecord.getStartDate());
        contentValues.put(END_TIME,newRecord.getEndDate());
        contentValues.put(TIME_SEGMENT,newRecord.getOtr());
     //   database.up
        return 0;
    }

    /**
     * Нужна при обновлении чтобы взять старый ID
     * */
    public int getTimeRecordIdByDescription(String desc,SQLiteDatabase database){
        int res = 0;
        for (TimeRecord record:getAllTimes(database)){
            if(record.getDescription().equals(desc)){
                res = record.getId();
            }
        }
        return res;
    }

    //count и один и тот же селект
    public int pieData(SQLiteDatabase database,Category category){
        int res;
        String sql = "select sum(TIME_SEGMENT) from TimeRecord where CATEGORY_ID=?";
        String str = "";
        Cursor cursor = database.rawQuery(sql,new String[]{String.valueOf(category.getId())},null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                for (String cn : cursor.getColumnNames()) {
                    str = cursor.getString(cursor.getColumnIndex(cn));
                }
            }
            while (cursor.moveToNext());
        }
        if (str==null){
            res = 0;
        }
        else {
            res = Integer.valueOf(str);
        }
        cursor.close();
        return res;
}
    public TimeCategory sumTimePerCategory(SQLiteDatabase database,Category category,long startDate,long endDate) {
        sqlQuery = "select sum(TIME_SEGMENT) from TimeRecord where CATEGORY_ID=? and ( "+START_TIME+" BETWEEN "+startDate+" AND "+endDate+" )";
        String str = "";
        int res;
        Cursor cursor = database.rawQuery(sqlQuery, new String[]{String.valueOf(category.getId())}, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                for (String cn : cursor.getColumnNames()) {
                    str = cursor.getString(cursor.getColumnIndex(cn));
                }
            }
            while (cursor.moveToNext());
        }
        if (str==null){
            res = 0;
        }
        else {
            res = Integer.valueOf(str);
        }
        TimeCategory timePerCategory = new TimeCategory(category,res);
        cursor.close();
        return timePerCategory;
    }
    public List<TimeCategory> sumTimeOrder(SQLiteDatabase database,List<Category> allCategories,long startDate,long endDate){
        List<TimeCategory> res = new LinkedList<>();
        int sd;
        sqlQuery = "select sum(TIME_SEGMENT) from TimeRecord where CATEGORY_ID=? and ( "+START_TIME+" BETWEEN "+startDate+" AND "+endDate+" )";
        String str = "";
        for (Category c:allCategories){
            Cursor cursor = database.rawQuery(sqlQuery, new String[]{String.valueOf(c.getId())}, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    for (String cn : cursor.getColumnNames()) {
                        str = cursor.getString(cursor.getColumnIndex(cn));
                    }
                }
                while (cursor.moveToNext());
            }
            if (str==null){
                sd=0;
            }
            else {
                sd = Integer.valueOf(str);
            }
            TimeCategory timePerCategory = new TimeCategory(c,sd);
            res.add(timePerCategory);
        }
        String s = "";
        Collections.sort(res, new Comparator<TimeCategory>() {
            @Override
            public int compare(TimeCategory timeCategory, TimeCategory t1) {
                if(timeCategory.getSegmentValue()>t1.getSegmentValue()){
                    return -1;
                }
                else return 1;
            }
        });
        return res;
    }

    public int getCountRecordFromCategory(SQLiteDatabase database,Category category,long startDate,long endDate){
        int res = 0;
        sqlQuery = "select * from "+TIME_RECORD_TABLE+" where "+CATEGORY_ID_REF+" ="+String.valueOf(category.getId()+" and "+START_TIME+" between "+startDate+" and "+endDate);
        Cursor cursor = database.rawQuery(sqlQuery,null,null);
        res = cursor.getCount();
        cursor.close();
        return res;
    }

    /**Каскадное удаление категории*/
    public void deleteCascadeCategory(SQLiteDatabase database,Category category){
        int res =  database.delete (CATEGORY_TABLE, CATEGORY_NAME+"=?", new String[] {category.getCategoryName()});
        sqlQuery = "select "+TIME_ID+" from "+TIME_RECORD_TABLE+" where "+CATEGORY_ID_REF+ " = ?";
        Cursor cursor = database.rawQuery(sqlQuery,new String[]{String.valueOf(category.getId())},null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(TIME_ID);
        int idValue;//значение id
        if (cursor != null && cursor.moveToFirst()) {
            do {
                idValue = cursor.getInt(idx);
               int i =  deleteTimeRecord(database,idValue);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void deleteCascadePhoto(SQLiteDatabase database,Photo photo){
        ArrayList<Integer> removedCategoryIds = new ArrayList<>();
        deleteEntity(database,DbUtils.PHOTO_TABLE,DbUtils.PHOTO_ID,new String[]{String.valueOf(photo.getId())});
        sqlQuery = "select * from "+TIME_PHOTO_TABLE+" where "+PHOTO_ID_REF+" ="+String.valueOf(photo.getId());
        Cursor cursor = database.rawQuery(sqlQuery,null,null);
        deleteEntity(database,TIME_PHOTO_TABLE,PHOTO_ID_REF,new String[]{String.valueOf(photo.getId())});
        cursor.moveToFirst();
        int catIdIdx = cursor.getColumnIndex(TIME_ID_REF);
        int categoryIdValue;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                categoryIdValue = cursor.getInt(catIdIdx);
                removedCategoryIds.add(categoryIdValue);
            }
            while (cursor.moveToNext());
        }
        for (int id:removedCategoryIds){
            deleteTimeRecord(database,id);
        }
        cursor.close();
    }
}
