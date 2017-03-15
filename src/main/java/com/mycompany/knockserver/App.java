package com.mycompany.knockserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    Hub myChat = new Hub(6666);
    myChat.run();
    }
}
