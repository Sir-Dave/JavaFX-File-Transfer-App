package com.sirdave;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/com/sirdave/sample.fxml"));
        primaryStage.setTitle("File Transfer App");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 700, 450));
        String file = getClass().getResource("/com/sirdave/icon.png").toString();
        Image image = new Image(file);
        primaryStage.getIcons().add(image);
        primaryStage.requestFocus();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);

    }
}
