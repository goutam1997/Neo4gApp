package com.example.hp.neo4gapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class RegisterPatient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patient);

        final EditText patient_name=(EditText)findViewById(R.id.patient_name);
        final EditText patient_email=(EditText)findViewById(R.id.patient_email);
        final EditText patient_password=(EditText)findViewById(R.id.patient_password);
        Button register=(Button)findViewById(R.id.patient_register);
        Button login=(Button)findViewById(R.id.button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterPatient.this, PatientLogin.class));
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name=patient_name.getText().toString().trim();
                String Email=patient_email.getText().toString().trim();
                String Password=patient_password.getText().toString().trim();

                if (Name.isEmpty() || Email.isEmpty() || Password.isEmpty()) {
                    Toast.makeText(RegisterPatient.this, "Please Fill Up All the Fields", Toast.LENGTH_SHORT).show();
                } else {
                    String patientInfo="PatReg:" + Name + ":" + Email + ":" + Password;
                    MessageSender messageSender = new MessageSender();
                    messageSender.execute(patientInfo);

                    Thread myThread = new Thread(new MyServerThread());
                    myThread.start();
                }
            }
        });
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
                            Toast.makeText(RegisterPatient.this, message, Toast.LENGTH_SHORT).show();

                            if (message.equals("Patient Registration Done Successfully!!!")) {
                                startActivity(new Intent(RegisterPatient.this, MainActivity.class));
                                finish();
                            }
                        }
                    });
                }
            } catch (IOException e) {
//                Toast.makeText(RegisterPatient.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
            }
        }
    }
}
