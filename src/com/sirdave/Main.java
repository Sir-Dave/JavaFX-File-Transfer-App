package com.sirdave;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static com.sirdave.Util.generateFilePath;
import static com.sirdave.Util.generateImage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(generateFilePath("/sample.fxml"));
        primaryStage.setTitle("File Transfer App");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 700, 450));
        primaryStage.getIcons().add(generateImage("icon.png"));
        primaryStage.requestFocus();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);

    }
}
