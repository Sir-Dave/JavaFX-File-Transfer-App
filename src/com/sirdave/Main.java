package com.sirdave;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static com.sirdave.Util.generateFilePath;
import static com.sirdave.Util.generateImage;

public class Main extends Application {

    // Variables definition
    public String visiblePassword = "";
    public String hiddenPassword = "";

    static String networkName = null;
    static String networkKey = null;

    static TextField netNameField;
    static PasswordField netPassField;

    Action action = new Action();
    static Stage publicStage = new Stage();


    // Overriding Start method
    @Override
    public void start(Stage primaryStage) {

        /*
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        */

        publicStage = primaryStage;

        netNameField = new TextField();
        netNameField.setPromptText("Enter Hotspot Name");

        netPassField = new PasswordField();
        netPassField.setPromptText("Enter Password");

        TextField visiblePasswordField = new TextField();
        visiblePasswordField.setPromptText("Enter Password");

        Image imgpass = generateImage("hide.png");
        Button btnShowPass = new Button();
        btnShowPass.setGraphic(new ImageView(imgpass));

        Image imgpassshow = generateImage("show.png");


        Image imgStart = generateImage("start.png");
        Button btnStart = new Button("", new ImageView(imgStart));

        Image imgStop = generateImage("stop.png");
        Button btnStop = new Button("", new ImageView(imgStop));

        Label statusLbl = new Label();
        statusLbl.setText("Idle");

        Image imgCheck = generateImage("check.png");
        Button btnCheck = new Button("", new ImageView(imgCheck));
        btnCheck.setId("rm-bkgrnd");

        GridPane grd = new GridPane();
        grd.add(netPassField, 0, 0);
        grd.add(btnShowPass, 1, 0);
        grd.setVgap(10);
        grd.setHgap(10);

        StackPane stkpn = new StackPane();
        stkpn.getChildren().addAll(netNameField, grd,
                btnStart, btnStop, statusLbl, btnCheck);

        StackPane.setAlignment(netNameField, Pos.TOP_LEFT);
        StackPane.setAlignment(grd, Pos.TOP_LEFT);
        StackPane.setAlignment(btnStart, Pos.TOP_CENTER);
        StackPane.setAlignment(btnStop, Pos.CENTER);
        StackPane.setAlignment(statusLbl, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(btnCheck, Pos.BOTTOM_RIGHT);


        StackPane.setMargin(netNameField, new Insets(80, 50, 0, 50));
        StackPane.setMargin(grd, new Insets(130, 50, 0, 50));
        StackPane.setMargin(btnStart, new Insets(200, 0, 0, 0));
        StackPane.setMargin(btnStop, new Insets(210, 0, 0, 0));
        StackPane.setMargin(statusLbl, new Insets(0, 0, 5, 0));
        StackPane.setMargin(btnCheck, new Insets(0, -20, -3, 0));

        BorderPane root = new BorderPane();
        root.setCenter(stkpn);

        Scene scene = new Scene(root, 350, 430);
        scene.getStylesheets().add(generateFilePath("styles.css"));

        netNameField.setFocusTraversable(true);
        netPassField.setFocusTraversable(false);
        visiblePasswordField.setFocusTraversable(false);
        ////    ****  Layout Design End  ****    ////

        ////    ****  Actions Start  ****    ////
        btnShowPass.setOnMousePressed(e -> {
            //
            Button button = (Button) e.getSource();
            button.setGraphic(new ImageView(imgpassshow));
            //
            this.visiblePassword = netPassField.getText();
            netPassField.setText(null);
            grd.getChildren().remove(netPassField);
            grd.getChildren().add(visiblePasswordField);
            visiblePasswordField.setText(visiblePassword);
        });

        btnShowPass.setOnMouseReleased(e -> {
            //
            Button button = (Button) e.getSource();
            button.setGraphic(new ImageView(imgpass));
            //
            this.hiddenPassword = visiblePasswordField.getText();
            visiblePasswordField.setText(null);
            grd.getChildren().remove(visiblePasswordField);
            grd.getChildren().add(netPassField);
            netPassField.setText(hiddenPassword);
        });

        btnStart.setOnAction(e -> {
            action.executeCommand("netsh wlan start hostednetwork");
            String activation = "The hosted network couldn't be started.";
            int index = activation.indexOf(action.outputString);

            if (!action.outputString.contains(activation)) {

                action.executeCommand("netsh wlan stop hostednetwork");

                if (!netNameField.getText().equals("") && netPassField.getText().length() >= 8) {
                    Action.initializeHotspot();
                    action.startHotspot();
                    statusLbl.setText("Started...");

                } else if (netNameField.getText().equals("")) {
                    action.enterName();

                } else if (netPassField.getText().length() <= 8) {
                    action.eightChar();

                } else {
                    action.internalError();
                }
            } else {
                action.warning();
            }
        });

        btnStop.setOnAction(e -> {
            action.stopHotspot();
            statusLbl.setText("Stopped");
        });

        btnCheck.setOnAction(e -> {
            action.checkingAvailability();
        });

        primaryStage.setTitle("Wifi Application");
        primaryStage.getIcons().add(generateImage("icon.png"));
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
