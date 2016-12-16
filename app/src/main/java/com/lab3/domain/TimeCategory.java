package com.lab3.domain;

/**
 * Created by Александр on 11.12.2016.
 */

import java.io.Serializable;

/**
 * Данные для статистики по выбранным категориям
 * */
public class TimeCategory implements Serializable {
    private Category category;
    private int segmentValue;

    public TimeCategory(Category category, int segmentValue) {
        this.category = category;
        this.segmentValue = segmentValue;
    }

    public int getSegmentValue() {
        return segmentValue;
    }

    public void setSegmentValue(int segmentValue) {
        this.segmentValue = segmentValue;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return category.getCategoryName();
    }
}
