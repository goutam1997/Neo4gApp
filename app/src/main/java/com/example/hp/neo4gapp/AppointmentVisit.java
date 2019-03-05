package com.example.hp.neo4gapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class AppointmentVisit extends AppCompatActivity {
    private TextView welcome;
    private Button appointment, visit, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_visit);

        Bundle bundle = getIntent().getExtras();
        final String patientEmail = bundle.getString("patientEmail");

        welcome = (TextView)findViewById(R.id.welcome);
        appointment = (Button)findViewById(R.id.appointment);
        visit = (Button)findViewById(R.id.visit);
        logout = (Button)findViewById(R.id.logout);

        welcome.setText("Welcome " + patientEmail);

        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppointmentVisit.this, PresentDoctor.class);
                intent.putExtra("patientEmail", patientEmail);
                startActivity(intent);
            }
        });

        visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppointmentVisit.this, AppointmentList.class);
                intent.putExtra("patientEmail", patientEmail);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppointmentVisit.this, MainActivity.class));
                finish();
            }
        });
    }

//    class DoctorThread implements Runnable {
//        Socket s;
//        ServerSocket ss;
//        InputStreamReader isr;
//        BufferedReader br;
//        String message;
//        Handler handler = new Handler();
//
//        @Override
//        public void run() {
//            try {
//                ss = new ServerSocket(7801);
//                while (true) {
//                    s = ss.accept();
//
//                    isr = new InputStreamReader(s.getInputStream());
//                    br = new BufferedReader(isr);
//                    final String message = br.readLine();
//
//                    s.close();
//
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(AppointmentVisit.this, message, Toast.LENGTH_SHORT).show();
//
////                            if (message.equals("You have Logged in Successfully!!!")) {
////
////                                Intent intent = new Intent(AppointmentVisit.this, AppointmentVisit.class);
////                                intent.putExtra("patientEmail", Email);
////                                startActivity(intent);
////                            }
//                        }
//                    });
//
//                }
//            } catch (IOException e) {
//                Toast.makeText(AppointmentVisit.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
//        }
//    }
}
