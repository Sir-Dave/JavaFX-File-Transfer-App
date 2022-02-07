package com.sirdave;

public class MyFile {
    private String filename;
    private byte[] fileContent;

    public MyFile(String filename, byte[] fileContent) {
        this.filename = filename;
        this.fileContent = fileContent;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }
}
