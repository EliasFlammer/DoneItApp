package com.example.myjavaapp.ToDoPackage;

public class TodoModel {
    private int id;
    private int status;
    private String title;

    private Intensity intensity;
    private Duration duration;
    private int hidden;

    //getter and setter methods are here so that if you make a change to something regarding the variable,
    // you dont have to change it multiple times but you can change it in those methods here
    public enum Intensity {
        LOW,
        MEDIUM,
        HIGH
    }

    public enum Duration{
        SHORT,
        MODERATE,
        LONG
    }

    public Intensity getIntensity() {
        return intensity;
    }

    public void setIntensity(Intensity intensity) {
        this.intensity = intensity;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int isHidden() {
        return hidden;
    }

    public void setHidden(int hidden) {
        this.hidden = hidden;
    }


}

