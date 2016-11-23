package com.lab3.domain;

/**
 * Created by Александр on 13.11.2016.
 */

public class Photo {
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    private  int Id;
    private byte[] Image;

    public Photo(){

    }

    public Photo(byte[] image, int id) {
        Image = image;
        Id = id;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }

    public Photo(byte[] image) {
        Image = image;
    }
}
