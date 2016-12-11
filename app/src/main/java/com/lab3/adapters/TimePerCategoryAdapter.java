package com.lab3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lab3.R;
import com.lab3.domain.TimeCategory;

import java.util.List;

/**
 * Created by Александр on 11.12.2016.
 */

public class TimePerCategoryAdapter extends BaseAdapter {
    private List<TimeCategory> data;
    private Context ctx;
    private  int LayResId;
    private LayoutInflater inflater;

    public TimePerCategoryAdapter(List<TimeCategory> data, Context ctx, int layResId) {
        this.data = data;
        this.ctx = ctx;
        LayResId = layResId;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        if (row==null){
            row = inflater.inflate(LayResId,viewGroup,false);
            TimeCategory currMeet = getEntity(i);

            TextView categoryName = (TextView) row.findViewById(R.id.category_name);
            categoryName.setText(currMeet.getCategory().getCategoryName()+":");

            TextView categoryValue = (TextView) row.findViewById(R.id.ghh);
            categoryValue.setText(String.valueOf(currMeet.getSegmentValue())+"  минут");
        }
        return row;
    }

    public TimeCategory getEntity(int Position){
        return (TimeCategory) getItem(Position);
    }
}
