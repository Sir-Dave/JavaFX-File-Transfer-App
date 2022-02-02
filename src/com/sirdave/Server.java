package com.sirdave;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;

    public Server(ServerSocket serverSocket){
        try {
            this.serverSocket = serverSocket;
            this.socket = serverSocket.accept();
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
            this.dataInputStream = new DataInputStream(socket.getInputStream());
        }
        catch (IOException exception){
            System.out.println("Error creating server");
            exception.printStackTrace();
            closeServer(socket, dataOutputStream, dataInputStream);
        }
    }


    public void closeServer(Socket socket, DataOutputStream outputStream, DataInputStream inputStream){
        try {
            if (socket != null)
                socket.close();

            if (outputStream != null)
                outputStream.close();

            if (inputStream != null)
                inputStream.close();
        }
        catch (IOException exception){
            System.out.println("ERROR: Shutting down connection");
            exception.printStackTrace();
        }
    }


}
