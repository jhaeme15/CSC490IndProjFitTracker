package com.example.fitnesstrackertest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SetsPage extends AppCompatActivity {
    private LinearLayout linearLayout;
    private Button btnAddSet;
    private Button btnDeleteLift;
    private RelativeLayout btnLayout;
    private EditText txtLiftType;
    private EditText txtNotes;
    private ArrayList<Integer> weightsIds;
    private ArrayList<Integer> repsids;
    private ArrayList<RelativeLayout> setsViewList;
    private Lift lift;
    private int setNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lift = (Lift) getIntent().getSerializableExtra("choosenLift");
        txtLiftType =(EditText) findViewById(R.id.editTxtSetsPageLiftType) ;
        txtNotes=(EditText) findViewById(R.id.txtNotesSetsPage);
        setNum=1;
        setsViewList=new ArrayList<RelativeLayout>();
        weightsIds=new ArrayList<Integer>();
        repsids=new ArrayList<Integer>();
        linearLayout=(LinearLayout) findViewById(R.id.lin_layout_setsPage);
            setNum=lift.getSets().size()+1;
            txtLiftType.setText(lift.getLiftName());
            txtNotes.setText(lift.getNotes());
            ArrayList<Set> sets=lift.getSets();

            for (int i=0; i<sets.size(); i++){
                createSetUI(linearLayout, i+1, sets.get(i));
            }
            createButtonUI(linearLayout);

    }

    public void createSetUI(final LinearLayout linearLayout, final int setNum, Set set){
        final RelativeLayout relativeLayout=new RelativeLayout(this);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams edittxtWeightParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,5,0,0);
        relativeLayout.setLayoutParams(params);
        RelativeLayout.LayoutParams edittxtRepParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lblxParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lblSetsParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams btndelParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        EditText editTextWeight=new EditText(this);
        editTextWeight.setId(View.generateViewId());
        weightsIds.add(editTextWeight.getId());
        editTextWeight.setInputType(InputType.TYPE_CLASS_NUMBER);

       EditText editTextRep=new EditText(this);
        editTextRep.setId(View.generateViewId());
        editTextRep.setInputType(InputType.TYPE_CLASS_NUMBER);

        repsids.add(editTextRep.getId());
       TextView xdivider=new TextView(this);
        xdivider.setId(View.generateViewId());
         TextView setNumlbl=new TextView(this);
        setNumlbl.setId(View.generateViewId());
        Button btnDelSet=new Button(this);
        btnDelSet.setId(View.generateViewId());
        btnDelSet.setText("x");
        btnDelSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { delSet(v); }});
        btnDelSet.setTextColor(Color.RED);
        btnDelSet.setHeight(80);
        btnDelSet.setWidth(80);
        btnDelSet.setBackgroundColor(Color.TRANSPARENT);
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
        setNumlbl.setText("Set "+setNum);

        edittxtWeightParams.addRule(RelativeLayout.BELOW, setNumlbl.getId());
        edittxtWeightParams.addRule(RelativeLayout.LEFT_OF, xdivider.getId());
        lblSetsParams.setMargins(10, 10, 10, 10);
        edittxtWeightParams.setMargins(10, 0, 10, 10);
        edittxtRepParams.addRule(RelativeLayout.BELOW, setNumlbl.getId());
        edittxtRepParams.addRule(RelativeLayout.RIGHT_OF, xdivider.getId());
        edittxtRepParams.setMargins(10, 0, 10, 10);
        lblxParams.setMargins(10, 0, 10, 10);
        btndelParams.setMargins(5, 5,10,10);
        btndelParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btndelParams.addRule(RelativeLayout.ALIGN_BASELINE, setNumlbl.getId());
        lblxParams.addRule(RelativeLayout.BELOW, setNumlbl.getId());
        lblxParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lblxParams.addRule(RelativeLayout.ALIGN_BASELINE, editTextRep.getId());
        editTextWeight.setLayoutParams(edittxtWeightParams);
        editTextRep.setLayoutParams(edittxtRepParams);
        setNumlbl.setLayoutParams(lblSetsParams);
        xdivider.setLayoutParams(lblxParams);
        btnDelSet.setLayoutParams(btndelParams);
        if (set!=null){
            editTextWeight.setText(set.getWeight()+"");
            editTextRep.setText(set.getReps()+"");
        }
        relativeLayout.addView(editTextWeight, edittxtWeightParams);
        relativeLayout.addView(editTextRep, edittxtRepParams);
        relativeLayout.addView(xdivider, lblxParams);
        relativeLayout.addView(setNumlbl, lblSetsParams);
        relativeLayout.addView(btnDelSet, btndelParams);

        View divider=new View(this);
        divider.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1));
        divider.setBackgroundColor(Color.parseColor("#B3B3B3"));
        linearLayout.addView(relativeLayout, params);
        linearLayout.addView(divider);
        setsViewList.add(relativeLayout);
    }

    public void createButtonUI(final LinearLayout linearLayout){
        btnAddSet =new Button(this);
        btnDeleteLift=new Button(this);
        btnAddSet.setText("Add");
        btnDeleteLift.setText("Delete");
        btnAddSet.setId(View.generateViewId());
        btnDeleteLift.setId(View.generateViewId());
        btnLayout=new RelativeLayout(this);
        final RelativeLayout.LayoutParams btnRelLayoutparams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        btnRelLayoutparams.setMargins(0,5,0,20);
        btnLayout.setLayoutParams(btnRelLayoutparams);
        RelativeLayout.LayoutParams btnAddSetParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams btnDeleteLiftParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        btnAddSetParams.setMargins(75, 0, 10, 0);
        btnDeleteLiftParams.setMargins(10, 0, 75, 0);
        btnAddSetParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        btnDeleteLiftParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnAddSet.setLayoutParams(btnAddSetParams);
        btnDeleteLift.setLayoutParams(btnDeleteLiftParams);

        btnLayout.addView(btnAddSet, btnAddSetParams);
        btnLayout.addView(btnDeleteLift, btnDeleteLiftParams);

        linearLayout.addView(btnLayout, btnRelLayoutparams);
        btnAddSet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                linearLayout.removeView(btnLayout);
                createSetUI(linearLayout,setNum , null);
                setNum++;
                linearLayout.addView(btnLayout, btnRelLayoutparams);
            }
        });

        btnDeleteLift.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //https://stackoverflow.com/questions/36747369/how-to-show-a-pop-up-in-android-studio-to-confirm-an-order
                AlertDialog.Builder builder = new AlertDialog.Builder(SetsPage.this);
                builder.setCancelable(true);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want to delete this lift?");
                builder.setPositiveButton("Confirm Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               lift.setId(lift.getId()*-1);
                                Intent intent=new Intent(SetsPage.this, LiftsPage.class);
                                intent.putExtra("lift", lift);
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
        });

    }


    //https://stackoverflow.com/questions/14545139/android-back-button-in-the-title-bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    //https://stackoverflow.com/questions/45729852/android-check-if-back-button-was-pressed?rq=1
    @Override
    public void onBackPressed() {
       saveData();
    }

    // got from https://stackoverflow.com/questions/8391979/does-java-have-a-int-tryparse-that-doesnt-throw-an-exception-for-bad-data
    public boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void delSet(View v){

            int id = v.getId();
            int count = 0;
            for (int i = setsViewList.size() - 1; i >= 0; i--) {
                Button btn = setsViewList.get(i).findViewById(id);
                if (btn != null) {

                    weightsIds.remove(i);
                    repsids.remove(i);
                    linearLayout.removeView(setsViewList.get(i));
                    setsViewList.remove(i);

                    count = i;
                    for (int j = count; j < setsViewList.size(); j++) {
                        TextView lbl = (TextView) setsViewList.get(j).getChildAt(3);
                        String setStr = lbl.getText().toString();
                        int num = Integer.parseInt(setStr.substring(setStr.length() - 1));
                        lbl.setText("Set " + (num - 1));

                    }
                    setNum--;
                }


            }
    }

    public void saveData(){
        boolean valid=true;
        ArrayList<Set> sets=new ArrayList<Set>();
        String liftName= txtLiftType.getText().toString();
        String notes=txtNotes.getText().toString();


        if(!liftName.matches("")){
            lift.setLiftName(liftName);
        }else{
            valid=false;
        }
        lift.setNotes(notes);
        for (int i=0; i<setsViewList.size(); i++){
            RelativeLayout relativeLayout=setsViewList.get(i);
            EditText txtWeights=relativeLayout.findViewById(weightsIds.get(i));
            EditText txtReps=relativeLayout.findViewById(repsids.get(i));
            String weightStr=txtWeights.getText().toString();
            String repsStr=txtReps.getText().toString();
            int weight=0;
            int reps=0;
            if (tryParseInt(weightStr)){
                weight=Integer.parseInt(weightStr);
            }else{


                valid=false;
            }
            if (tryParseInt(repsStr)){
                reps=Integer.parseInt(repsStr);
            }else{
                valid=false;
            }
            if(valid) {
                Set set=new Set(i+1, weight, reps);
                sets.add(set);
            }
        }
        if(valid) {
            lift.setSets(sets);
            Intent intent = new Intent(SetsPage.this, LiftsPage.class);
            intent.putExtra("lift", lift);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(SetsPage.this);
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


}
