package com.lab3.domain;

/**
 * Created by Александр on 13.11.2016.
 */
//таблица ктегории
public class Category {
    private int Id;
    private String CategoryName;

    public Category(int id, String categoryName) {
        CategoryName = categoryName;
        Id = id;
    }

    public Category(String categoryName) {
        CategoryName = categoryName;
    }

    public  Category(){}

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    @Override
    public String toString() {
        return CategoryName;
    }
}
