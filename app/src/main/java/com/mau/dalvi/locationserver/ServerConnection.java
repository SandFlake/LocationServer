package com.mau.dalvi.locationserver;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ServerConnection {

    private String serverIP = "195.178.227.53";
    private int port = 7117;
    private Socket connection;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private String message;

    public void startRunning(){
       try {
           connectToServer();

           setUpStreams();
       } catch ( IOException io){
           io.printStackTrace();
       }
    }

    // connect to server
    private void connectToServer() throws IOException{
        System.out.println("attempting to connect....");
        connection = new Socket(InetAddress.getByName(serverIP), port);
        System.out.println("connect to " + connection.getInetAddress().getHostName());

    }

    //sets up streams to send and recieve messages
    private void setUpStreams() throws IOException {

        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();

        input = new ObjectInputStream(connection.getInputStream());
        System.out.println("Streams good to go heeeey");
    }

    private void whileChatting() throws IOException {
        try {
            message = (String) input.readObject();

        } catch (ClassNotFoundException cnfe) {
            System.out.println("I dont understand that object type");
        }
    }
}
