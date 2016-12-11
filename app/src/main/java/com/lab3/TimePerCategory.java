package com.lab3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.lab3.adapters.TimePerCategoryAdapter;
import com.lab3.domain.TimeCategory;

import java.util.LinkedList;
import java.util.List;

public class TimePerCategory extends AppCompatActivity {
    private ListView listView;
    private Intent intent;
    private TimePerCategoryAdapter adapter;
    private List<TimeCategory> sqlData;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_per_category);
        setTitle("Суммарное время по выбранным категориям");
        sqlData = new LinkedList<>();
        intent = getIntent();
        count = intent.getIntExtra("count",0);
        for (int i = 0;i<count;i++){
            sqlData.add((TimeCategory) intent.getSerializableExtra("data"+i));
        }
        listView = (ListView) findViewById(R.id.time_per_category_list);
        adapter = new TimePerCategoryAdapter(sqlData,this,R.layout.category_time);
        listView.setAdapter(adapter);
    }
}
