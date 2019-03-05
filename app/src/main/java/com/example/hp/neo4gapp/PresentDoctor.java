package com.example.hp.neo4gapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class PresentDoctor extends AppCompatActivity {
    private EditText disease;
    ListView listView;
    String message, patientEmail;
    String[] listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_doctor);

        Bundle bundle = getIntent().getExtras();
        patientEmail = bundle.getString("patientEmail");

        disease=(EditText)findViewById(R.id.editText);
        listView=(ListView)findViewById(R.id.sun);

        MessageSender messageSender=new MessageSender();
        messageSender.execute("PatApt");

        Thread myThread = new Thread(new Doctor2Thread());
        myThread.start();
    }

    class Doctor2Thread implements Runnable {
        Socket s;
        ServerSocket ss;
        InputStreamReader isr;
        BufferedReader br;
        Handler handler = new Handler();

        @Override
        public void run() {
            try {
                ss=new ServerSocket(7802);
                s=ss.accept();

                isr = new InputStreamReader(s.getInputStream());
                br = new BufferedReader(isr);
                message = br.readLine();

                s.close();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Toast.makeText(PresentDoctor.this, message, Toast.LENGTH_SHORT).show();
                                listItem = message.split("#");

                                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(PresentDoctor.this, android.R.layout.simple_list_item_1, listItem);
                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                                        final String patientDisease=disease.getText().toString().trim();

                                        if(patientDisease.isEmpty())
                                            Toast.makeText(PresentDoctor.this, "Please Enter Your Disease First", Toast.LENGTH_SHORT).show();
                                        else {
                                            final String selectedDoctor = (String) listView.getItemAtPosition(position);

                                            AlertDialog.Builder builder = new AlertDialog.Builder(PresentDoctor.this);
                                            builder.setCancelable(true);
                                            builder.setTitle("Confirm Your Appointment");
                                            builder.setMessage(selectedDoctor);
                                            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    String []docorInfo = selectedDoctor.split(":");

                                                    MessageSender messageSender=new MessageSender();
                                                    messageSender.execute("Conf:" + docorInfo[1] + ":" + patientDisease + ":" + patientEmail);
                                                    Toast.makeText(PresentDoctor.this, ""+selectedDoctor, Toast.LENGTH_SHORT).show();

                                                    startActivity(new Intent(PresentDoctor.this, MainActivity.class));
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
                                });

                            } catch (Exception e) {
                            }

                        }
                    });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
