package com.example.fitnesstrackertest;
import android.app.LoaderManager;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Workout implements Serializable {
    private LocalDate date;
    private String description;
    private String notes;
    private ArrayList<Lift> lifts;
    private int id;



    public Workout(int id, LocalDate date, String description, String notes){
        this.date=date;
        this.description=description;
        lifts=new ArrayList<Lift>();
        this.id=id;
        this.notes=notes;
    }
    public Workout(int id, LocalDate date, String description, String notes,ArrayList<Lift> lifts){
        this.id=id;
        this.date=date;
        this.description=description;
        this.lifts=new ArrayList<Lift>(lifts);
        this.notes=notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    public ArrayList<Lift> getLifts() {
        return lifts;
    }

    public void setLifts(ArrayList<Lift> lifts) {
        this.lifts = lifts;
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

    public String getDateStr(){
        return date.getMonthValue()+"/"+date.getDayOfMonth()+"/"+date.getYear();
    }

    @Override
    public String toString(){
        if(date!=null) {
            return date.getMonthValue() + "/" + date.getDayOfMonth() + "/" + date.getYear() + "\n" + description;
        }else{
            return "";
        }
    }
}
