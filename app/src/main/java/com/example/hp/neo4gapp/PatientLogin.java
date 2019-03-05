package com.example.hp.neo4gapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class PatientLogin extends AppCompatActivity {
    private FusedLocationProviderClient client;
    private EditText input_date;
    String Email, Password, latitude, longitude, visit_date;

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);

        input_date = (EditText) findViewById(R.id.input_date);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        input_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(PatientLogin.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final EditText pat_email=(EditText)findViewById(R.id.pat_email);
        final EditText pat_password=(EditText)findViewById(R.id.pat_password);
        Button pat_login=(Button)findViewById(R.id.pat_login);
        Button pat_reg=(Button)findViewById(R.id.pat_register);

        pat_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email=pat_email.getText().toString().trim();
                Password=pat_password.getText().toString().trim();

                if (Email.isEmpty() || Password.isEmpty()) {
                    Toast.makeText(PatientLogin.this, "Please Fill Up All the Fields", Toast.LENGTH_SHORT).show();
                } else {
                    requestPermission();
                    client = LocationServices.getFusedLocationProviderClient(PatientLogin.this);

                    if (ActivityCompat.checkSelfPermission(PatientLogin.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PatientLogin.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(PatientLogin.this, "You have to Enable your Location", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    client.getLastLocation().addOnSuccessListener(PatientLogin.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                String myLoc = location.toString();
                                String my[] = myLoc.split(" ");
                                String po[] = my[1].split(",");
                                latitude = po[0];
                                longitude = po[1];
//                                Log.d("Latitude",latitude);
//                                Log.d("Longitude", longitude);

                                Log.d("Date1",visit_date);

//                                Toast.makeText(PatientLogin.this, "Date2: "+visit_date, Toast.LENGTH_SHORT).show();

                                String patientInfo = "PatLog:" + Email + ":" + Password + ":" + latitude + ":" + longitude + ":" +visit_date;
                                MessageSender messageSender = new MessageSender();
                                messageSender.execute(patientInfo);

                                Thread myThread = new Thread(new MyServerThread());
                                myThread.start();
                            }
                        }
                    });
                }
            }
        });

        pat_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PatientLogin.this, RegisterPatient.class));
                finish();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        input_date.setText(sdf.format(myCalendar.getTime()));
        visit_date=sdf.format(myCalendar.getTime());
        Log.d("Date",visit_date);
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
//                while(true) {
                    s=ss.accept();

                    isr = new InputStreamReader(s.getInputStream());
                    br = new BufferedReader(isr);
                    final String message = br.readLine();

                    s.close();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PatientLogin.this, message, Toast.LENGTH_SHORT).show();

                            if (message.equals("You have Logged in Successfully!!!")) {

                                Intent intent = new Intent(PatientLogin.this, DoctorType.class);
                                intent.putExtra("patientEmail", Email);
                                startActivity(intent);
                            }
                        }
                    });
//                }
            } catch (IOException e) {
//                Toast.makeText(PatientLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}