package com.lab3;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.lab3.adapters.TimePerCategoryAdapter;
import com.lab3.db.DbUtils;
import com.lab3.domain.Category;
import com.lab3.domain.TimeCategory;

import java.util.List;

public class SortActivity extends AppCompatActivity {
    private ListView listView;
    private List<TimeCategory> sortData;
    private SQLiteDatabase database;
    private DbUtils utils;
    private List<Category> allCategories;
    private TimePerCategoryAdapter adapter;
    private long startDate;
    private long endDate;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        setTitle("Самое большое суммарное время по категориям");
        intent = getIntent();
        utils = new DbUtils(this, DbUtils.DATABASE_NAME, DbUtils.DATABASE_VERSION);
        database = utils.getWritableDatabase();
        allCategories = utils.parseCursor(utils.getAllRecords(database,DbUtils.CATEGORY_TABLE));

        listView = (ListView) findViewById(R.id.sum_time_list);

        startDate = intent.getLongExtra("startDate",0);
        endDate = intent.getLongExtra("endDate",0);
        sortData  = utils.sumTimeOrder(database,allCategories,startDate,endDate);
        adapter = new TimePerCategoryAdapter(sortData,this,R.layout.category_time);
        listView.setAdapter(adapter);


    }
}
