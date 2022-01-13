package service;

import modle.ConfigImpl;
import utility.ConfigServices;
import tools.TextColor;
import utility.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ConfigServicesImpl implements ConfigServices {
    private final static String configPath = "./config/config.ini";
    private static TextColor textColor = new TextColor();

    public ConfigServicesImpl() {
    }

    @Override
    public Config configBuilder(String root, String masterFolder, String masterFileName, String passwordFolder, String passwordFileName) {

       return ConfigImpl.builder()
               .root(root)
               .masterFolder(masterFolder)
               .masterFileName(masterFileName)
               .passwordFolder(passwordFolder)
               .passwordFileName(passwordFileName)
               .build();
    }

    @Override
    public Config loadConfigFile() throws IOException {
        Config config = ConfigImpl.builder().build();
        String line = "";
        File configFile = new File(configPath);
        if(configFile.exists()){
            BufferedReader reader = new BufferedReader(new FileReader(configFile.getPath()));

            line = reader.readLine();

            do{
                if(line.contains("root")){
                    config.setRoot(line.split("=")[1].replaceAll(" ",""));
                }else if(line.contains("masterFolder")){
                    config.setMasterFolder(line.split("=")[1].replaceAll(" ",""));
                }else if(line.contains("masterFileName")){
                    config.setMasterFileName (line.split("=")[1].replaceAll(" ",""));
                }else if(line.contains("passwordFolder")){
                    config.setPasswordFolder (line.split("=")[1].replaceAll(" ",""));
                }else if(line.contains("passwordFileName")){
                    config.setPasswordFileName (line.split("=")[1].replaceAll(" ",""));
                }

                line = reader.readLine();
            }while(line != null);
        }else{
            System.out.println(textColor.RED + "CONFIG FILE - NOT FOUND");
        }

        return config;
    }

}
