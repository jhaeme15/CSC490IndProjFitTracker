package com.example.fitnesstrackertest;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class AddWorkout extends AppCompatActivity {
    private EditText txtDescription;
    private EditText txtDate;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);
        txtDescription=(EditText) findViewById(R.id.txtDescription);
        txtDate=(EditText) findViewById(R.id.txtDate);
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        txtDate.setText(dateFormat.format(date));
        id=(int) getIntent().getSerializableExtra("size");

    }

    public void createWorkout(View view){
        String dateStr=txtDate.getText().toString();
        String descriptionStr=txtDescription.getText().toString();
        if(dateStr!=null && dateStr.length()>=7 && txtDescription.getText()!=null){
            //int month=Integer.parseInt(dateStr.substring(0,3));
            //int day=Integer.parseInt(dateStr.substring(4,6));
            //int year=Integer.parseInt(dateStr.substring(7));
            LocalDate date= LocalDate.now();
            Workout workout=new Workout(id+1, date, descriptionStr);
            Intent intent=new Intent(this, MainActivity.class);
            intent.putExtra("workout", workout);
            setResult(Activity.RESULT_OK, intent);
            finish();
            //startActivity(intent);

        }

    }
}
