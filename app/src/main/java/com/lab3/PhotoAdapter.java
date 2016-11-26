package com.lab3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lab3.domain.Photo;

import java.util.List;

/**
 * Created by Александр on 25.11.2016.
 */

public class PhotoAdapter extends BaseAdapter {
    private List<Photo> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private  int LayResId;

    public PhotoAdapter(){

    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
