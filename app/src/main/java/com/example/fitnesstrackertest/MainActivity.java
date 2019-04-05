package com.example.fitnesstrackertest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.view.View;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView lvWorkout;
    private Button btnAddWorkout1;
    private ArrayList<Workout> workouts;
    private ArrayAdapter arrayAdapter;
    public static final int CREATE_WORKOUT_ID = 1;
    public static final int EDIT_WORKOUT_ID = 2;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvWorkout = (ListView) findViewById(R.id.workoutListView);
        btnAddWorkout1 = (Button) findViewById(R.id.btnAddWorkout);
        workouts = new ArrayList<Workout>();
        ArrayList<Lift> testLift=new ArrayList<Lift>();
        ArrayList<Set> setTest=new ArrayList<Set>();
        setTest.add(new Set(1,165, 10));
        setTest.add(new Set(2,175, 10));
        setTest.add(new Set(3,185, 10));
        testLift.add(new Lift(1,"Bench","", setTest));
        testLift.add(new Lift(2, "Squat","", setTest));
        testLift.add(new Lift(3,"Deadlift","", setTest));
        workouts.add(new Workout(1,LocalDate.now(), "test2","", testLift));
        workouts.add(new Workout(2,LocalDate.now(), "test3","", testLift));
        workouts.add(new Workout(3,LocalDate.now(), "test4","", testLift));
        database = FirebaseDatabase.getInstance().getReference();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, workouts);
        lvWorkout.setAdapter(arrayAdapter);
        // https:stackoverflow.com/questions/18405299/onitemclicklistener-using-arrayadapter-for-listview
        lvWorkout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapter, View v, int position, long arg3) {
                Workout choosenWorkout = workouts.get(position);
                Intent intent = new Intent(MainActivity.this, LiftsPage.class);
                intent.putExtra("choosenWorkout",choosenWorkout);
                startActivityForResult(intent, EDIT_WORKOUT_ID);
            }
        });

    }

    public void addWorkout(View view) {
        Intent intent = new Intent(this, LiftsPage.class);
        intent.putExtra("choosenWorkout",new Workout(findMaxid()+1, LocalDate.now(), "","", new ArrayList<Lift>()));
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
                if(newWorkout.getDate()!=null) {
                    workouts.add(newWorkout);
                }

            }
        } else if(requestCode==EDIT_WORKOUT_ID){
            if (resultCode == RESULT_OK) {
                Workout workout = (Workout) data.getSerializableExtra("workout");
                if(workout.getDate()==null) {
                    for (int i = workouts.size()-1; i>=0; i--){
                        if (workout.getId()== workouts.get(i).getId()){
                            workouts.remove(i);
                        }
                    }
                }else{
                    for (int i = workouts.size()-1; i>=0; i--){
                        if (workout.getId()== workouts.get(i).getId()){
                            workouts.remove(i);
                            workouts.add(i,workout);
                        }
                    }
                }

            }
        }
        arrayAdapter.notifyDataSetChanged();
    }
    public int findMaxid(){
        int max=0;
        for (Workout workout: workouts){
            if(workout.getId()>max){
                max=workout.getId();
            }
        }
        return max;
    }
}




