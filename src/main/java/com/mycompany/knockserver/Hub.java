package com.mycompany.knockserver;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by okhoruzhenko on 3/13/17.
 */
public class Hub implements Chat {

    private ArrayList<Chattable> members;
    private ServerSocket serverSocket;
    private ConcurrentLinkedQueue<String> messages;
    private int port;
    public Hub(int port) {
        this.port = port;
        messages = new ConcurrentLinkedQueue<>();
        members = new ArrayList<>();
    }

    @Override
    public void broadCastMessage(){
        String m;
        while ((m = messages.poll()) != null) {
            for (Chattable member: members) {member.receive(m);}
        }
    }

    @Override
    public void post(String message){
        messages.add(message);
        broadCastMessage();
    }


    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket incomingConnection = serverSocket.accept();
                members.add(new ChatMember(incomingConnection, this));
            }

        } catch(IOException e) {}

    }
}
