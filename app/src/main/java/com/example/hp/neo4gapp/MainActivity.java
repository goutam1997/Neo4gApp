package com.example.hp.neo4gapp;
//CHECKING GIT STASH
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button doctor = (Button) findViewById(R.id.doctor);
        Button pat_reg=(Button)findViewById(R.id.pat_reg);
        Button pat_log=(Button)findViewById(R.id.pat_log);

        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DoctorRegistration.class));
                finish();
            }
        });

        pat_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterPatient.class));
                finish();
            }
        });

        pat_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PatientLogin.class));
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.about:
                Toast.makeText(this, "Created by Goutam Kundu", Toast.LENGTH_LONG).show();
                return(true);
            case R.id.exit:
                finish();
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }
}
