package com.example.hp.neo4gapp;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageSender extends AsyncTask<String, Void, Void> {
    Socket s;
    PrintWriter writer;

    @Override
    protected Void doInBackground(String... voids) {
        try {
            String message = voids[0];
            s=new Socket("10.12.194.14",6001);
            writer=new PrintWriter(s.getOutputStream());
            writer.write(message);
            writer.flush();
            writer.close();

//            ObjectOutputStream objectOutput = new ObjectOutputStream(s.getOutputStream());
//            objectOutput.writeObject(message);

//                s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

