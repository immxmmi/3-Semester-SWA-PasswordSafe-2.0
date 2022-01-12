package config;


import tools.TextColor;

import java.io.*;

public class ConfigImpl implements Config {

    private static TextColor textColor = new TextColor();
    private final static String configPath = "./config/config.ini";
    private String root = "./KEY";
    private String masterFolder = "/master";
    private String masterFileName = "/master.pw";
    private String passwordFolder = "/password";
    private String passwordFileName = "/password.pw";

    public ConfigImpl(boolean configFile) throws IOException {
        if(configFile){
            this.loadConfigFile();
        }
    }
    public ConfigImpl(String root, String masterFolder, String masterFileName, String passwordFolder, String getPasswordFileName) {
        this.root = root;
        this.masterFolder = masterFolder;
        this.masterFileName = masterFileName;
        this.passwordFolder = passwordFolder;
        this.passwordFileName = getPasswordFileName;
    }

    @Override
    public void loadConfigFile() throws IOException {
        String line = "";
        File config = new File(configPath);
        if(config.exists()){
            BufferedReader reader = new BufferedReader(new FileReader(config.getPath()));

            line = reader.readLine();

            do{
                if(line.contains("root")){
                    setRoot(line.split("=")[1].replaceAll(" ",""));
                }else if(line.contains("masterFolder")){
                    setMasterFolder(line.split("=")[1].replaceAll(" ",""));
                }else if(line.contains("masterFileName")){
                    setMasterFileName (line.split("=")[1].replaceAll(" ",""));
                }else if(line.contains("passwordFolder")){
                    setPasswordFolder (line.split("=")[1].replaceAll(" ",""));
                }else if(line.contains("passwordFileName")){
                    setPasswordFileName (line.split("=")[1].replaceAll(" ",""));
                }

            line = reader.readLine();
            }while(line != null);
        }else{
            System.out.println(textColor.RED + "CONFIG FILE - NOT FOUND");
        }
    }

    @Override
    public void setRoot(String root) {
        this.root = root;
    }
    @Override
    public void setMasterFolder(String masterFolder) {
        this.masterFolder = masterFolder;
    }
    @Override
    public void setMasterFileName(String masterFileName) {
        this.masterFileName = masterFileName;
    }

    @Override
    public void setPasswordFolder(String passwordFolder) {
        this.passwordFolder = passwordFolder;
    }

    @Override
    public void setPasswordFileName(String passwordFileName) {
        this.passwordFileName = passwordFileName;
    }

    @Override
    public String getRoot() {
        return root;
    }

    @Override
    public String getMasterFolder() {
        return masterFolder;
    }

    @Override
    public String getMasterFileName() {
        return masterFileName;
    }

    @Override
    public String getPasswordFolder() {
        return passwordFolder;
    }

    @Override
    public String getPasswordFileName() {
        return passwordFileName;
    }
}
