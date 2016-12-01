package com.lab3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lab3.domain.Photo;

import java.util.List;

/**
 * Created by Александр on 25.11.2016.
 */

/**
 * адаптер для ListView
 *
 * */
public class PhotoAdapter extends BaseAdapter {
    private List<Photo> data;
    private LayoutInflater layoutInflater;
    private Context ctx;
    private  int LayResId;
    private LayoutInflater inflater;

    public PhotoAdapter(){

    }

    public PhotoAdapter(Context context, int resource, List<Photo> objects) {
        this.ctx =context;
        this.LayResId = resource;
        this.data = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View row = convertView;
        if (row==null){
            row = inflater.inflate(LayResId,viewGroup,false);
            Photo currMeet = getPhoto(i);
            ImageView meetName = (ImageView) row.findViewById(R.id.bmp);
            meetName.setImageBitmap(currMeet.getImage());
        }
        return row;
    }

    public Photo getPhoto(int Position){
        return (Photo) getItem(Position);
    }
}
