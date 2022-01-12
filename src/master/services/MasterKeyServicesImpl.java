package master.services;

import crypto.CipherFacility;
import crypto.CipherFacilityImpl;
import handler.Handler;
import handler.IHandler;
import master.MasterKey;
import master.MasterKeyImpl;
import tools.TextColor;

import java.io.File;
import java.util.Scanner;

public class MasterKeyServicesImpl implements MasterKeyServices { // TODO: 11.01.2022 Fertig

    /** ROOT **/
    public static String root = "./KEY/master/";
    public static String passwordPath = "./KEY/password/password.pw";
    private static MasterKey masterKey = null;
    private static String fileName;

    /** INSTANCE **/
    private CipherFacility cipherFacility = CipherFacilityImpl.builder().build();
    private IHandler handler = new Handler();
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
        System.out.println(textColor.ANSI_PURPLE +"Enter new master password !"+ textColor.ANSI_RED +" (Warning you will loose all already stored passwords)" + textColor.ANSI_RESET);
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
        System.out.println(textColor.ANSI_GREEN + "Success" + textColor.ANSI_RESET);
    }

    /** CHECK **/
    @Override
    public boolean checkMasterKey(String masterPassword) throws Exception{
        if(new File(this.masterKey.getMasterPasswordPath()).exists() == false){return false;}
        return handler.readFile(this.masterKey.getMasterPasswordPath()).equals(cipherFacility.HashText(this.masterKey.getCryptography(),masterPassword));
    }

}
