package com.example.dell.smartgarden;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;


public class Activity_Details extends AppCompatActivity {
    TextView name,description,type;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar =  findViewById(R.id.toolbar);
        if (Activity_GlobalVariable.SELECTTYPE.equals("1")){
            getSupportActionBar().setTitle("Plant Details");
        }else {
            getSupportActionBar().setTitle("Vegetable Details");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name=(TextView)findViewById(R.id.tv_namedetail);
        description=(TextView)findViewById(R.id.tv_descriptiondetail);
        type=(TextView)findViewById(R.id.tv_typedetail);

        name.setText(Activity_GlobalVariable.NAME);
        description.setText(Activity_GlobalVariable.DESCRIPTION);
        type.setText(Activity_GlobalVariable.TYPE);
    }
}
