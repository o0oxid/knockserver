package com.mycompany.knockserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.net.Socket;

/**
 * Created by okhoruzhenko on 3/15/17.
 */
public class ChatMember implements Chattable {
    private Chat chat;
    private ConcurrentLinkedQueue<String> messages;
    private String inputLine;
    private Socket incomingConnection;

    public ChatMember(Socket incomingConnection, Chat chat) {
        this.incomingConnection = incomingConnection;
        this.chat = chat;
        messages = new ConcurrentLinkedQueue<>();
        InputReader inputReader = new InputReader();
        OutputWriter outputWriter = new OutputWriter();
        new Thread(inputReader).start();
        new Thread(outputWriter).start();
    }

    class InputReader implements Runnable {
        @Override
        public void run() {
            try(BufferedReader in = new BufferedReader(new InputStreamReader(incomingConnection.getInputStream()))) {
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.equals("bye")) {
                        incomingConnection.close();
                        break;
                    }
                    chat.post(inputLine);
                    Thread.sleep(300);
                }
            } catch(IOException e) {
            } catch (InterruptedException e) {}
        }
    }

    class OutputWriter implements Runnable {
        @Override
        public void run() {
            try( PrintWriter out = new PrintWriter(incomingConnection.getOutputStream(), true)) {
                while (true) {
                    if (messages.isEmpty() ) {
                        Thread.sleep(300);
                    } else {
                        out.println(messages.poll());
                    }
                }
            } catch (InterruptedException e) {}
            catch (IOException e) {}
        }
    }


    public void receive(String message) {
        if (message != inputLine ) {
            messages.add(message);
        }
    }


}
