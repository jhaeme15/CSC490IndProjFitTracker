package com.example.fitnesstrackertest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;


/**
 * Author: Jared Haeme
 * Date: 4/11/2019
 * Lifts page for displaying the lifts in a workout and allows for adding, and deleting changes
 */

public class LiftsPage extends AppCompatActivity {
    //Data fields
    private ArrayList<Lift> lifts;
    private ArrayAdapter arrayAdapter;
    private ListView lsLifts;
    private ListView lvCopy;
    private EditText txtDate;
    private AutoCompleteTextView txtDescription;
    private EditText txtNotes;
    private  Workout workout;
    private DatabaseReference database;
    private ArrayAdapter<String> autoFillAdapter;
    private java.util.Set<String> descriptions;
//Id for activities
    public static final int CREATE_LIFT_ID = 100;
    public static final int EDIT_LIFT_ID =200;

    /**
     * Intializes data fields and has listener for an item selected in the list view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifts_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lvCopy=findViewById(R.id.lvCopy);
    workout = (Workout) getIntent().getSerializableExtra("choosenWorkout");
        database= FirebaseDatabase.getInstance().getReference("current workouts");
        lsLifts=(ListView) findViewById(R.id.liftsListView);
        txtDate=(EditText) findViewById(R.id.txtDateLiftsPage);
        txtDescription=(AutoCompleteTextView) findViewById(R.id.txtDescriptionLiftsPage);
        txtNotes=(EditText) findViewById(R.id.txtNotesLiftsPage);
        getDescriptionHints();
        if(workout!=null) {
            txtDate.setText(workout.getDateStr());
            txtDescription.setText(workout.getDescription());
            txtNotes.setText(workout.getNotes());

            lifts = new ArrayList<Lift>(workout.getLifts());

            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, lifts);
            lsLifts.setAdapter(arrayAdapter);
            //Opens new sets page activity when item selected in list view
            lsLifts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView adapter, View v, int position, long arg3) {
                    Lift choosenLift = lifts.get(position);
                    Intent intent = new Intent(LiftsPage.this, SetsPage.class);
                    intent.putExtra("choosenLift", choosenLift);
                    startActivityForResult(intent, EDIT_LIFT_ID);
                }
            });
        }
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
                if(lvCopy.isShown()){
                    lvCopy.setVisibility(View.GONE);
                }else {
                    saveLift();
                }
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

    /**
     * Happens when add lift button clicked. Starts activity which opens sets page
     * @param view
     */
    public void addLift(View view) {
        Intent intent = new Intent(this, SetsPage.class);
        intent.putExtra("choosenLift",new Lift(findMaxid()+1, "","", new ArrayList<Set>()));
        startActivityForResult(intent, CREATE_LIFT_ID);
    }

    /**
     * When the user clicks the delete workout button and deletes a workout
     * @param view
     */
    public void delLift(View view){
        //https://stackoverflow.com/questions/36747369/how-to-show-a-pop-up-in-android-studio-to-confirm-an-order
        //opens pop up to confirm
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this workout?");
        builder.setPositiveButton("Confirm Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        workout.setId(workout.getId()*-1);
                        Intent intent=new Intent(LiftsPage.this, MainActivity.class);
                        intent.putExtra("workout", workout);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    /**
     * Saves and adds workout to workouts list
     *
     */
    public void saveLift(){
        boolean valid=true;
        String description=txtDescription.getText().toString();
        String date=txtDate.getText().toString();
        String notes=txtNotes.getText().toString();
        String[] dateSplit=new String[0];
        if(date.indexOf("/")>0) {
            dateSplit = date.split("/");
        }else if(date.indexOf("-")>0){
            dateSplit=date.split("-");
        }else{
            valid=false;
        }
           workout.setDescription(description);
        workout.setNotes(notes);
        if(dateSplit.length==3){
            if(tryParseInt(dateSplit[0])&&tryParseInt(dateSplit[1])&&tryParseInt(dateSplit[2])) {
                int month=Integer.parseInt(dateSplit[0]);
                int day=Integer.parseInt(dateSplit[1]);
                int year=Integer.parseInt(dateSplit[2]);
                if (isValidDate(month,day,year)) {
                    workout.setDate(LocalDate.of(year,month,day));
                }else{
                    valid=false;
                }
            }else {
                valid=false;
            }
        }else {
            valid=false;
        }


        if(valid){
            workout.setLifts(lifts);
            Intent intent = new Intent(LiftsPage.this, MainActivity.class);
            intent.putExtra("workout", workout);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(LiftsPage.this);
            builder.setCancelable(true);
            builder.setTitle("Invalid Entry");
            builder.setMessage("Entry is invalid. If you continue your changes will not be saved");
            builder.setPositiveButton("Continue",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    //https://stackoverflow.com/questions/920306/sending-data-back-to-the-main-activity-in-android
    //https://developer.android.com/training/basics/intents/result

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        DatabaseReference newRef=database.child("workout"+workout.getId()).child("lifts");
        if (requestCode == CREATE_LIFT_ID) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Lift newLift = (Lift) data.getSerializableExtra("lift");
                if(newLift.getLiftName()!=null) {

                    addToFirebase(newLift, newRef);
                    lifts.add(newLift);
                }

            }
        }
        else if(requestCode==EDIT_LIFT_ID){
            if (resultCode == RESULT_OK) {
                Lift lift = (Lift) data.getSerializableExtra("lift");
                if(lift.getId()<0) {
                  for (int i=lifts.size()-1; i>=0; i--){
                      if (Math.abs(lift.getId())==Math.abs(lifts.get(i).getId())){
                          newRef.child("lift"+lift.getId()).setValue(null);
                          lifts.remove(i);
                      }
                  }
                }else{
                    for (int i=lifts.size()-1; i>=0; i--){
                        if (lift.getId()==lifts.get(i).getId()){
                            lifts.remove(i);
                            newRef.child("lift"+lift.getId()).setValue(null);
                            addToFirebase(lift, newRef);
                            lifts.add(i,lift);
                        }
                    }
                }

            }
        }
        arrayAdapter.notifyDataSetChanged();
    }
//from death calculator

    /**
     * Checks if date is valid
     * @param month int month of date
     * @param day int day of month
     * @param year int year of date
     * @return boolean if date is valid
     */
    public boolean isValidDate(int month, int day, int year){

        if (year>=0&& month>=1 && month<=12){
            // got code from https://stackoverflow.com/questions/1021324/java-code-for-calculating-leap-year/1021373#1021373
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);

            if((month==1 || month==3 || month==5 || month==7 || month==8 || month==10 || month==12) && (day>=1 && day<=31)){
                return true;
            }else if((month==4 || month==6 || month==9 || month==11) && (day>=1 && day<=30)){
                return true;
            }else if((month==2 && day>=1&& day<=28) || (month==2 && cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365 && day>=1 && day<=29) ){
                return true;
            }
        }
        return false;
    }
    /**
     * checks if a num can be converted to an int
     * @param value a string
     * @return
     */
    // got from https://stackoverflow.com/questions/8391979/does-java-have-a-int-tryparse-that-doesnt-throw-an-exception-for-bad-data

    /**
     * Checks if string value is an int
     * @param value string
     * @return boolen if string can be converted
     */
    public boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Finds the max id
     * @return int the max id
     */
    public int findMaxid(){
        int max=0;
        for (Lift lift: lifts){
            if(lift.getId()>max){
                max=lift.getId();
            }
        }
        return max;
    }

    //https://stackoverflow.com/questions/45729852/android-check-if-back-button-was-pressed?rq=1
    @Override
    public void onBackPressed() {
        if(lvCopy.isShown()){
            lvCopy.setVisibility(View.GONE);
        }else {
            saveLift();
        }
    }

    public void copyBtn(View view){
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            //Gets data from firebase
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<Workout> workouts=new ArrayList<Workout>();
                for(DataSnapshot snapshotWorkout: dataSnapshot.getChildren()){

                    String description=snapshotWorkout.child("description").getValue().toString();
                    String date=snapshotWorkout.child("date").getValue().toString();
                    int id=Integer.parseInt(snapshotWorkout.child("id").getValue().toString());
                    String notes=snapshotWorkout.child("notes").getValue().toString();
                    ArrayList<Lift> loadedLifts=new ArrayList<Lift>();
                    for(DataSnapshot snapshotLifts: snapshotWorkout.child("lifts").getChildren()){
                        String liftName=snapshotLifts.child("liftName").getValue().toString();
                        int liftId=Integer.parseInt(snapshotLifts.child("id").getValue().toString());
                        String liftNotes=snapshotLifts.child("notes").getValue().toString();
                        ArrayList<Set> sets=new ArrayList<Set>();
                        for(DataSnapshot snapshotSets: snapshotLifts.child("sets").getChildren()){
                            int setId=Integer.parseInt(snapshotSets.child("id").getValue().toString());
                            int reps=Integer.parseInt(snapshotSets.child("reps").getValue().toString());
                            int weight=Integer.parseInt(snapshotSets.child("weight").getValue().toString());
                            sets.add(new Set(setId, weight, reps));
                        }
                        loadedLifts.add(new Lift(liftId, liftName, liftNotes, sets));
                    }

                    workouts.add(new Workout(id, LocalDate.parse(date), description, notes, loadedLifts));
                }

                ArrayAdapter arrayAdapterCopy = new ArrayAdapter(LiftsPage.this, android.R.layout.simple_expandable_list_item_1,  workouts);
                lvCopy.setAdapter(arrayAdapterCopy);
                lvCopy.setVisibility(View.VISIBLE);
                lvCopy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView adapter, View v, int position, long arg3) {
                        Workout choosenWorkout = workouts.get(position);
                        workout.setLifts(choosenWorkout.getLifts());
                        lifts=choosenWorkout.getLifts();
                        arrayAdapter.addAll(lifts);
                        arrayAdapter.notifyDataSetChanged();
                        lvCopy.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void addToFirebase(Lift lift, DatabaseReference databaseRef){
            database.getParent().child("lifts").child(lift.getLiftName().toLowerCase()).setValue(0);
            databaseRef.child("lift"+lift.getId()).child("id").setValue(lift.getId());
            databaseRef.child("lift"+lift.getId()).child("liftName").setValue(lift.getLiftName());
            databaseRef.child("lift"+lift.getId()).child("notes").setValue(lift.getNotes());
            for(Set set: lift.getSets()){
                databaseRef.child("lift"+lift.getId()).child("sets").child("set"+set.getId()).child("id").setValue(set.getId());
                databaseRef.child("lift"+lift.getId()).child("sets").child("set"+set.getId()).child("reps").setValue(set.getReps());
                databaseRef.child("lift"+lift.getId()).child("sets").child("set"+set.getId()).child("weight").setValue(set.getWeight());
            }
    }

    public void getDescriptionHints(){
        descriptions=new HashSet<String>();
        database.getParent().child("descriptions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    descriptions.add(snapshot.getKey().substring(0,1).toUpperCase()+snapshot.getKey().substring(1).toLowerCase());

                }
                autoFillAdapter=new ArrayAdapter<String>(LiftsPage.this, android.R.layout.simple_list_item_1, descriptions.toArray(new String[0]));
                txtDescription.setAdapter(autoFillAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
