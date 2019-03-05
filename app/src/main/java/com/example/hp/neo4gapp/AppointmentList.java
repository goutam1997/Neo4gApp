package com.example.hp.neo4gapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class AppointmentList extends AppCompatActivity {
    ListView listView;
    String message, patientEmail;

    private ArrayList<HashMap<String, String>> hashList = new ArrayList<HashMap<String,String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);

        listView=(ListView)findViewById(R.id.reg_doc_list);

//        MessageSender messageSender=new MessageSender();
//        messageSender.execute("Find:" + patientEmail);

        Thread myThread = new Thread(new AppointThread());
        myThread.start();
    }

    class AppointThread implements Runnable {
        Socket s;
        ServerSocket ss;
        InputStreamReader isr;
        BufferedReader br;
        Handler handler = new Handler();

        @Override
        public void run() {
//            while (true) {
                try {
                    ss = new ServerSocket(7802);
                    s = ss.accept();

                    isr = new InputStreamReader(s.getInputStream());
                    br = new BufferedReader(isr);
                    message = br.readLine();

                s.close();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.d("Before","Toast");
                                String msg2=String.valueOf(message);
                                Toast.makeText(AppointmentList.this, message, Toast.LENGTH_SHORT).show();
                                Log.d("After", "Toast");
                                String[] listItem = msg2.split("#");
//                                String[] listItem={"Goutam","Suman","Guddu"};
                                Log.d("Doc splited listitem", listItem[0]);

                                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(AppointmentList.this, R.layout.my_text_view, R.id.my_doctor, listItem);
                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                                        final String selectedDoctor = (String) listView.getItemAtPosition(position);

                                        AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentList.this);
                                        builder.setCancelable(true);
                                        builder.setTitle("Confirm Your Visit");
                                        builder.setMessage(selectedDoctor);
                                        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                MessageSender messageSender = new MessageSender();
                                                messageSender.execute(selectedDoctor);

                                                Toast.makeText(AppointmentList.this, "Thank You For Your Visit", Toast.LENGTH_SHORT).show();

                                                startActivity(new Intent(AppointmentList.this, MainActivity.class));
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

//                              CORRECT CODE HERE. DON'T DO ANYTHING WITH IT
//                                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(AppointmentList.this, R.layout.my_text_view, R.id.my_doctor, listItem);
//                                listView.setAdapter(adapter);
//
//                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//
//                                        final String selectedDoctor = (String) listView.getItemAtPosition(position);
//
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentList.this);
//                                        builder.setCancelable(true);
//                                        builder.setTitle("Confirm Your Visit");
//                                        builder.setMessage(selectedDoctor);
//                                        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//
//                                                MessageSender messageSender = new MessageSender();
//                                                messageSender.execute(selectedDoctor);
//
//                                                Toast.makeText(AppointmentList.this, "Thank You For Your Visit", Toast.LENGTH_SHORT).show();
//
//                                                startActivity(new Intent(AppointmentList.this, MainActivity.class));
//                                                finish();
//                                            }
//                                        });
//                                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                            }
//                                        });
//
//                                        AlertDialog dialog = builder.create();
//                                        dialog.show();
//                                    }
//                                });

                            } catch (Exception e) {
                            }

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//        }
    }
}
