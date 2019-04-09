package com.example.fitnesstrackertest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;


public class LiftsPage extends AppCompatActivity {
    private ArrayList<Lift> lifts;
    private ArrayAdapter arrayAdapter;
    private ListView lsLifts;
    private EditText txtDate;
    private EditText txtDescription;
    private EditText txtNotes;
    private  Workout workout;

    public static final int CREATE_LIFT_ID = 100;
    public static final int EDIT_LIFT_ID =200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifts_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    workout = (Workout) getIntent().getSerializableExtra("choosenWorkout");
        lsLifts=(ListView) findViewById(R.id.liftsListView);
        txtDate=(EditText) findViewById(R.id.txtDateLiftsPage);
        txtDescription=(EditText) findViewById(R.id.txtDescriptionLiftsPage);
        txtNotes=(EditText) findViewById(R.id.txtNotesLiftsPage);
        if(workout!=null) {
            txtDate.setText(workout.getDateStr());
            txtDescription.setText(workout.getDescription());
            txtNotes.setText(workout.getNotes());

            lifts = new ArrayList<Lift>(workout.getLifts());

            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, lifts);
            lsLifts.setAdapter(arrayAdapter);
            lsLifts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView adapter, View v, int position, long arg3) {
                    Lift choosenLift = lifts.get(position);
                    Intent intent = new Intent(LiftsPage.this, SetsPage.class);
                    // Use TaskStackBuilder to build the back stack and get the PendingIntent
                    PendingIntent pendingIntent =
                            TaskStackBuilder.create(LiftsPage.this)
                                    // add all of DetailsActivity's parents to the stack,
                                    // followed by DetailsActivity itself
                                    .addNextIntentWithParentStack(intent)
                                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(LiftsPage.this);
                    builder.setContentIntent(pendingIntent);
                    intent.putExtra("choosenLift", choosenLift);
                    startActivityForResult(intent, EDIT_LIFT_ID);
                }
            });
        }
    }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    public void addLift(View view) {
        Intent intent = new Intent(this, SetsPage.class);
        intent.putExtra("choosenLift",new Lift(findMaxid()+1, "","", new ArrayList<Set>()));
        startActivityForResult(intent, CREATE_LIFT_ID);
    }

    public void delLift(View view){
        //https://stackoverflow.com/questions/36747369/how-to-show-a-pop-up-in-android-studio-to-confirm-an-order
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

    public void saveLift(View view){
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
        if(description!=null){
           workout.setDescription(description);

        }else{
            valid=false;
        }
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
        }
    }

    //https://stackoverflow.com/questions/920306/sending-data-back-to-the-main-activity-in-android
    //https://developer.android.com/training/basics/intents/result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_LIFT_ID) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Lift newLift = (Lift) data.getSerializableExtra("lift");
                if(newLift.getLiftName()!=null) {
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
                          lifts.remove(i);
                      }
                  }
                }else{
                    for (int i=lifts.size()-1; i>=0; i--){
                        if (lift.getId()==lifts.get(i).getId()){
                            lifts.remove(i);
                            lifts.add(i,lift);
                        }
                    }
                }

            }
        }
        arrayAdapter.notifyDataSetChanged();
    }
//from death calculator
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
    public boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public int findMaxid(){
        int max=0;
        for (Lift lift: lifts){
            if(lift.getId()>max){
                max=lift.getId();
            }
        }
        return max;
    }

}
