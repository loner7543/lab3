package com.lab3;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.lab3.db.DbUtils;
import com.lab3.domain.Category;
import com.lab3.domain.PieData;
import com.lab3.domain.TimeCategory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class StatisticsActivity extends AppCompatActivity {
    private ListView frequent_sessions;
    private ListView sum_time;
    private ListView time_from_category;
    private PieChart graficoPartidos;
    private SQLiteDatabase database;
    private DbUtils utils;
    private ArrayList<Category> allCategories;
    private  ArrayAdapter<Category> adapter;
    private SparseBooleanArray checked;
    private   ArrayList<Category> selectedItems;
    private DatePicker fromDatePicker;
    private TimePicker fromTimePicker;
    private DatePicker toDatePicker;
    private TimePicker toTomePicker;

    private Calendar calendar;

    private int fromDay;
    private int fromManth;
    private int fromYear;
    private int fronHour;
    private int fromMinute;

    private int toDay;
    private int toMonth;
    private int toYear;
    private int toHour;
    private int toMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        frequent_sessions = (ListView) findViewById(R.id.frequent_sessions);
        sum_time = (ListView) findViewById(R.id.sum_time);
        time_from_category = (ListView) findViewById(R.id.time_from_category);

        fromDatePicker = (DatePicker) findViewById(R.id.fromDpStat);
        fromTimePicker = (TimePicker) findViewById(R.id.fromTPStat);

        toDatePicker = (DatePicker) findViewById(R.id.ToDpStatDp);
        toTomePicker = (TimePicker) findViewById(R.id.toTPStat);

        utils = new DbUtils(this, DbUtils.DATABASE_NAME, DbUtils.DATABASE_VERSION);
        database = utils.getWritableDatabase();
        allCategories = utils.parseCursor(utils.getAllRecords(database,DbUtils.CATEGORY_TABLE));
        adapter = new ArrayAdapter<Category>(this,
                android.R.layout.simple_list_item_multiple_choice, allCategories);
        time_from_category.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        time_from_category.setAdapter(adapter);
        selectedItems = new ArrayList<Category>();

        graficoPartidos = (PieChart) findViewById(R.id.asdf);
        graficoPartidos.getBackgroundPaint().setColor(Color.WHITE);
        calendar = Calendar.getInstance();
        drawPie();
        List<TimeCategory> ds = utils.sumTimeOrder(database,allCategories);
        String s = "efdef";
    }

    private void drawPie() {
        try{
        Random random = new Random();
        ArrayList<PieData> times = new ArrayList<>();
        for (Category category:allCategories){
            times.add(new PieData(category.getCategoryName(),utils.pieData(database,category)));
        }
        for (PieData pieData:times){
            if (pieData.getTime()!=0){//убираю сегменты с нулевыми отрезками, дабы не загромождать подпись
                Segment segment = new Segment(pieData.getCategory(),pieData.getTime());
                graficoPartidos.addSeries(segment, new SegmentFormatter(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)), Color.BLACK,Color.BLACK, Color.BLACK));
            }
        }
        PieRenderer pieRenderer = graficoPartidos.getRenderer(PieRenderer.class);
        pieRenderer.setDonutSize((float) 0 / 100,   PieRenderer.DonutMode.PERCENT);
        }
        catch (NullPointerException e){
            Toast toast = Toast.makeText(this,"Нет данных для построения круговой диаграммы",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void onShowListStat(View view){
        fromDay = fromDatePicker.getDayOfMonth();
        fromManth = fromDatePicker.getMonth();
        fromYear = fromDatePicker.getYear();

        fronHour = fromTimePicker.getCurrentHour();
        fromMinute = fromTimePicker.getCurrentMinute();

        toDay = toDatePicker.getMonth();
        toMonth = toDatePicker.getDayOfMonth();
        toYear = toDatePicker.getYear();

        toHour = toTomePicker.getCurrentHour();
        toMinute = toTomePicker.getCurrentMinute();

        checked = time_from_category.getCheckedItemPositions();
        for (int i = 0; i < checked.size(); i++) {
            int position = checked.keyAt(i);
            // Add sport if it is checked i.e.) == TRUE!
            if (checked.valueAt(i))
                selectedItems.add(adapter.getItem(position));
        }
        Intent intent = new Intent(this,TimePerCategory.class);
        int i = 0;
        for (Category c:selectedItems){
            TimeCategory timeCategory = utils.sumTimePerCategory(database,c);
            intent.putExtra("data"+i,timeCategory);
            i++;
        }
        intent.putExtra("count",i);
        startActivity(intent);
    }


}
