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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.lab3.adapters.TimePerCategoryAdapter;
import com.lab3.db.DbUtils;
import com.lab3.domain.Category;
import com.lab3.domain.PieData;
import com.lab3.domain.TimeCategory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener  {
    private ListView frequent_sessions;
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

    private RadioGroup radioGroup;
    private RadioButton monthButton;
    private RadioButton randomRadioButton;
    private boolean perMonth = false;
    private boolean ranadomPeriod = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        frequent_sessions = (ListView) findViewById(R.id.frequent_sessions);
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
        radioGroup = (RadioGroup) findViewById(R.id.filter_type_radio);
        monthButton = (RadioButton) findViewById(R.id.radioMonth);
        monthButton.setOnClickListener(this);
        randomRadioButton = (RadioButton) findViewById(R.id.radioRandom);
        randomRadioButton.setOnClickListener(this);
        drawPie();
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

    public void onShowSortList(View view){
        Intent intent = new Intent(this,SortActivity.class);
        startActivity(intent);
    }

    public void onShowFreqList(View view){
        List<TimeCategory> data = utils.getFrequent(database);
        TimePerCategoryAdapter adapter = new TimePerCategoryAdapter(data,getApplicationContext(),R.layout.category_time);
        frequent_sessions.setAdapter(adapter);
    }

    public void onShowListStat(View view){
        String toDate = "";
        String fromDate = "";
        if (perMonth){
            fromDate = "";
        }
        if (ranadomPeriod){
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
            fromDate = getDate(fromYear,fromManth,fromDay,fronHour,fromMinute);
            toDate = getDate(toYear,toMonth,toDay,toHour,toMinute);
        }
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
            TimeCategory timeCategory = utils.sumTimePerCategory(database,c,fromDate,toDate);
            intent.putExtra("data"+i,timeCategory);
            i++;
        }
        intent.putExtra("count",i);
        startActivity(intent);
    }


    @Override
    public void onClick(View view) {
        RadioButton rb = (RadioButton)view;
        switch (rb.getId()){
            case R.id.radioMonth:{
                perMonth = true;
                break;
            }
            case R.id.radioRandom:{
                ranadomPeriod = true;
                break;
            }
        }
    }

    public String getDate(int year,int month,int day,int hour,int minute ) {
        String s = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day) + " " + String.valueOf(hour) + ":" + String.valueOf(minute);
        return s;
    }
}
