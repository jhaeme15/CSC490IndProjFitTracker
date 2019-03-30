package com.example.fitnesstrackertest;
import android.app.LoaderManager;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Workout implements Serializable {
    private LocalDate date;
    private String description;
    private ArrayList<Lift> lifts;
    private int id;


    public ArrayList<Lift> getLifts() {
        return lifts;
    }

    public void setLifts(ArrayList<Lift> lifts) {
        this.lifts = lifts;
    }

    public Workout(int id, LocalDate date, String description){
        this.date=date;
        this.description=description;
        lifts=new ArrayList<Lift>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Workout(int id, LocalDate date, String description, ArrayList<Lift> lifts){
        this.id=id;
        this.date=date;
        this.description=description;
        this.lifts=new ArrayList<Lift>(lifts);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString(){
        return date.getMonthValue()+"/"+date.getDayOfMonth()+"/"+date.getYear()+"\n"+description;
    }
}
