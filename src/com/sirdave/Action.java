package com.sirdave;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.sirdave.Util.generateFilePath;
import static com.sirdave.Util.generateImage;

public class Action {

    public String outputString;

    public void executeCommand(String command) {

        StringBuilder output = new StringBuilder();
        Process processCommand;

        try {
            processCommand = Runtime.getRuntime().exec(command);
            processCommand.waitFor();
            String line;

            BufferedReader reader = new BufferedReader(new InputStreamReader(processCommand.getInputStream()));
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            outputString = output.toString();

        } catch (IOException | InterruptedException ex) {
            internalError();
        }

    }

    public static void initializeHotspot() {
        Main.networkName = Main.netNameField.getText();
        Main.networkKey = Main.netPassField.getText();

        try {
            Process p = Runtime.getRuntime().exec("netsh wlan set hostednetwork mode=allow ssid=\""
                    + Main.networkName + "\" key="
                    + Main.networkKey);

        } catch (IOException ex) {
            Action ac = new Action();
            ac.internalError();
        }
    }

    public void startHotspot() {
        try {
            Process p = Runtime.getRuntime().exec("netsh wlan start hostednetwork");
        } catch (IOException ex) {
            internalError();
        }
    }

    public void stopHotspot() {
        try {
            Process p = Runtime.getRuntime().exec("netsh wlan stop hostednetwork");
        } catch (IOException ex) {
            internalError();
        }
    }

    public void aboutMeStage() {

        final Stage aboutMe = new Stage();

        final Label aboutMeHeader = new Label();
        final String content = "\n   ======= About ======= \n"
                + "            " + "\n\t    "
                + "-→ Hey Wifi ! ←-\n" + "   " + "\n\t\t       </>\n\t     Younus Shalaby";
        aboutMeHeader.setId("header");

        Label aboutMeVersion = new Label(" Version:");
        aboutMeVersion.setId("yellow");

        ImageView aboutMeConnect = new ImageView(generateImage("msg.png"));

        Label versioninfo = new Label("1.0  [alpha]");

        Label connectinfo = new Label("m.me/YounusShalaby");

// ****************  //  // ****************  //  // ****************  //
        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(5000));
            }

            @Override
            protected void interpolate(double frac) {
                final int length = content.length();
                final int n = Math.round(length * (float) frac);
                aboutMeHeader.setText(content.substring(0, n));
            }
        };
        animation.play();
// ****************  //  // ****************  //  // ****************  //

        GridPane aboutMegrid = new GridPane();
        aboutMegrid.setAlignment(Pos.CENTER_LEFT);
        aboutMegrid.setHgap(10);
        aboutMegrid.setVgap(10);
        aboutMegrid.setPadding(new Insets(100, 25, 25, 25));

        aboutMegrid.add(aboutMeVersion, 0, 0);
        aboutMegrid.add(versioninfo, 1, 0);
        aboutMegrid.add(aboutMeConnect, 0, 1);
        aboutMegrid.add(connectinfo, 1, 1);
        aboutMegrid.setAlignment(Pos.BOTTOM_CENTER);

        BorderPane bp = new BorderPane();
        bp.setTop(aboutMeHeader);

        StackPane aboutMeroot = new StackPane();
        aboutMeroot.getChildren().addAll(bp, aboutMegrid);

        Scene aboutMeScene = new Scene(aboutMeroot, 300, 300);
        aboutMe.setScene(aboutMeScene);
        aboutMeScene.getStylesheets().add(generateFilePath("styles.css"));
        aboutMe.initModality(Modality.APPLICATION_MODAL);
        aboutMe.setResizable(false);
        aboutMe.setTitle("About me");
        aboutMe.getIcons().add(generateImage("icon.png"));
        aboutMe.showAndWait();
    }

    public void enterName() {

        final Stage hotspotWarning = new Stage();

        Label warningLabel = new Label();
        warningLabel.setText("Please...\nEnter Name For Your Hotspot");

        Image warning = generateImage("alert.png");
        ImageView warningView = new ImageView();
        warningView.setImage(warning);

        VBox hotspotVbox = new VBox(20);
        hotspotVbox.setAlignment(Pos.CENTER);

        hotspotVbox.getChildren().add(warningView);
        hotspotVbox.getChildren().add(warningLabel);

        Scene hotspotWarningScene = new Scene(hotspotVbox, 250, 120);
        hotspotWarning.setScene(hotspotWarningScene);
        hotspotWarningScene.getStylesheets().add(generateFilePath("styles.css"));

        hotspotWarning.setTitle("Hotspot Name Alert");
        hotspotWarning.getIcons().add(generateImage("alert.png"));
        hotspotWarning.initModality(Modality.APPLICATION_MODAL);
        hotspotWarning.initOwner(Main.publicStage);
        hotspotWarning.setResizable(false);
        hotspotWarning.show();
    }

    public void eightChar() {

        final Stage hotspotWarning = new Stage();

        Label warningLabel = new Label();
        warningLabel.setText("At least 8 Charachter\npassword required");

        Image warning = generateImage("alert.png");
        ImageView warningView = new ImageView();
        warningView.setImage(warning);

        VBox hotspotVbox = new VBox(20);
        hotspotVbox.setAlignment(Pos.CENTER);

        hotspotVbox.getChildren().add(warningView);
        hotspotVbox.getChildren().add(warningLabel);

        Scene hotspotWarningScene = new Scene(hotspotVbox, 250, 120);
        hotspotWarning.setScene(hotspotWarningScene);
        hotspotWarningScene.getStylesheets().add(generateFilePath("styles.css"));

        hotspotWarning.setTitle("Password Alert");
        hotspotWarning.getIcons().add(generateImage("alert.png"));
        hotspotWarning.initModality(Modality.APPLICATION_MODAL);
        hotspotWarning.initOwner(Main.publicStage);
        hotspotWarning.setResizable(false);
        hotspotWarning.show();
    }

    public void internalError() {

        final Stage error = new Stage();

        Label errorLabel = new Label();
        errorLabel.setText("Internal Error\nWe are Sorry About that  :( ");

        Image warning = generateImage("error.png");
        ImageView warningView = new ImageView();
        warningView.setImage(warning);

        Button close = new Button("Close");

        close.setOnAction(e -> {
            error.close();
            Main.publicStage.close();
        });

        VBox hotspotVbox = new VBox(20);
        hotspotVbox.setAlignment(Pos.CENTER);

        hotspotVbox.getChildren().add(warningView);
        hotspotVbox.getChildren().add(errorLabel);
        hotspotVbox.getChildren().add(close);

        Scene errorScene = new Scene(hotspotVbox, 250, 170);
        error.setScene(errorScene);
        errorScene.getStylesheets().add(generateFilePath("styles.css"));

        error.setTitle("Error");
        error.getIcons().add(generateImage("error.png"));
        error.initModality(Modality.APPLICATION_MODAL);
        error.initOwner(Main.publicStage);
        error.setResizable(false);
        error.show();
    }

    public void warning() {

        final Stage hotspotWarning = new Stage();

        Label warningLabel = new Label();
        warningLabel.setText("Please...\nTurn on your Wi-Fi Device");

        Image warning = generateImage("alert.png");
        ImageView warningView = new ImageView();
        warningView.setImage(warning);

        VBox hotspotVbox = new VBox(20);
        hotspotVbox.setAlignment(Pos.CENTER);

        hotspotVbox.getChildren().add(warningView);
        hotspotVbox.getChildren().add(warningLabel);

        Scene hotspotWarningScene = new Scene(hotspotVbox, 250, 120);
        hotspotWarning.setScene(hotspotWarningScene);
        hotspotWarningScene.getStylesheets().add(generateFilePath("styles.css"));

        hotspotWarning.setTitle("Device Mode");
        hotspotWarning.getIcons().add(generateImage("alert.png"));
        hotspotWarning.initModality(Modality.APPLICATION_MODAL);
        hotspotWarning.initOwner(Main.publicStage);
        hotspotWarning.setResizable(false);
        hotspotWarning.show();
    }

    public void checkingAvailability() {

        final Stage dialog = new Stage();

        Label label = new Label();

        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);

        executeCommand("netsh wlan show drivers");
        String deviceAvailability = "Hosted network supported  : Yes";

        if (outputString.contains(deviceAvailability)) {

            label.setText("Wi-Fi Device Available!");
            Image dialogImg = generateImage("avail.png");
            ImageView dialogImage = new ImageView();
            dialogImage.setImage(dialogImg);
            dialogImage.setSmooth(true);
            dialogImage.setCache(true);
            dialogVbox.getChildren().add(dialogImage);

        } else {
            label.setText("Wi-Fi Device not Available,\nPlease Install Wi-Fi Driver");
            Image dialogImg = generateImage("error.png");
            ImageView dialogImage = new ImageView();
            dialogImage.setImage(dialogImg);
            dialogImage.setSmooth(true);
            dialogImage.setCache(true);
            dialogVbox.getChildren().add(dialogImage);

        }
        dialogVbox.getChildren().add(label);

        Scene dialogScene = new Scene(dialogVbox, 250, 120);
        dialog.setScene(dialogScene);
        dialogScene.getStylesheets().add(generateFilePath("styles.css"));

        dialog.getIcons().add(generateImage("info.png"));
        dialog.setResizable(false);
        dialog.setTitle("Device Mode");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(Main.publicStage);
        dialog.show();
    }

}
