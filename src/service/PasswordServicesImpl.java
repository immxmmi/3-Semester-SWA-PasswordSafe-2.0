package service;

import utility.ConfigServices;
import utility.Config;
import crypto.CipherFacilityImpl;
import crypto.Cryptography;
import utility.PasswordServices;
import utility.FileHandler;
import utility.MasterKey;
import utility.Password;
import modle.PasswordImpl;
import tools.TextColor;
import tools.Tools;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;


public class PasswordServicesImpl extends Tools implements PasswordServices {

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
    private static String root = config.getRoot() + config.getPasswordFolder();
    /** VAR **/
    public static String fullPath = root + config.getPasswordFileName();

    /** INSTANCE **/
    private CipherFacilityImpl cipherFacilityImpl;
    private FileHandler handler = new FileHandlerImpl();
    private MasterKey masterKey;
    private TextColor textColor;

    /** STATIC **/
    static {new File(root).mkdir();}

    /** CONSTRUCTOR **/
    public  PasswordServicesImpl(){};
    public PasswordServicesImpl(String fileName, MasterKey masterKey) {
        this.textColor = new TextColor();
        this.masterKey = masterKey;
        this.cipherFacilityImpl = CipherFacilityImpl.builder()
                .key(masterKey.getMasterPasswordPlain())
                .cryptography(Cryptography.AES)
                .build();
        this.fullPath =this.root + fileName;
    }

    /** GETTER **/
    public String getFullPath() {
        return fullPath;
    }
    private Password getPasswordByID(int id) throws IOException {
        return loadPasswordList().get(id);
    }

    /** PRINT **/
    @Override
    public void printPasswordList(ArrayList<Password> passwords) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        int counter = 0;
        System.out.println(" ____________________________________________________________________________________________________________________________");
        System.out.println("|ID   | Category           | Name           | Crypto           | Password                                                    |");
        if(passwords == null){
            System.out.println(textColor.RED + "NO PASSWORDS AVAILABLE" + textColor.RESET);
            return;
        }

        for(Password password : passwords){
            this.printPassword(counter,password);
            counter++;
        }
        System.out.println("|____________________________________________________________________________________________________________________________|");
    }
    @Override
    public void printSinglePassword() throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        if(this.masterKey.getPasswords() == null){
            System.out.println(textColor.RED + "NO PASSWORDS AVAILABLE" +textColor.RESET);
            return;
        }

        int index = this.getInputID(this.masterKey.getPasswords().size()-1,"PW - ID ?","PASSWORD NOT AVAILABLE - TRY AGAIN");

        System.out.println(" ____________________________________________________________________________________________________________________________");
        System.out.println("|ID   | Category           | Name           | Crypto           | Password                                                    |");

        Password password = getPasswordByID(index);

       if(password == null){
            System.out.println(textColor.RED + "PASSWORD NOT AVAILABLE" + textColor.RESET);
        }else{
            this.printPassword(index,password);
        }
        System.out.println("|____________________________________________________________________________________________________________________________|");
}
    private void printPassword(int index, Password password) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        int maxNr = 3;
        int maxCategory = 18;
        int maxName = 14;
        int maxCrypto = 16;

        System.out.println("|-----|--------------------|----------------|------------------|-------------------------------------------------------------|");
        System.out.print(textColor.BLUE +"| "+index+this.checkSpace(""+index,maxNr));
        System.out.print("| "+ password.getCategory()+this.checkSpace(password.getCategory(),maxCategory));
        System.out.print("| "+ password.getName()+this.checkSpace(password.getName(),maxName));
        System.out.print("| "+ password.getCryptography()+this.checkSpace(""+ password.getCryptography(),maxCrypto));
        System.out.print("| "+this.cipherFacilityImpl.Decrypt(password.getPlain()));
        System.out.println(textColor.RESET);
    }

    /** INPUT **/
    private int getInputID(int max, String quest, String errorMsg){
            Scanner read = new Scanner(System.in);
            int input = 0;
        do{
            if(input < 0 || input > max){
                System.out.println(textColor.RED + errorMsg + textColor.RESET);
            }
            System.out.println(quest);
            input = Integer.parseInt(String.valueOf(read.nextInt()));

        }while(input < 0 || input > max);


        return input;
    }

    /** BUILD **/
    private Password buildPassword(String category, String name, Cryptography cryptography, String password){
        return PasswordImpl.builder()
                .name(name)
                .Category(category)
                .cryptography(cryptography)
                .plain(password)
                .build();
    }

    /** UPDATE **/ 
    @Override
    public void updatePasswordMenu() throws Exception {
        if(this.masterKey.getPasswords() == null){
            System.out.println(textColor.RED + "NO PASSWORD AVAILABLE" + textColor.RESET);
            return;
        }
        int index = this.getInputID(this.masterKey.getPasswords().size()-1,"PW - ID ?","PASSWORD NOT AVAILABLE - TRY AGAIN");

        ArrayList<Password> PasswordList =  this.masterKey.getPasswords();
        Password password = this.masterKey.getPasswords().get(index);
        Password newPassword = updatePassword(password);
        PasswordList.add(newPassword);
        deletePasswordByID(index);
        this.updatePasswordList();
    }
    private void printUpdateMenu(String category,String name, String pw, Cryptography cryptography){
        System.out.println("###############################");
        System.out.println("# - CHANGE PASSWORD - MENU - ##");
        System.out.println("# 1. Category:     " + category);
        System.out.println("# 2. Name:         " + name);
        System.out.println("# 3. Password:     " + pw);
        System.out.println("# 4. Cryptography: " + cryptography);
        System.out.println("# 0. Back          ");
        System.out.println("##############################");
    }
    private Password updatePassword(Password password) throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        String category = password.getCategory();
        String name = password.getName();
        String pw = this.cipherFacilityImpl.Decrypt(password.getPlain());
        Cryptography cryptography = password.getCryptography();

        String cryptInput = "";
        this.printUpdateMenu(category,name, pw, cryptography);

        int input = getInputID(5,"SELECT:","ERROR - TRY AGAIN");

        Scanner read = new Scanner(System.in);

        switch (input) {
            case 1: System.out.println("Set new category: ");category = read.nextLine();break;
            case 2: System.out.println("Set new name: ");name = read.nextLine();break;
            case 3: System.out.println("Set new password: ");pw = read.nextLine();break;
            case 4: {System.out.println("Set new cryptography: AES (1) ");
                do {
                    cryptInput = read.nextLine().replaceAll(" ", "").replaceAll("\n", "");
                    if (!cryptInput.equals("1")) {System.out.println(textColor.RED + "Invalid input!" + textColor.RESET);}
                } while (!cryptInput.equals("1"));

                switch (cryptInput) {
                    case "1": cryptography = Cryptography.AES;
                    default: cryptography = Cryptography.AES;
                }
            }
            case 0: System.out.println("Back");break;
            default: System.out.println(textColor.RED + "Invalid input!" + textColor.RESET);
        }

            password.setName(name);
            password.setCryptography(cryptography);
            password.setCategory(category);
            password.setPlain(this.cipherFacilityImpl.HashText(password.getCryptography(), pw));

            return password;
    }

    /** ADD  **/
    @Override
    public void addNewPasswordMenu() throws Exception {
        Scanner read = new Scanner(System.in);
        System.out.println("Create NEW PW");
        System.out.println(" - CATEGORY - ");
        String category = read.nextLine();
        System.out.println(" - NAME - ");
        String name = read.nextLine();
        System.out.println(" - PASSWORD -");
        String password = read.nextLine();
        System.out.println("- CRYPTOGRAPHY -");
        System.out.println("AES (1) ");

        String cryptInput= "";
        do{
            cryptInput = read.nextLine().replaceAll(" ","").replaceAll("\n","");
            if(!cryptInput.equals("1")){
                System.out.println(textColor.RED + "Invalid input!" + textColor.RESET);
            }
        }while(!cryptInput.equals("1"));
        Cryptography crypt = null;
        switch (cryptInput){
            case "1": crypt = Cryptography.AES;
            default: crypt = Cryptography.AES;
        }

        this.addNewPassword(buildPassword(category,name,crypt,this.cipherFacilityImpl.HashText(crypt,password)));
    }
    private void addNewPassword(Password newPassword) throws Exception {
        this.addNewPasswordToFile(convertPasswordToString(newPassword, handler.countLine(this.fullPath)),this.fullPath);
    }
    private File addNewPasswordToFile(String password, String path) throws IOException {
        File file = new File(path);

        if (!file.exists()) {
            file.createNewFile();
            handler.writeToFile(" __________________________________________________________________________________________________________________________",path);
            handler.writeToFile("|ID | Category           | Name           | Crypto           | Password                                                    |",path);
            handler.writeToFile("|---|--------------------|----------------|------------------|-------------------------------------------------------------|",path);
            handler.writeToFile(password,path);
        }else{
            file.createNewFile();
            handler.writeToFile(password,path);
        }
        return file;
    }

    /** CONVERT **/
    private String convertPasswordToString(Password newPassword, int number) {
        int maxNr = 1;
        int maxCategory = 17;
        int maxName = 13;
        int maxCrypto = 15;


        return "|"+this.checkSpace(""+number,maxNr)+number+
               " |~"+newPassword.getCategory()+this.checkSpace(newPassword.getCategory(),maxCategory) +
               " |<"+newPassword.getName()+this.checkSpace(newPassword.getName(),maxName) +
               " |>"+ newPassword.getCryptography()+this.checkSpace(""+newPassword.getCryptography(),maxCrypto) +
               " |#"+ newPassword.getPlain();
    }

    /** DELETE **/
    @Override
    public void deletePasswordMenu() throws Exception {
    loadPasswordList();
        System.out.println("DELETE - MENU");
        if(this.masterKey.getPasswords() == null){
            System.out.println(textColor.RED + "NO PASSWORD AVAILABLE" + textColor.RESET);
            return;
        }
        int index = this.getInputID(this.masterKey.getPasswords().size()-1,"PW - ID ?","PASSWORD NOT AVAILABLE - TRY AGAIN");
        deletePasswordByID(index);
    }
    private void deletePasswordByID(int index) throws Exception {
        ArrayList<Password> newPasswordList = this.masterKey.getPasswords();
        newPasswordList.remove(index);
        this.masterKey.setPasswords(newPasswordList);
        updatePasswordList();
    }


    /** PASSWORD LIST **/
    @Override
    public MasterKey reloadData() throws IOException {
        this.loadPasswordList();
        return this.masterKey;
    }
    @Override
    public void updatePasswordList() throws IOException {
        File oldFile = new File(this.getFullPath());
        if(oldFile.exists()){
            oldFile.delete();
        }
        if(masterKey.getPasswords()==null){
            return;
        }

        File newFile = new File(this.fullPath);
        int count = 0;
        for(Password password : this.masterKey.getPasswords()){
            addNewPasswordToFile(convertPasswordToString(password,count),newFile.getPath());
            count ++;
        }
        this.loadPasswordList();
    }
    private ArrayList<Password> loadPasswordList() throws IOException {
        File file = new File(this.fullPath);
        if(!file.exists()){return null;}

        ArrayList<Password> PWList = new ArrayList<>();
        BufferedReader reader;
        reader = new BufferedReader(new FileReader(this.fullPath));

        reader.readLine();
        reader.readLine();
        reader.readLine();


        String line = reader.readLine();
        String password = "";
        String name = "";
        String category = "";
        Cryptography crypto = null;


        while(line != null){
            if(line.contains("#") && line.contains(">") && line.contains("<")){
                password = line.split("#")[1].replaceAll(" ","");
                line = line.split("#")[0].replaceAll(" ","");
                crypto = Cryptography.valueOf(line.split(">")[1].replace("|",""));
                line = line.split(">")[0];
                name = line.split("<")[1].replace("|","");
                line = line.split("<")[0];
                category = line.split("~")[1].replace("|","");
                PWList.add(this.buildPassword(category,name,crypto,password));
            }

            line = reader.readLine();
        }
        reader.close();

        this.masterKey.setPasswords(PWList);
        return PWList;
    }

}
