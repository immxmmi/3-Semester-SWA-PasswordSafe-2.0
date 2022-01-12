package service;

import java.io.FileNotFoundException;

public interface FileHandler {

    int countLine(String path) throws FileNotFoundException;
    void writeToFile(String text,String path);
    void destroyFile(String path);
    void storeTextToFile(String password, String path) throws Exception;
    String readFile(String path) throws Exception;

}
