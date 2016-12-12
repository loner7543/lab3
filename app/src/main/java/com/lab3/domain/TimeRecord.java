package com.lab3.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by alma0516 on 11/25/2016.
 */
public class TimeRecord implements Serializable {
    private  int Id;
    private long StartDate;
    private long EndDate;
    private String Description;
    private Category Category;
    private String Otr;
    private List<Photo> photos;

    public TimeRecord(long startDate, long endDate, String description, com.lab3.domain.Category category, List<Photo> photo, String otr) {
        StartDate = startDate;
        EndDate = endDate;
        Description = description;
        Category = category;
        this.photos = photo;
        Otr = otr;
    }

    public TimeRecord(String description) {
        Description = description;
    }

    public TimeRecord(int id, long startDate, long endDate, String description, com.lab3.domain.Category category, String otr, List<Photo> photos) {
        Id = id;
        StartDate = startDate;
        EndDate = endDate;
        Description = description;
        Category = category;
        Otr = otr;
        this.photos = photos;
    }

    public TimeRecord(int id, String otr, com.lab3.domain.Category category, String description, long endDate, long startDate) {
        Id = id;
        Otr = otr;
        Category = category;
        Description = description;
        EndDate = endDate;
        StartDate = startDate;
    }

    public TimeRecord(){

    }

    public long getStartDate() {
        return StartDate;
    }

    public void setStartDate(long startDate) {
        StartDate = startDate;
    }

    public long getEndDate() {
        return EndDate;
    }

    public void setEndDate(long endDate) {
        EndDate = endDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public com.lab3.domain.Category getCategory() {
        return Category;
    }

    public void setCategory(com.lab3.domain.Category category) {
        Category = category;
    }

    public String getOtr() {
        return Otr;
    }

    public void setOtr(String otr) {
        Otr = otr;
    }

    public List<Photo> getPhoto() {
        return photos;
    }

    public void setPhoto(List<Photo> photo) {
        this.photos = photo;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
