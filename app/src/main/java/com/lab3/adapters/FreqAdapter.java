package com.lab3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lab3.R;
import com.lab3.domain.AppMonth;
import com.lab3.domain.TimeCategory;

import java.util.List;

/**
 * Created by Александр on 17.12.2016.
 */

public class FreqAdapter extends BaseAdapter {
    private List<TimeCategory> data;
    private Context ctx;
    private  int LayResId;
    private LayoutInflater inflater;

    public FreqAdapter(Context ctx, int layResId,List<TimeCategory> data ) {
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
        row = inflater.inflate(LayResId,viewGroup,false);
        TimeCategory currEntity = getEntity(i);

        TextView monthName = (TextView) row.findViewById(R.id.freq_text);
        monthName.setText(currEntity.getCategory().getCategoryName());
        return row;
    }
    public TimeCategory getEntity(int Position){
        return (TimeCategory) getItem(Position);
    }

}
