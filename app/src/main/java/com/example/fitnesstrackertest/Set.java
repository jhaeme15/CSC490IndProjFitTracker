package com.example.fitnesstrackertest;

import java.io.Serializable;

/**
 * Author: Jared Haeme
 * Date: 4/11/2019
 * A class the represents a set which has a unique id and a weight and number of reps
 */
public class Set implements Serializable {
    //Data fields
    private int weight;
    private int reps;
    private int id;

    /**
     * Constructor to initialize set with id, reps and weight
     * @param id int an unique id
     * @param weight int the weight for the set
     * @param reps int the number of reps for the set
     */
    public Set(int id, int weight, int reps){
        this.id=id;
        this.weight=weight;
        this.reps=reps;
    }

    /**
     *
     * @return int a unique id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id int a unique id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return int the weight used for the set
     */
    public int getWeight() {
        return weight;
    }

    /**
     *
     * @param weight int the weight used for the set
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     *
     * @return int the number of reps for the set
     */
    public int getReps() {
        return reps;
    }

    /**
     *
     * @param reps int the number of reps for the set
     */
    public void setReps(int reps) {
        this.reps = reps;
    }

    /**
     *
     * @return a string displays the weight and reps for the set
     */
    public String toString(){
        return weight+" x "+reps;
    }
}
