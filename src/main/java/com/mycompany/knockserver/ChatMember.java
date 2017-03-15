package com.mycompany.knockserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by okhoruzhenko on 3/15/17.
 */
public class ChatMember implements Chattable {
    BufferedReader in;
    PrintWriter out;
    Chat chat;
    ArrayList<String> messages;

    public ChatMember(BufferedReader in, PrintWriter out, Chat chat) {
        this.in = in;
        this.out = out;
        this.chat = chat;
        messages = new ArrayList<>();
        InputReader inputReader = new InputReader();
        OutputWriter outputWriter = new OutputWriter();
        new Thread(inputReader).start();
        new Thread(outputWriter).start();
    }

    class InputReader implements Runnable {
        public void run() {
            String inputLine;
            try {
                while ((inputLine = in.readLine()) != null) {
                    chat.post(ChatMember.this, inputLine);
                    Thread.sleep(300);
                }
            } catch(IOException e) {
            } catch (InterruptedException e) {}
        }
    }

    class OutputWriter implements Runnable {
        public void run() {
            try {
                while (true) {
                    if (messages.size() == 0 ) {
                        Thread.sleep(300);
                    } else {
                        String m = messages.get(0);
                        out.println(m);
                        synchronized (messages) {
                            messages.remove(m);
                        }
                    }
                }
            } catch (InterruptedException e) {}
        }
    }

    public synchronized void receive(String message) {
        messages.add(message);
    }


}
