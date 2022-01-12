package handler;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Handler implements IHandler {

    public Handler() {
    }

    @Override
    /** READ FROM FILE **/
    public String readFile(String path) throws Exception {
        File passwordFile = new File(path);
        char[] buffer = null;
        if (passwordFile.exists()) {
            FileReader reader = null;
            try {
                buffer = new char[(int)passwordFile.length()];
                reader = new FileReader(passwordFile);
                reader.read(buffer);
            }
            finally {
                if (reader != null) { try { reader.close(); } catch (IOException ex) { } };
            }
        }
        return buffer == null ? null : new String(buffer);
    }
    @Override
    /** OVERWRITE FILE**/
    public void storeTextToFile(String password, String path) throws Exception{
        FileWriter writer = null;
        try {
            // PATH
            writer = new FileWriter(path);
            // Speichert Master KEY in den PATH
            writer.write(password);
        } finally {
            if(writer != null) try {
                writer.flush();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    @Override
    /** DELETE File **/
    public void destroyFile(String path) {
        File file = new File(path);
        if(file.exists()){
            System.out.println("FILE GEFUNDEN");
            file.delete();
        }
    }
}
