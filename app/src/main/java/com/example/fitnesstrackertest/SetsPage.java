package com.example.fitnesstrackertest;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SetsPage extends AppCompatActivity {
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets_page);
        Lift lift = (Lift) getIntent().getSerializableExtra("choosenLift");
        linearLayout=(LinearLayout) findViewById(R.id.lin_layout_setsPage);
        RelativeLayout relativeLayout=new RelativeLayout(this);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams edittxtWeightParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,5,0,0);
        relativeLayout.setLayoutParams(params);
        RelativeLayout.LayoutParams edittxtRepParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lblxParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lblSetsParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditText editTextWeight=new EditText(this);
        editTextWeight.setId(View.generateViewId());

        EditText editTextRep=new EditText(this);
        editTextRep.setId(View.generateViewId());
        TextView xdivider=new TextView(this);
        xdivider.setId(View.generateViewId());
        TextView setNumlbl=new TextView(this);
        setNumlbl.setId(View.generateViewId());

        editTextWeight.setHint("Weight");
        editTextWeight.setHeight(125);
        editTextWeight.setWidth(350);
        editTextRep.setWidth(350);
        editTextRep.setHeight(125);
        setNumlbl.setTextSize(20);

        Typeface bold=Typeface.defaultFromStyle(Typeface.BOLD);
        setNumlbl.setTypeface(bold);
        editTextRep.setHint("Rep");
        xdivider.setText("X");
        setNumlbl.setText("Set "+1);

        edittxtWeightParams.addRule(RelativeLayout.BELOW, setNumlbl.getId());
        edittxtWeightParams.addRule(RelativeLayout.LEFT_OF, xdivider.getId());
        lblSetsParams.setMargins(10, 10, 10, 5);
        edittxtWeightParams.setMargins(10, 0, 10, 10);
        edittxtRepParams.addRule(RelativeLayout.BELOW, setNumlbl.getId());
        edittxtRepParams.addRule(RelativeLayout.RIGHT_OF, xdivider.getId());
        edittxtRepParams.setMargins(10, 0, 10, 10);
        lblxParams.setMargins(10, 0, 10, 10);
        lblxParams.addRule(RelativeLayout.BELOW, setNumlbl.getId());
        lblxParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lblxParams.addRule(RelativeLayout.ALIGN_BASELINE, editTextRep.getId());
        editTextWeight.setLayoutParams(edittxtWeightParams);
        editTextRep.setLayoutParams(edittxtRepParams);
        setNumlbl.setLayoutParams(lblSetsParams);
        xdivider.setLayoutParams(lblxParams);
        relativeLayout.addView(editTextWeight, edittxtWeightParams);
        relativeLayout.addView(editTextRep, edittxtRepParams);
        relativeLayout.addView(xdivider, lblxParams);
        relativeLayout.addView(setNumlbl, lblSetsParams);

        View divider=new View(this);
        divider.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1));
        divider.setBackgroundColor(Color.parseColor("#B3B3B3"));

        linearLayout.addView(relativeLayout, params);
        linearLayout.addView(divider);

    }
}
