package com.example.fitnesstrackertest;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.view.View;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Jared Haeme
 * Date: 4/11/2019
 * The first page of the app that displays a list view of the list of workouts and allows the user to add a workout. Also connects to firebase to store workouts
 */
public class MainActivity extends AppCompatActivity {
    //Data fields
    private ListView lvWorkout;
    private Button btnAddWorkout1;
    private ArrayList<Workout> workouts;
    private ArrayAdapter arrayAdapter;
    public static final int CREATE_WORKOUT_ID = 1;
    public static final int EDIT_WORKOUT_ID = 2;
    private DatabaseReference database;


    /**
     * Initializes fields
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database=FirebaseDatabase.getInstance().getReference("current workouts");
        lvWorkout = (ListView) findViewById(R.id.workoutListView);
        btnAddWorkout1 = (Button) findViewById(R.id.btnAddWorkout);
        workouts = new ArrayList<Workout>();
        databaseListener();

    }

    /**
     * Action for when user chooses the add workout button that opens a new page for creating a new workout
     * @param view the button for adding the workout
     */
    public void addWorkout(View view) {
        Intent intent = new Intent(this, LiftsPage.class);
        intent.putExtra("choosenWorkout",new Workout(findMaxid()+1, LocalDate.now(), "","", new ArrayList<Lift>()));
        startActivityForResult(intent, CREATE_WORKOUT_ID);

    }

    /**
     * For when returning back to this page from another activity
     * @param requestCode int the id for the activity
     * @param resultCode int the result of the activity
     * @param data Intent the data retunred from the activity
     */
    //https://stackoverflow.com/questions/920306/sending-data-back-to-the-main-activity-in-android
    //https://developer.android.com/training/basics/intents/result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //When retunring back from creating a new activity
        if (requestCode == CREATE_WORKOUT_ID) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Workout newWorkout = (Workout) data.getSerializableExtra("workout");
                if(newWorkout.getDate()!=null) {
                    workouts.add(newWorkout);
                    addToFirebase(newWorkout, database);

                }

            }
            //From returning back from editing an old activity
        } else if(requestCode==EDIT_WORKOUT_ID){
            if (resultCode == RESULT_OK) {
                Workout workout = (Workout) data.getSerializableExtra("workout");
                //delete an activity
                if(workout.getId()<0) {
                    for (int i = workouts.size()-1; i>=0; i--){
                        if (Math.abs(workout.getId())== Math.abs(workouts.get(i).getId())){
                            workouts.remove(i);
                            DatabaseReference newRef=database.getParent().child("deleted workouts");
                            addToFirebase(workout, newRef);
                            database.child("workout"+Math.abs(workout.getId())).setValue(null);

                        }
                    }
                    //editing an activity
                }else{
                    for (int i = workouts.size()-1; i>=0; i--){
                        if (workout.getId()== workouts.get(i).getId()){
                            workouts.remove(i);
                            workouts.add(i,workout);
                            database.child("workout"+Math.abs(workout.getId())).setValue(null);
                            addToFirebase(workout, database);
                        }
                    }
                }

            }
        }
        //update listview
        arrayAdapter.notifyDataSetChanged();
    }

    /**
     * find the max id in the list of workouts
     * @return int a unique max id
     */

    public int findMaxid(){
        int max=0;
        for (Workout workout: workouts){
            if(workout.getId()>max){
                max=workout.getId();
            }
        }
        return max;
    }

    /**
     * Get workout data from firebase
     */
    public void databaseListener(){
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            //Gets data from firebase
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshotWorkout: dataSnapshot.getChildren()){

                    String description=snapshotWorkout.child("description").getValue().toString();
                    String date=snapshotWorkout.child("date").getValue().toString();
                    int id=Integer.parseInt(snapshotWorkout.child("id").getValue().toString());
                    String notes=snapshotWorkout.child("notes").getValue().toString();
                    ArrayList<Lift> lifts=new ArrayList<Lift>();
                    for(DataSnapshot snapshotLifts: snapshotWorkout.child("lifts").getChildren()){
                        String liftName=snapshotLifts.child("liftName").getValue().toString();
                        int liftId=Integer.parseInt(snapshotLifts.child("id").getValue().toString());
                        String liftNotes=snapshotLifts.child("notes").getValue().toString();
                        ArrayList<com.example.fitnesstrackertest.Set> sets=new ArrayList<com.example.fitnesstrackertest.Set>();
                        for(DataSnapshot snapshotSets: snapshotLifts.child("sets").getChildren()){
                            int setId=Integer.parseInt(snapshotSets.child("id").getValue().toString());
                            int reps=Integer.parseInt(snapshotSets.child("reps").getValue().toString());
                            int weight=Integer.parseInt(snapshotSets.child("weight").getValue().toString());
                            sets.add(new com.example.fitnesstrackertest.Set(setId, weight, reps));
                        }
                        lifts.add(new Lift(liftId, liftName, liftNotes, sets));
                    }

                    workouts.add(new Workout(id, LocalDate.parse(date), description, notes, lifts));
                }
                arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_expandable_list_item_1, workouts);
                lvWorkout.setAdapter(arrayAdapter);
                //gets selected item from listview and starts a new activity for editing that item
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //https://firebase.google.com/docs/database/android/read-and-write

    /**
     * Add data to firebase
     * @param workout adds a workout to firebase
     * @param databaseRef the appropriate data reference
     */
    public void addToFirebase(Workout workout, DatabaseReference databaseRef){
        database.getParent().child("descriptions").child(workout.getDescription().toLowerCase()).setValue(0);
        databaseRef.child("workout"+workout.getId()).child("date").setValue(workout.getFirebaseDateStr());
        databaseRef.child("workout"+workout.getId()).child("description").setValue(workout.getDescription());
        databaseRef.child("workout"+workout.getId()).child("notes").setValue(workout.getNotes());
        databaseRef.child("workout"+workout.getId()).child("id").setValue(workout.getId());
        for(Lift lift: workout.getLifts()){
            databaseRef.child("workout"+workout.getId()).child("lifts").child("lift"+lift.getId()).child("id").setValue(lift.getId());
            databaseRef.child("workout"+workout.getId()).child("lifts").child("lift"+lift.getId()).child("liftName").setValue(lift.getLiftName());
            databaseRef.child("workout"+workout.getId()).child("lifts").child("lift"+lift.getId()).child("notes").setValue(lift.getNotes());
            for(com.example.fitnesstrackertest.Set set: lift.getSets()){
                databaseRef.child("workout"+workout.getId()).child("lifts").child("lift"+lift.getId()).child("sets").child("set"+set.getId()).child("id").setValue(set.getId());
                databaseRef.child("workout"+workout.getId()).child("lifts").child("lift"+lift.getId()).child("sets").child("set"+set.getId()).child("reps").setValue(set.getReps());
                databaseRef.child("workout"+workout.getId()).child("lifts").child("lift"+lift.getId()).child("sets").child("set"+set.getId()).child("weight").setValue(set.getWeight());
            }
        }
    }
}




