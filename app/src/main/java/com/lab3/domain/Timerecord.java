package com.lab3.domain;

import java.util.Date;
import java.util.List;

/**
 * Created by Александр on 13.11.2016.
 */
//комплесная запись в бд
public class TimeRecord {
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
}
