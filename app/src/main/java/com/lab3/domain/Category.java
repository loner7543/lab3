package com.lab3.domain;

/**
 * Created by Александр on 13.11.2016.
 */

public class Category {
    private String CategoryName;
    private int  Record;

    public Category(int record, String categoryName) {
        Record = record;
        CategoryName = categoryName;
    }

    public  Category(){}

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public int getRecord() {
        return Record;
    }

    public void setRecord(int record) {
        Record = record;
    }
}
