package com.lab3;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.lab3.domain.Photo;

import java.util.List;

/**
 * Created by Александр on 29.11.2016.
 */

public class CustomPhotoAdapter extends ArrayAdapter<Photo> {
    private LayoutInflater inflater;
    private Photo tempValues;
    private List<Photo> data;
    private Context ctx;
    private  int LayResId;

    public CustomPhotoAdapter(Context context, int resource, List<Photo> objects) {
        super(context, resource, objects);
        this.ctx = context;
        this.LayResId = resource;
        this.data = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Nullable
    @Override
    public Photo getItem(int position) {
        return data.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(LayResId, parent, false);
        tempValues =  getRecord(position);
        ImageView container = (ImageView) row.findViewById(R.id.image_item);
        container.setImageBitmap(tempValues.getImage());
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position,convertView,parent);
    }

    public Photo getRecord(int Position){
        return  getItem(Position);
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
