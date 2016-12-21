package com.lab3;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.lab3.adapters.FreqAdapter;
import com.lab3.adapters.MonthAdapter;
import com.lab3.adapters.TimePerCategoryAdapter;
import com.lab3.db.DbUtils;
import com.lab3.domain.AppMonth;
import com.lab3.domain.Category;
import com.lab3.domain.PieData;
import com.lab3.domain.TimeCategory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {
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

    private long startDate;
    private long endDate;

    private RadioGroup radioGroup;
    private RadioButton monthButton;
    private RadioButton randomRadioButton;
    private boolean perMonth = false;
    private boolean ranadomPeriod = false;
    private Calendar fromDateCalendar;
    private Calendar toDateCalendar;
    private Spinner MonthSpinner;
    private MonthAdapter monthAdapter;
    private List<AppMonth> months;
    private AppMonth selectedMonth;
    private TextView ValidationDateStatistics;
    private TextView ValidationHoursTextView;

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
        fromDateCalendar = Calendar.getInstance();
        toDateCalendar = Calendar.getInstance();
        initMonths();
        monthAdapter = new MonthAdapter(months,this,R.layout.month_item);
        MonthSpinner = (Spinner) findViewById(R.id.months);
        MonthSpinner.setAdapter(monthAdapter);
        MonthSpinner.setOnItemSelectedListener(this);
        ValidationDateStatistics = (TextView) findViewById(R.id.ValidateStatDate);
        ValidationHoursTextView = (TextView) findViewById(R.id.ValidationStstisticsHour);
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

    //ревлизация не совсем ок - кидаю даты на др активити и там делаю запрос  используя их
    public void onShowSortList(View view){
        ValidationDateStatistics.setText("");
        ValidationHoursTextView.setText("");

        MonthSpinner.setEnabled(true);
        toDatePicker.setEnabled(true);
        fromDatePicker.setEnabled(true);
        toTomePicker.setEnabled(true);
        fromTimePicker.setEnabled(true);
        Intent intent = new Intent(this,SortActivity.class);
        setDate();
        if (validate()){
            intent.putExtra("startDate",startDate);
            intent.putExtra("endDate",endDate);
            startActivity(intent);
        }

    }

    public void onShowFreqList(View view){
        ValidationDateStatistics.setText("");
        ValidationHoursTextView.setText("");
        MonthSpinner.setEnabled(true);

        toDatePicker.setEnabled(true);
        fromDatePicker.setEnabled(true);
        toTomePicker.setEnabled(true);
        fromTimePicker.setEnabled(true);
        setDate();
        if (validate()){
            List<TimeCategory> data = new LinkedList<>();
            for (Category category:allCategories){
                TimeCategory timeCategory = new TimeCategory(category,utils.getCountRecordFromCategory(database,category,startDate,endDate));
                data.add(timeCategory);
            }

            Collections.sort(data, new Comparator<TimeCategory>() {
                @Override
                public int compare(TimeCategory timeCategory, TimeCategory t1) {
                    if(timeCategory.getSegmentValue()>t1.getSegmentValue()){
                        return -1;
                    }
                    else return 1;
                }
            });
            FreqAdapter adapter = new FreqAdapter(getApplicationContext(),R.layout.freq_item,data);
            frequent_sessions.setAdapter(adapter);
        }

    }

    public void onShowListStat(View view){
        ValidationDateStatistics.setText("");
        ValidationHoursTextView.setText("");
        MonthSpinner.setEnabled(true);

        toDatePicker.setEnabled(true);
        fromDatePicker.setEnabled(true);
        toTomePicker.setEnabled(true);
        fromTimePicker.setEnabled(true);
        setDate();
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
            TimeCategory timeCategory = utils.sumTimePerCategory(database,c,startDate,endDate);
            intent.putExtra("data"+i,timeCategory);
            i++;
        }
        intent.putExtra("count",i);
        startActivity(intent);
    }

    private void setDate() {
        if (perMonth){
            calendar.set(2016,selectedMonth.getValue(),0,0,0);
            startDate = calendar.getTime().getTime();

            calendar.set(2016,selectedMonth.getValue()+1,0,0,0);
            endDate = calendar.getTime().getTime();
            perMonth = false;
        }
        if (ranadomPeriod){
            fromDay = fromDatePicker.getDayOfMonth();
            fromManth = fromDatePicker.getMonth();
            fromYear = fromDatePicker.getYear();

            fronHour = fromTimePicker.getCurrentHour();
            fromMinute = fromTimePicker.getCurrentMinute();

            toMonth = toDatePicker.getMonth();
            toDay = toDatePicker.getDayOfMonth();
            toYear = toDatePicker.getYear();

            toHour = toTomePicker.getCurrentHour();
            toMinute = toTomePicker.getCurrentMinute();

            fromDateCalendar.set(fromYear,fromManth,fromDay);
            Date fromDate = fromDateCalendar.getTime();
            fromDate.setHours(fronHour);
            fromDate.setMinutes(fromMinute);

            startDate = fromDate.getTime();

            toDateCalendar.set(toYear,toMonth,toDay);
            Date toDate = toDateCalendar.getTime();
            toDate.setHours(toHour);
            toDate.setMinutes(toMinute);
            endDate = toDate.getTime();
            ranadomPeriod = false;
        }
    }


    @Override
    public void onClick(View view) {
        RadioButton rb = (RadioButton)view;
        switch (rb.getId()){
            case R.id.radioMonth:{
                MonthSpinner.setEnabled(true);

                toDatePicker.setEnabled(false);
                fromDatePicker.setEnabled(false);
                toTomePicker.setEnabled(false);
                fromTimePicker.setEnabled(false);
                perMonth = true;
                break;
            }
            case R.id.radioRandom:{
                ranadomPeriod = true;
                MonthSpinner.setEnabled(false);

                toDatePicker.setEnabled(true);
                fromDatePicker.setEnabled(true);
                toTomePicker.setEnabled(true);
                fromTimePicker.setEnabled(true);

                break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedMonth = (AppMonth) MonthSpinner.getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void initMonths(){
        months = new LinkedList<>();
        months.add(new AppMonth("Январь",0));
        months.add(new AppMonth("Февраль",1));
        months.add(new AppMonth("Март",2));
        months.add(new AppMonth("Апрель",3));
        months.add(new AppMonth("Май",4));
        months.add(new AppMonth("Июнь",5));
        months.add(new AppMonth("Июль",6));
        months.add(new AppMonth("Август",7));
        months.add(new AppMonth("Сентябрь",8));
        months.add(new AppMonth("Октябрь",9));
        months.add(new AppMonth("Ноябрь",10));
        months.add(new AppMonth("Декабпь",11));
    }

    public boolean validate(){
        boolean res = true;
       /* if (fromYear>toYear){
            ValidationDateStatistics.setText("Дата введена неверно");
            res = false;
        }
        if (fromYear==toYear){//год один и тот же но часы неверно
            if (fronHour>toHour){
                ValidationHoursTextView.setText("Часы установлены неверно");
                res = false;
            }
        }*/
        return res;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter = null;
    }
}
