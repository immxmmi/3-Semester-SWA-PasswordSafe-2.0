package service;

import utility.ConfigServices;
import utility.Config;
import utility.CipherFacility;
import crypto.CipherFacilityImpl;
import modle.MasterKeyImpl;
import tools.TextColor;
import utility.FileHandler;
import utility.MasterKey;
import utility.MasterKeyServices;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MasterKeyServicesImpl implements MasterKeyServices { // TODO: 11.01.2022 Fertig


    /** CONFIG **/
    private static ConfigServices configServices = new ConfigServicesImpl();
    private static Config config;
    static {
        try {
            config = configServices.loadConfigFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** ROOT **/
    public static String root = config.getRoot() + config.getMasterFolder();
    public static String passwordPath = config.getRoot() + config.getPasswordFolder() + config.getPasswordFileName();
    private static MasterKey masterKey = null;
    private String fileName;

    /** INSTANCE **/
    private CipherFacility cipherFacility = CipherFacilityImpl.builder().build();
    private FileHandler handler = new FileHandlerImpl();
    private TextColor textColor = new TextColor();

    /** STATIC **/
    static {
        new File(root).mkdir();
    }

    /** CONSTRUCTOR **/
    public MasterKeyServicesImpl(String masterPasswordPath, String passwordPath){
        this.fileName = masterPasswordPath;
        this.passwordPath = passwordPath;
        this.masterKey = MasterKeyImpl.builder()
                .masterPasswordPath(this.root+masterPasswordPath)
                .build();
    }

    /** INSTANCE **/
    private static MasterKeyServicesImpl instance = new MasterKeyServicesImpl("","");


    /** SETTER **/
    @Override
    public void setNewMasterKey() throws Exception {
        Scanner read = new Scanner(System.in);
        System.out.println(textColor.PURPLE +"Enter new master password !"+ textColor.RED +" (Warning you will loose all already stored passwords)" + textColor.RESET);
        // Liest neues masterPW ein
        String masterPw = read.next();
        // HASHED das neue masterPW
        String hashPw = cipherFacility.HashText(this.masterKey.getCryptography(),masterPw);
        // Speichert das neue masterPW im File
        handler.storeTextToFile(hashPw,this.masterKey.getMasterPasswordPath());
        // Löscht alle alten PW
        handler.destroyFile("./KEY/password/password.pw");
        // setzt das neue PW --> in MasterKEY
        this.masterKey.setMasterPasswordPlain(hashPw);
        // LEERT DIE PW - LISTE
        this.masterKey.setPasswords(null);
        System.out.println(textColor.GREEN + "Success" + textColor.RESET);
    }

    /** CHECK **/
    @Override
    public boolean checkMasterKey(String masterPassword) throws Exception{
        if(new File(this.masterKey.getMasterPasswordPath()).exists() == false){return false;}
        return handler.readFile(this.masterKey.getMasterPasswordPath()).equals(cipherFacility.HashText(this.masterKey.getCryptography(),masterPassword));
    }

}
