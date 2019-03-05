package com.example.hp.neo4gapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DoctorRegistration extends AppCompatActivity {
    private FusedLocationProviderClient client;
    private EditText name, fees, email, password;
    private Spinner spinner;
    private String Specialisation, latitude, longitude;
    private Button register;
    private static final String[] item = {"ENT", "Child Specialist", "Gynecologist", "Orthopedic", "General Physician"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

        name = (EditText) findViewById(R.id.name);
        fees = (EditText) findViewById(R.id.fees);
        email = (EditText) findViewById(R.id.doc_reg_email);
        password = (EditText) findViewById(R.id.doc_reg_pass);
        register = (Button) findViewById(R.id.register);

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DoctorRegistration.this,
                android.R.layout.simple_spinner_item, item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Specialisation = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Name = name.getText().toString().trim();
                final String Fees = fees.getText().toString().trim();
                final String Email = email.getText().toString().trim();
                final String Password = password.getText().toString().trim();

                if (Name.isEmpty() || Fees.isEmpty() || Specialisation.isEmpty() || Email.isEmpty() || Password.isEmpty()) {
                    Toast.makeText(DoctorRegistration.this, "Please Enter All the Fields", Toast.LENGTH_SHORT).show();

                } else {
                    requestPermission();
                    client = LocationServices.getFusedLocationProviderClient(DoctorRegistration.this);

                    if (ActivityCompat.checkSelfPermission(DoctorRegistration.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DoctorRegistration.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(DoctorRegistration.this, "You have to Enable your Location", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    client.getLastLocation().addOnSuccessListener(DoctorRegistration.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                String myLoc = location.toString();
                                String my[]=myLoc.split(" ");
                                String po[]=my[1].split(",");
                                latitude=po[0];     longitude=po[1];
//                                Log.d("Latitude",latitude);
//                                Log.d("Longitude", longitude);

                                Toast.makeText(DoctorRegistration.this, latitude + " : " + longitude, Toast.LENGTH_SHORT).show();
                                String DoctorInfo = "Doctor:" + Name + ":" + Fees + ":" + Specialisation + ":" + Email + ":" + Password + ":" + latitude + ":" + longitude;
                                MessageSender messageSender = new MessageSender();
                                messageSender.execute(DoctorInfo);

                                Thread myThread = new Thread(new MyServerThread());
                                myThread.start();
                            }
                        }
                    });
                }
            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {ACCESS_FINE_LOCATION}, 1);
    }

    class MyServerThread implements Runnable {
        Socket s;
        ServerSocket ss;
        InputStreamReader isr;
        BufferedReader br;
        String message;
        Handler handler = new Handler();

        @Override
        public void run() {
            try {
                ss=new ServerSocket(7801);
                while(true) {
                    s=ss.accept();

                    isr = new InputStreamReader(s.getInputStream());
                    br = new BufferedReader(isr);
                    final String message = br.readLine();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DoctorRegistration.this, message, Toast.LENGTH_SHORT).show();

                            if (message.equals("Doctor Registration Done Successfully!!!")) {
                                startActivity(new Intent(DoctorRegistration.this, MainActivity.class));
                                finish();
                            }
                        }
                    });
                }
            } catch (IOException e) {
//                Toast.makeText(DoctorRegistration.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
            }
        }
    }
}
