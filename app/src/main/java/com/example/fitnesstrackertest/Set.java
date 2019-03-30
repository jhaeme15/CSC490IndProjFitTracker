package com.example.fitnesstrackertest;

import java.io.Serializable;

public class Set implements Serializable {
    private int weight;
    private int reps;
    private int id;

    public Set(int id, int weight, int reps){
        this.id=id;
        this.weight=weight;
        this.reps=reps;
    }
    public Set(int reps){
        this.weight=0;
        this.reps=reps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public String toString(){
        return weight+" x "+reps;
    }
}
