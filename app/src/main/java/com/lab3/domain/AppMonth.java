package com.lab3.domain;

/**
 * Created by Александр on 13.12.2016.
 */

public class AppMonth {
    private String name;
    private int value;

    public AppMonth(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
