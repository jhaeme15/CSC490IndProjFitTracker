package com.example.fitnesstrackertest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class LiftsPage extends AppCompatActivity {
    ArrayList<Lift> lifts;
    ArrayAdapter arrayAdapter;
    ListView lsLifts;
    TextView heading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifts_page);
        Workout workout = (Workout) getIntent().getSerializableExtra("choosenWorkout");
        lsLifts=(ListView) findViewById(R.id.liftsListView);
        heading=(TextView) findViewById(R.id.txtHeading);
        heading.setText(workout.toString());
        lifts=new ArrayList<Lift>(workout.getLifts());
        arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, lifts);
        lsLifts.setAdapter(arrayAdapter);
        lsLifts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapter, View v, int position, long arg3) {
                Lift choosenLift = lifts.get(position);
                Intent intent = new Intent(LiftsPage.this, SetsPage.class);
                intent.putExtra("choosenLift",choosenLift);
                startActivity(intent);
            }
        });
    }
}
