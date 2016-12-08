package com.lab3.domain;

/**
 * Created by Александр on 08.12.2016.
 */

public class PieData {
    private String Category;
    private int Time;

    public PieData(String category, int time) {
        Category = category;
        Time = time;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public int getTime() {
        return Time;
    }

    public void setTime(int time) {
        Time = time;
    }
}
