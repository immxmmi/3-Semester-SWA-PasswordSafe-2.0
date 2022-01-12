package master.services;
import master.MasterKey;
import master.MasterKeyImpl;
import password.services.PasswordServices;
import tools.Tools;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MasterKeyServices extends Tools{ // TODO: 11.01.2022 Fertig

    /** ROOT **/
    public static String root = "./KEY/master/";
    public static String passwordPath = "./KEY/password/password.pw";
    private static MasterKey masterKey = null;
    private static String fileName;

    /** STATIC **/
    static {
        new File(root).mkdir();
    }

    /** CONSTRUCTOR **/
    public MasterKeyServices(String masterPasswordPath,String passwordPath){
        this.fileName = masterPasswordPath;
        this.passwordPath = passwordPath;
        this.masterKey = MasterKeyImpl.builder()
                .masterPasswordPath(this.root+masterPasswordPath)
                .build();
    }

    /** INSTANCE **/
    private static MasterKeyServices instance = new MasterKeyServices("","");

    /** GETTER **/
    public static MasterKeyServices getInstance(){
        return new MasterKeyServices("","");
    }
    public static String getRoot() {
        return root;
    }
    public static String getFileName() {
        return fileName;
    }

    /** SETTER **/
    public void setNewMasterKey() throws Exception {
        Scanner read = new Scanner(System.in);
        System.out.println(ANSI_PURPLE +"Enter new master password !"+ ANSI_RED +" (Warning you will loose all already stored passwords)" + ANSI_RESET);
        // Liest neues masterPW ein
        String masterPw = read.next();
        // HASHED das neue masterPW
        String hashPw = hashMasterKey(masterPw);
        // Speichert das neue masterPW im File
        this.StoreMasterPasswordToFile(hashPw);
        // Löscht alle alten PW
        this.destroyPasswordFile();
        // setzt das neue PW --> in MasterKEY
        this.masterKey.setMasterPasswordPlain(hashPw);
        // LEERT DIE PW - LISTE
        this.masterKey.setPasswords(null);
        System.out.println(ANSI_GREEN + "Success" + ANSI_RESET);
    }

    /** HASH STRING **/
    private String hashMasterKey(String masterPassword) {
        return this.hashString(masterPassword);
    }

    /** READ FROM FILE **/
    private String readMasterPasswordFromFile() throws Exception {
        File passwordFile = new File(this.masterKey.getMasterPasswordPath());
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

    /** CHECK **/
    public boolean checkMasterKey(String masterPassword) throws Exception{
        if(new File(this.masterKey.getMasterPasswordPath()).exists() == false){return false;}
        return this.readMasterPasswordFromFile().equals(hashString(masterPassword));
    }

    /** SAVE PW **/
    private void StoreMasterPasswordToFile(String masterPassword) throws Exception{
        FileWriter writer = null;
        try {
            // PATH
            writer = new FileWriter(this.masterKey.getMasterPasswordPath());
            // Speichert Master KEY in den PATH
            writer.write(masterPassword);
        } finally {
            if(writer != null) try {
                writer.flush();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /** DELETE ALL PW **/
    private void destroyPasswordFile() {
      File file = new File(this.passwordPath);
      if(file.exists()){
          file.delete();
      }
    }
}
