package com.lab3.domain;

import java.util.Date;
import java.util.List;

/**
 * Created by alma0516 on 11/25/2016.
 */

public class TimeRecord {
    private  int Id;
    private Date StartDate;
    private Date EndDate;
    private String Description;
    private Category Category;
    private String Otr;
    private List<Photo> photos;

    public TimeRecord(Date startDate, Date endDate, String description, com.lab3.domain.Category category, List<Photo> photo, String otr) {
        StartDate = startDate;
        EndDate = endDate;
        Description = description;
        Category = category;
        this.photos = photo;
        Otr = otr;
    }

    public TimeRecord(){

    }

    public TimeRecord(Date endDate, String description, com.lab3.domain.Category category, String otr, List<Photo> photos, Date startDate) {
        EndDate = endDate;
        Description = description;
        Category = category;
        Otr = otr;
        this.photos = photos;
        StartDate = startDate;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
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
