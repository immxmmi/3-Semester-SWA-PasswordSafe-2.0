package handler;

import java.io.FileWriter;
import java.io.IOException;

public interface IHandler {
    /**
     * DELETE File
     **/
    void destroyFile(String path);
    void storeTextToFile(String password, String path) throws Exception;
    String readFile(String path) throws Exception;

}
