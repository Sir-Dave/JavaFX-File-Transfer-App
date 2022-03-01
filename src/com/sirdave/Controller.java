package com.sirdave;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Text fxIpAddress;

    @FXML
    private Text fxPort;

    @FXML
    private Text fxServerStatus;

    @FXML
    private Text fxMobileConnected;

    @FXML
    private TextArea fxLog;

    @FXML
    private Button fxStartButton;

    @FXML
    private Button fxSendFileButton;

    private ServerSocket serverSocket = null;
    private Socket socket;
    private int port;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    String folderName;

    public Controller() {}

    @FXML
    void startApp() {
        if (serverSocket == null) {
            Runnable runnable = this::startServer;

            Thread t = new Thread(runnable);
            t.setDaemon(true);
            t.start();

            fxStartButton.setText("Stop Server");
        } else {

            try {
                new Thread(() -> {
                    try {
                        serverSocket.close();
                        serverSocket = null;

                        Platform.runLater(() -> fxServerStatus.setText("Stopped"));

                        fxLog.setText("");
                        fxIpAddress.setText("");
                        fxPort.setText("");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

                Platform.runLater(() -> {
                    fxStartButton.setText("Start Server");
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void startServer() {
        port = 19051;

        Platform.runLater(() -> {
            fxServerStatus.setText("Initializing....");
        });

        try {
            serverSocket = new ServerSocket(port);
            Platform.runLater(() -> {
                fxServerStatus.setText("Running");
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        while (serverSocket!= null) {
            try {

                Platform.runLater(() -> {
                    try {
                        fxIpAddress.setText(InetAddress.getLocalHost() + "\n");
                        fxLog.appendText("Waiting for a new request... " + "\n");
                        fxPort.setText(port + "");
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                });

                socket = serverSocket.accept();

                Platform.runLater(() -> {
                    fxLog.appendText("Connected to client >> " + socket.getInetAddress() + "\n");
                    fxMobileConnected.setText("Connected");;
                });

                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                MyThread myThread = new MyThread();
                new Thread(myThread).start();

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fxLog.setEditable(false);
        fxLog.setWrapText(true);
        folderName = getDownloadPath();
    }

    public void sendFilesToClient(List<File> files){
        try {
            for (File file: files){
                FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
                byte[] bytesIn = new byte[4096];
                int read;

                // send file name
                byte[] fileNameBytes = file.getName().getBytes();
                dataOutputStream.writeInt(fileNameBytes.length);
                dataOutputStream.write(fileNameBytes);

                // send file size
                dataOutputStream.writeLong(file.length());

                while ((read = fileInputStream.read(bytesIn)) != -1) {
                    dataOutputStream.write(bytesIn, 0, read);
                    dataOutputStream.flush();
                }
                fileInputStream.close();
                Platform.runLater(() -> fxLog.appendText("File " + file.getName() + " sent\n"));
            }
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }

    class MyThread implements Runnable{

        @Override
        public void run() {
            receiveFilesFromClient();
        }
    }

    public void receiveFilesFromClient(){
        try {
            while (true){
                String filename;

                int fileNameLength = dataInputStream.readInt();
                if (fileNameLength > 0) {
                    byte[] fileNameBytes = new byte[fileNameLength];
                    dataInputStream.readFully(fileNameBytes, 0, fileNameLength);
                    filename = new String(fileNameBytes, StandardCharsets.UTF_8);

                    int bytes;

                    File file = new File(folderName, filename);
                    FileOutputStream fileOutputStream = new FileOutputStream(file);

                    long size = dataInputStream.readLong();     // read file size
                    byte[] buffer = new byte[4096];
                    while (size > 0 && (bytes = dataInputStream.read(buffer, 0,
                            (int)Math.min(buffer.length, size))) != -1) {
                        fileOutputStream.write(buffer,0,bytes);
                        size -= bytes;      // read up to file size
                    }
                    fileOutputStream.close();
                    Platform.runLater(() -> fxLog.appendText("File saved to " + file + "\n"));
                }
            }
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }

    @FXML
    private String getDownloadPath(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directory = directoryChooser.showDialog(new Stage());
        return directory.getAbsolutePath();
    }


    @FXML
    public void openFileDialog() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select files to send");
        List<File> files = chooser.showOpenMultipleDialog(fxSendFileButton.getScene().getWindow());
        sendFilesToClient(files);
    }
}
