package com.lab3;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lab3.db.DbUtils;

public class CatrgoryActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private String[] categories;
    private SQLiteDatabase database;
    private DbUtils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catrgory);
        utils = new DbUtils(this, DbUtils.DATABASE_NAME, DbUtils.DATABASE_VERSION);
        database = utils.getReadableDatabase();//дает бд на чтение
        listView = (ListView) findViewById(R.id.category_list);
        parseCursor(utils.getAllRecords(database,DbUtils.CATEGORY_TABLE));
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, categories);
        listView.setAdapter(adapter);
    }

    public void parseCursor(Cursor cursor) {
        categories = new String[cursor.getCount()];
        int i = 0;
        if (cursor != null && cursor.moveToFirst()) {
            int categoryIdx = cursor.getColumnIndex(DbUtils.CATEGORY_NAME);
            do {
                categories[i] = cursor.getString(categoryIdx);
            }
            while (cursor.moveToNext());
        }
        i = 0;
    }
}
