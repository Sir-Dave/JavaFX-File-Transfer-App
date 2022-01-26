package com.sirdave;

import javafx.scene.image.Image;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class Util {

    static Image generateImage(String name){
        String filePath = new File("resources/" + name).toURI().toString();
        return new Image(filePath);
    }

    static URL generateFilePath(String name) throws MalformedURLException {
        return new File("resources/" + name).toURI().toURL();
    }
}
