package com.sirdave;

import javafx.scene.image.Image;

import java.io.File;

public class Util {

    static Image generateImage(String name){
        String filePath = new File("resources/" + name).toURI().toString();
        return new Image(filePath);
    }

    static String generateFilePath(String name) {
        return new File("resources/" + name).toURI().toString();
    }
}
