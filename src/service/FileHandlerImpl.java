package service;

import utility.FileHandler;

import java.io.*;

public class FileHandlerImpl implements FileHandler {

    public FileHandlerImpl() {
    }

    @Override
    /** COUNTER **/
    public int countLine(String path) throws FileNotFoundException {
        File file = new File(path);
        if(file.exists()){

            BufferedReader datei = new BufferedReader(new FileReader(path));
            String z = null;
            int counter = 0;
            while (true) {
                try {
                    if (!((z = datei.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                counter++;
            }

            return counter-3;
        }
        return  0;
    }
    @Override
    /** WRITE FILE + Append **/
    public void writeToFile(String text,String path) {
        try (FileWriter f = new FileWriter(path, true);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter p = new PrintWriter(b);) {
            p.println(text);

            f.flush();
            b.flush();
            p.flush();
        } catch (IOException i) {
            i.printStackTrace();
        }
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
