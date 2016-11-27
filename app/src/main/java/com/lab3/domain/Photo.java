package com.lab3.domain;

import android.graphics.Bitmap;

/**
 * Created by Александр on 13.11.2016.
 */

public class Photo {
    public String PhotoName;
    private  int Id;
    private Bitmap Image;

    public Photo(){

    }

    public Photo(Bitmap image, int id) {
        Image = image;
        Id = id;
    }

    public Bitmap getImage() {
        return Image;
    }

    public void setImage(Bitmap image) {
        Image = image;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getPhotoName() {
        return PhotoName;
    }

    public void setPhotoName(String photoName) {
        PhotoName = photoName;
    }
}
