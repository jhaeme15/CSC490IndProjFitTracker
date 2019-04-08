package com.example.fitnesstrackertest;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Author: Jared Haeme
 * Date: 4/11/2019
 * A class to represent a single lift that has a name, id, notes and a list of sets.
 */

public class Lift implements Serializable {
    //Data Fields
    private String liftName;
    private ArrayList<Set> sets;
    private int id;
    private String notes;

    /**
     * Constructor that initializes a lift object without a list of sets
     * @param id an int unique id
     * @param liftName a String that give the name of the lift
     * @param notes a string of notes about a lift
     */
    public Lift(int id, String liftName, String notes){
        this.liftName = liftName;
        this.sets=new ArrayList<Set>();
        this.id=id;
        this.notes=notes;
    }

    /**
     * Constructor that initializes a lift object with a list of sets
     * @param id an int unique id
     * @param liftName a String that give the name of the lift
     * @param notes a string of notes about a lift
     * @param sets an arraylist of type sets that gives the sets of the lift
     */
    public Lift(int id, String liftName, String notes, ArrayList<Set> sets){
        this.id=id;
        this.liftName = liftName;
        this.sets=new ArrayList<Set>(sets);
        this.notes=notes;
    }
    /**
     * Gets id
     * @return int unique lift id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id
     * @param id int unique lift id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * gets notes of lift
     * @return a string of notes on a lift
     */
    public String getNotes() {
        return notes;
    }

    /**
     *
     * @param notes a string of notes on a lift
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     *
     * @return a string that gives the name of the lift
     */
    public String getLiftName() {
        return liftName;
    }

    /**
     *
     * @param liftName a string that is the name of the lift
     */
    public void setLiftName(String liftName) {
        this.liftName = liftName;
    }

    /**
     *
     * @return an arraylist of type set that gives a list of sets for a lift
     */
    public ArrayList<Set> getSets() {
        return sets;
    }

    /**
     *
     * @param sets an arraylist of sets  that gives a list of sets for a lift
     */
    public void setSets(ArrayList<Set> sets) {
        this.sets = sets;
    }

    /**
     *
     * @return  a string that displays the lift name and the weight and reps for each set
     */
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
