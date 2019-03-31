package com.example.fitnesstrackertest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.view.View;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ListView lsTest;
    private Button btnAddWorkout1;
    private ArrayList<Workout> test;
    private ArrayAdapter arrayAdapter;
    public static final int CREATE_WORKOUT_ID = 1;
    public static final int EDIT_WORKOUT_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lsTest = (ListView) findViewById(R.id.workoutListView);
        btnAddWorkout1 = (Button) findViewById(R.id.btnAddWorkout);
        test = new ArrayList<Workout>();
        ArrayList<Lift> testLift=new ArrayList<Lift>();
        ArrayList<Set> setTest=new ArrayList<Set>();
        setTest.add(new Set(1,165, 10));
        setTest.add(new Set(2,175, 10));
        setTest.add(new Set(3,185, 10));
        testLift.add(new Lift(1,"Bench", setTest));
        testLift.add(new Lift(2, "Squat", setTest));
        testLift.add(new Lift(3,"Deadlift", setTest));
        test.add(new Workout(1,LocalDate.now(), "test2", testLift));
        test.add(new Workout(2,LocalDate.now(), "test3", testLift));
        test.add(new Workout(3,LocalDate.now(), "test4", testLift));

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, test);
        lsTest.setAdapter(arrayAdapter);
        // https:stackoverflow.com/questions/18405299/onitemclicklistener-using-arrayadapter-for-listview
        lsTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapter, View v, int position, long arg3) {
                Workout choosenWorkout = test.get(position);
                Intent intent = new Intent(MainActivity.this, LiftsPage.class);
                intent.putExtra("choosenWorkout",choosenWorkout);
                startActivityForResult(intent, EDIT_WORKOUT_ID);
            }
        });

    }

    public void addWorkout(View view) {
        Intent intent = new Intent(this, LiftsPage.class);
        intent.putExtra("choosenWorkout",new Workout(test.size()+1, LocalDate.now(), "", new ArrayList<Lift>()));
        startActivityForResult(intent, CREATE_WORKOUT_ID);

    }

    //https://stackoverflow.com/questions/920306/sending-data-back-to-the-main-activity-in-android
    //https://developer.android.com/training/basics/intents/result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_WORKOUT_ID) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Workout newWorkout = (Workout) data.getSerializableExtra("workout");
                if(newWorkout!=null) {
                    test.add(newWorkout);
                }

            }
        } else if(requestCode==EDIT_WORKOUT_ID){
            if (resultCode == RESULT_OK) {
                Workout workout = (Workout) data.getSerializableExtra("workout");
                if(workout.getDate()==null) {
                    for (int i=test.size()-1; i>=0; i--){
                        if (workout.getId()==test.get(i).getId()){
                            test.remove(i);
                        }
                    }
                }else{
                    for (int i=test.size()-1; i>=0; i--){
                        if (workout.getId()==test.get(i).getId()){
                            test.remove(i);
                            test.add(i,workout);
                        }
                    }
                }

            }
        }
        arrayAdapter.notifyDataSetChanged();
    }
}




