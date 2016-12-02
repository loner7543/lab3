package com.lab3;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.lab3.db.DbUtils;
import com.lab3.domain.Category;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {
    private ListView frequent_sessions;
    private ListView sum_time;
    private ListView time_from_category;
    private PieChart graficoPartidos;
    private RelativeLayout parentLayout;
    private LinearLayout checkboxLayout;
    private SQLiteDatabase database;
    private DbUtils utils;
    private ArrayList<Category> allCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //parentLayout = (RelativeLayout) findViewById(R.layout.content_statistics);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        frequent_sessions = (ListView) findViewById(R.id.frequent_sessions);
        sum_time = (ListView) findViewById(R.id.sum_time);
        time_from_category = (ListView) findViewById(R.id.time_from_category);
        utils = new DbUtils(this, DbUtils.DATABASE_NAME, DbUtils.DATABASE_VERSION);
        database = utils.getWritableDatabase();
        allCategories = utils.parseCursor(utils.getAllRecords(database,DbUtils.CATEGORY_TABLE));
       /* for (Category category:allCategories){
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(category.getCategoryName());
            checkboxLayout.addView(checkBox);
        }*/

        graficoPartidos = (PieChart) findViewById(R.id.asdf);
        graficoPartidos.getBackgroundPaint().setColor(Color.WHITE);
        drawPie();
    }

    private void drawPie() {
        Segment seg0 = new Segment(" a", 20);
        Segment seg1 = new Segment("s ", 10);
        Segment seg2 = new Segment(" d", 10);
        Segment seg3= new Segment("f",5);

        graficoPartidos.addSeries(seg0, new SegmentFormatter(Color.rgb(106, 168, 79), Color.BLACK,Color.BLACK, Color.BLACK));
        graficoPartidos.addSeries(seg1, new SegmentFormatter(Color.rgb(255, 0, 0), Color.BLACK,Color.BLACK, Color.BLACK));
        graficoPartidos.addSeries(seg2, new SegmentFormatter(Color.rgb(255, 153, 0), Color.BLACK,Color.BLACK, Color.BLACK));
        graficoPartidos.addSeries(seg3, new SegmentFormatter(Color.rgb(200, 188, 0), Color.BLACK,Color.BLACK, Color.BLACK));
        PieRenderer pieRenderer = graficoPartidos.getRenderer(PieRenderer.class);
        pieRenderer.setDonutSize((float) 0 / 100,   PieRenderer.DonutMode.PERCENT);
    }


}
