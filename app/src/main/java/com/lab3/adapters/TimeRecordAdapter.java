package com.lab3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import com.lab3.R;
import com.lab3.domain.Photo;
import com.lab3.domain.TimeRecord;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Александр on 25.11.2016.
 */
public class TimeRecordAdapter extends BaseAdapter {
    private static final String LOG_TAG = "TimeRecordAdapter";
    private List<TimeRecord> data;
    private LayoutInflater inflater;
    private Context ctx;
    private  int LayResId;
    private  DateFormat dfISO;
    private Date date;

    public TimeRecordAdapter(Context context, int resource, List<TimeRecord> objects) {
        this.ctx =context;
        this.LayResId = resource;
        this.data = objects;
        inflater = (LayoutInflater) context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dfISO = new  SimpleDateFormat("dd-MM-yy:HH:mm");
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;//0
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
            row = inflater.inflate(LayResId,viewGroup,false);
            TimeRecord record = getRecord(i);

            TextView meetName = (TextView) row.findViewById(R.id.desc_value);
            meetName.setText(record.getDescription());

            TextView startDate = (TextView) row.findViewById(R.id.start_time_value);
            date = new Date(record.getStartDate());
            startDate.setText(dfISO.format(date));

            TextView catrgory = (TextView) row.findViewById(R.id.category_value);
            catrgory.setText(record.getCategory().getCategoryName());

            TextView endTime = (TextView) row.findViewById(R.id.etv);
            date = new Date(record.getEndDate());
            endTime.setText(dfISO.format(date));

            TextView segment = (TextView) row.findViewById(R.id.segment_value);
            segment.setText(record.getOtr());

            LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.photos_layout);
            for (Photo photo:record.getPhoto()){
                ImageView imageView = new ImageView(ctx);
                imageView.setImageBitmap(photo.getImage());
                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.addView(imageView);
            }
        return row;
    }

    public TimeRecord getRecord(int Position){
        return (TimeRecord) getItem(Position);
    }
}
