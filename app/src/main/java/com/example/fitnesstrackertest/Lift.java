package com.example.fitnesstrackertest;

import java.io.Serializable;
import java.util.ArrayList;

public class Lift implements Serializable {
    private String lift;
    private ArrayList<Set> sets;


    public Lift(String lift){
        this.lift=lift;
        this.sets=new ArrayList<Set>();
    }

    public Lift(String lift, ArrayList<Set> sets){
        this.lift=lift;
        this.sets=new ArrayList<Set>(sets);
    }
    public String getLift() {
        return lift;
    }

    public void setLift(String lift) {
        this.lift = lift;
    }

    public ArrayList<Set> getSets() {
        return sets;
    }

    public void setSets(ArrayList<Set> sets) {
        this.sets = sets;
    }

    @Override
    public String toString(){
        String display=lift+"\n";
        display+=sets.get(0).toString();
        for (int i=1; i<sets.size(); i++){
            display+=", "+sets.get(i).toString();
        }
        return display;
    }

}
