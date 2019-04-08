package com.example.fitnesstrackertest;

import java.io.Serializable;
import java.util.ArrayList;

public class Lift implements Serializable {
    private String liftName;
    private ArrayList<Set> sets;
    private int id;
    private String notes;

    public Lift(int id, String liftName, String notes){
        this.liftName = liftName;
        this.sets=new ArrayList<Set>();
        this.id=id;
        this.notes=notes;
    }

    public Lift(int id, String liftName, String notes, ArrayList<Set> sets){
        this.id=id;
        this.liftName = liftName;
        this.sets=new ArrayList<Set>(sets);
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

    public String getLiftName() {
        return liftName;
    }

    public void setLiftName(String liftName) {
        this.liftName = liftName;
    }

    public ArrayList<Set> getSets() {
        return sets;
    }

    public void setSets(ArrayList<Set> sets) {
        this.sets = sets;
    }

    @Override
    public String toString(){
        String display= liftName +"\n";
        if(sets.size()>0) {
            display += sets.get(0).toString();
            for (int i = 1; i < sets.size(); i++) {
                display += ", " + sets.get(i).toString();
            }
        }
        return display;
    }

}
