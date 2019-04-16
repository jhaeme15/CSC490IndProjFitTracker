package com.example.fitnesstrackertest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author: Jared Haeme
 * Date: 4/15/2019
 * Displays stats of from workouts including max and total improvment
 */
public class StatsPage extends AppCompatActivity {
    //Data fields
private ArrayList<Workout> workouts;
private TextView txtStats;
private Map<String, ArrayList<Set>> lifts;
private Map<String, int[]> minMaxWeightlifts;
private Map<String, Integer> maxReps;

    /**
     * Intialize data fields
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_page);
        workouts = (ArrayList<Workout>) getIntent().getSerializableExtra("workouts");
        minMaxWeightlifts=new TreeMap<String, int[]>();
        maxReps=new TreeMap<String, Integer>();
        lifts=new HashMap<String, ArrayList<Set>>();
        findLifts();
        findMaxMin();
        txtStats=(TextView) findViewById(R.id.txtStats);
        addStatsToView();
    }

    /**
     * For displaying all the stats
     */
    public void addStatsToView(){
        String stats="Total Workouts: "+workouts.size()+"\n"+"\n";
        for(String liftName: minMaxWeightlifts.keySet()){
            int[] minMax=minMaxWeightlifts.get(liftName);
            if(minMax[1]>0){
                stats += capitalizeString(liftName)+ "\n Max: " + minMax[1] + "lbs    Improved by: " + (minMax[1] - minMax[0]) + "lbs\n\n";
            }else if(maxReps.get(liftName)>0){
                stats+=capitalizeString(liftName) + "\n Max: " + maxReps.get(liftName) + " reps\n\n";
            }
        }
        txtStats.setText(stats);

    }

    /**
     * Gets all the sets for each lift and puts them in a map
     */
    public void findLifts(){
        for(Workout workout: workouts){
            for(Lift lift: workout.getLifts()){
                ArrayList<Set> sets=new ArrayList<Set>();
                for(Set set: lift.getSets()){
                    sets.add(set);
                }
                String liftName=lift.getLiftName().trim().toLowerCase();
                if(lifts.containsKey(liftName)){
                    sets.addAll(lifts.get(liftName));
                }
                lifts.put(liftName, sets);
            }
        }
    }

    /**
     * Finds the min and max weight and the max reps
     */
    public void findMaxMin(){
        for(String liftName: lifts.keySet()){
            int max=0;
            int min=Integer.MAX_VALUE;
            int maxRep=0;
            for(Set set: lifts.get(liftName)){
                if(set.getWeight()>max){
                    max=set.getWeight();
                }
                if(set.getWeight()<min){
                    min=set.getWeight();
                }
                if(set.getReps()>maxRep){
                    maxRep=set.getReps();
                }
            }
            if (min==Integer.MAX_VALUE){
                min=0;
            }
            int[] minMax={min, max};
            minMaxWeightlifts.put(liftName, minMax);
            maxReps.put(liftName, maxRep);
        }
    }
    //https://stackoverflow.com/questions/1892765/how-to-capitalize-the-first-character-of-each-word-in-a-string
    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') {
                found = false;
            }
        }
        return String.valueOf(chars);
    }
    /**
     * Code for back button
     * @param item MenuItem
     * @return boolean
     */
    //https://stackoverflow.com/questions/14545139/android-back-button-in-the-title-bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * For back button
     * @param menu MenuItem
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
