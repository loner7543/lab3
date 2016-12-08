package com.lab3.domain;

import java.io.Serializable;

/**
 * Created by Александр on 09.12.2016.
 */

public class SerialPhoto implements Serializable {
    private int id;
    private byte[] data;

    public SerialPhoto(int id, byte[] data) {
        this.id = id;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
