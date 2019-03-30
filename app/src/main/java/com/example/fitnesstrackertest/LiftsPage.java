package com.example.fitnesstrackertest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class LiftsPage extends AppCompatActivity {
    private ArrayList<Lift> lifts;
    private ArrayAdapter arrayAdapter;
    private ListView lsLifts;
    private EditText txtDate;
    private EditText txtDescription;

    public static final int CREATE_LIFT_ID = 100;
    public static final int EDIT_LIFT_ID =200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifts_page);
        Workout workout = (Workout) getIntent().getSerializableExtra("choosenWorkout");
        lsLifts=(ListView) findViewById(R.id.liftsListView);
        txtDate=(EditText) findViewById(R.id.txtDateLiftsPage);
        txtDescription=(EditText) findViewById(R.id.txtDescriptionLiftsPage);
        txtDate.setText(workout.getDate().toString());
        txtDescription.setText(workout.getDescription());
        lifts=new ArrayList<Lift>(workout.getLifts());

        arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, lifts);
        lsLifts.setAdapter(arrayAdapter);
        lsLifts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapter, View v, int position, long arg3) {
                Lift choosenLift = lifts.get(position);
                Intent intent = new Intent(LiftsPage.this, SetsPage.class);
                intent.putExtra("choosenLift",choosenLift);
                startActivityForResult(intent, EDIT_LIFT_ID);
            }
        });
    }

    public void addLift(View view) {
        Intent intent = new Intent(this, SetsPage.class);
        intent.putExtra("choosenLift",new Lift(lifts.size()+1, "", new ArrayList<Set>()));
        startActivityForResult(intent, CREATE_LIFT_ID);
    }

    //https://stackoverflow.com/questions/920306/sending-data-back-to-the-main-activity-in-android
    //https://developer.android.com/training/basics/intents/result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_LIFT_ID) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Lift newLift = (Lift) data.getSerializableExtra("lift");
                if(newLift!=null) {
                    lifts.add(newLift);
                }
                arrayAdapter.notifyDataSetChanged();
            }
        }
        else if(requestCode==EDIT_LIFT_ID){
            if (resultCode == RESULT_OK) {
                Lift lift = (Lift) data.getSerializableExtra("lift");
                if(lift.getLift()==null) {
                  for (int i=lifts.size()-1; i>=0; i--){
                      if (lift.getId()==lifts.get(i).getId()){
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
                arrayAdapter.notifyDataSetChanged();
            }
        }
    }


}
