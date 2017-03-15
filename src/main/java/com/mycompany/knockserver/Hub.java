package com.mycompany.knockserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by okhoruzhenko on 3/13/17.
 */
public class Hub implements Chat {

    ArrayList<Chattable> members;
    ServerSocket serverSocket;
    String message;
    int port;
    public Hub(int port) {
        this.port = port;
        members = new ArrayList<>();
    }

    @Override
    public void broadCastMessage(){
        members.forEach(member -> member.receive(this.message));
    }

    @Override
    public void post(Chattable member, String message){
        this.message = message;
        broadCastMessage();
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket incomingConnection = serverSocket.accept();
                PrintWriter out = new PrintWriter(incomingConnection.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(incomingConnection.getInputStream()));
                members.add(new ChatMember(in, out, this));
            }

        } catch(IOException e) {}

    }
}
