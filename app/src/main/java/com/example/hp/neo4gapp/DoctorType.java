package com.example.hp.neo4gapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class DoctorType extends AppCompatActivity {
    private ListView listView;
    private String[] listItem = {"ENT", "Child Specialist", "Gynecologist", "Orthopedic", "General Physician"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_type);

        listView=(ListView)findViewById(R.id.type_of_doctor);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(DoctorType.this, android.R.layout.simple_list_item_1, listItem);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                final String selectedDoctor = (String) listView.getItemAtPosition(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(DoctorType.this);
                builder.setCancelable(true);
                builder.setTitle("Confirm Your Visit");
                builder.setMessage(selectedDoctor);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        MessageSender messageSender = new MessageSender();
                        messageSender.execute(selectedDoctor);

                        startActivity(new Intent(DoctorType.this, AppointmentList.class));
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
}
