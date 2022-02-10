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


        while (serverSocket != null) {
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

                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                receiveFileFromClient();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fxLog.setEditable(false);
        fxLog.setWrapText(true);
    }

    public void sendFilesToClient(List<File> files){
        try {
            for (File file: files){
                FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
                String fileName = file.getName();
                System.out.println("File name is " + fileName);

                byte[] fileNameBytes = fileName.getBytes();
                byte[] fileContentBytes = new byte[(int) file.length()];

                int num = fileInputStream.read(fileContentBytes);
                dataOutputStream.writeInt(fileNameBytes.length);
                dataOutputStream.write(fileNameBytes);

                dataOutputStream.writeInt(fileContentBytes.length);
                dataOutputStream.write(fileContentBytes);
                dataOutputStream.flush();
            }
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public void receiveFileFromClient(){
        new Thread(() -> {
            while (true){
                try {
                    int fileNameLength = dataInputStream.readInt();
                    if (fileNameLength > 0) {
                        byte[] fileNameBytes = new byte[fileNameLength];
                        dataInputStream.readFully(fileNameBytes, 0, fileNameLength);
                        String filename = new String(fileNameBytes);

                        int fileContentLength = dataInputStream.readInt();
                        if (fileContentLength > 0) {
                            byte[] fileContentBytes = new byte[fileContentLength];
                            dataInputStream.readFully(fileContentBytes, 0, fileContentBytes.length);
                            downloadFile(filename, fileContentBytes);
                        }
                    }
                }
                catch (IOException exception){
                    exception.printStackTrace();
                }
            }
        }).start();
    }

    //TODO: Get download folder from user and save files here
    private void downloadFile(String fileName, byte[] fileContent) {
       try{
           File file = new File(""); // Save file here
           //File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
           FileOutputStream fileOutputStream = new FileOutputStream(file);
           fileOutputStream.write(fileContent);
           fileOutputStream.close();
           System.out.println("File " + file + " received");
       }
       catch (IOException ex){
           ex.printStackTrace();
       }
    }


    @FXML
    public void openFileDialog() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select files to send");
        List<File> files = chooser.showOpenMultipleDialog(fxSendFileButton.getScene().getWindow());
        sendFilesToClient(files);
    }


    public File showDownloadFileDialog(){
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select a folder");
        //String selectedDirPath = dirChooser.showDialog(mainApp.getPrimaryStage()).getAbsolutePath();
        //File downloadedFile = new File(selectedDirPath + "/" + downloadedFileName);
        return dirChooser.showDialog(new Stage());
    }
}
