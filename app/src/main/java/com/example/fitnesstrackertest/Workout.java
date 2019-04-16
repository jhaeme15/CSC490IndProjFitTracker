package com.example.fitnesstrackertest;
import android.app.LoaderManager;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Author: Jared Haeme
 * Date:4/11/2019
 * This class represents a workout which has a date of the workout, description, notes and a list of lifts for that workout
 */

public class Workout implements Serializable, Comparable<Workout> {
    //Fields
    private LocalDate date;
    private String description;
    private String notes;
    private ArrayList<Lift> lifts;
    private int id;


    /**
     * Constructor for workout without a list of lifts
     * @param id an int that is an unique workout identifier
     * @param date a localDate object. Represents the date of the workout
     * @param description a string that describes the type of workout
     * @param notes a string of any notes about the workout
     */
    public Workout(int id, LocalDate date, String description, String notes){
        this.date=date;
        this.description=description;
        lifts=new ArrayList<Lift>();
        this.id=id;
        this.notes=notes;
    }

    /**
     * Constructor for workout with a list of lifts
     * @param id an int that is an unique workout identifier
     * @param date a localDate object. Represents the date of the workout
     * @param description a string that describes the type of workout
     * @param notes a string of any notes about the workout
     * @param lifts a arraylist of type lift that gives the lifts done in the workout
     */

    public Workout(int id, LocalDate date, String description, String notes,ArrayList<Lift> lifts){
        this.id=id;
        this.date=date;
        this.description=description;
        this.lifts=new ArrayList<Lift>(lifts);
        this.notes=notes;
    }

    /**
     * Gets id
     * @return int unique workout id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id
     * @param id int unique workout id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return string notes about workout
     */
    public String getNotes() {
        return notes;
    }

    /**
     *
     * @param notes string notes about workout
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     *
     * @return an arraylist of lifts completed in the workout
     */
    public ArrayList<Lift> getLifts() {
        return lifts;
    }

    /**
     *
     * @param lifts an arraylist of type lift that represnts the lifts completed
     */
    public void setLifts(ArrayList<Lift> lifts) {
        this.lifts = lifts;
    }


    /**
     *
     * @return a LocalDate object that gives the date of the workout
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     *
     * @param date a LocalDate object that gives the date of the workout
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     *
     * @return a String that gives a description of the workout
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description  a String that gives a description of the workout
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return a String representation of the date for display
     */
    public String getDateStr(){
        if(date!=null) {
            return date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        }else{
            return "";
        }
    }

    /**
     *
     * @return String formatting the date for firebase storage
     */
    public String getFirebaseDateStr(){
        if(date!=null) {
            return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }else{
            return "";
        }
    }
    /**
     *
     * @return a string representation that gives the workout and discription
     */
    @Override
    public String toString(){
            return getDateStr() + "\n" + description;
    }


    /**
     *
     * @param o workout object to compare to the actual parameter
     * @return int which is 0 is the dates are equal, negative or positive if one date is before or after the other
     */
    @Override
    public int compareTo(Workout o) {
        return this.date.compareTo(o.date);
    }
}
