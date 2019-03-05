package com.example.hp.neo4gapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DoctorAdvise extends AppCompatActivity {
    private EditText medicine, food, test;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_advise);

        Bundle bundle = getIntent().getExtras();
        final String visitInfo = bundle.getString("visitInfo");
        
        medicine=(EditText)findViewById(R.id.editText2);
        food=(EditText)findViewById(R.id.editText3);
        test=(EditText)findViewById(R.id.editText4);
        submit=(Button)findViewById(R.id.button2);
        
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Medicine = medicine.getText().toString().trim();
                String Food = food.getText().toString().trim();
                String Test = test.getText().toString().trim();
                
                if (Medicine.isEmpty() || Food.isEmpty() || Test.isEmpty())
                    Toast.makeText(DoctorAdvise.this, "Please Fill Up All The Fields", Toast.LENGTH_SHORT).show();
                else {
                    MessageSender messageSender = new MessageSender();
                    messageSender.execute(visitInfo+":"+Medicine+":"+Food+":"+Test);

                    Toast.makeText(DoctorAdvise.this, "Thank You For Your Visit!!", Toast.LENGTH_SHORT).show();
                    
                    startActivity(new Intent(DoctorAdvise.this, MainActivity.class));
                    finish();
                }
            }
        });
    }
}
