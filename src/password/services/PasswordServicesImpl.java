package password.services;

import crypto.CipherFacilityImpl;
import crypto.Cryptography;
import master.MasterKey;
import password.IPassword;
import password.Password;
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


public class PasswordServicesImpl extends Tools implements PasswordServices{

    /** ROOT **/
    private static String root = "./KEY/password/";
    /** VAR **/
    private static String fullPath = "./KEY/password/password.pw";

    /** INSTANCE **/
    private final CipherFacilityImpl cipherFacilityImpl;
    private MasterKey masterKey;
    private TextColor textColor;

    /** STATIC **/
    static {new File(root).mkdir();}

    /** CONSTRUCTOR **/
    public PasswordServicesImpl(String fileName, MasterKey masterKey) {
        this.textColor = new TextColor();
        this.masterKey = masterKey;
        this.cipherFacilityImpl = CipherFacilityImpl.builder()
                .key(masterKey.getMasterPasswordPlain())
                .cryptography(Cryptography.AES)
                .build();
        this.fullPath =this.root + fileName;
    }

    /** PRINT **/
    public void printPasswordList() throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
       loadPasswords();
        int maxNr = 1;
        int maxCategory = 18;
        int maxName = 14;
        int maxCrypto = 16;
        int counter = 0;


        System.out.println(" __________________________________________________________________________________________________________________________");
        System.out.println("|ID | Category           | Name           | Crypto           | Password                                                    |");
        if(this.masterKey.getPasswords() == null){
            System.out.println(textColor.ANSI_RED + "NO PASSWORD AVAILABLE" + textColor.ANSI_RESET);
            return;
        }

        for(IPassword password : this.masterKey.getPasswords()){

            System.out.println("|---|--------------------|----------------|------------------|-------------------------------------------------------------|");
            System.out.print(textColor.ANSI_BLUE+"| "+counter+this.checkSpace(""+counter,maxNr));
            System.out.print("| "+ password.getCategory()+this.checkSpace(password.getCategory(),maxCategory));
            System.out.print("| "+ password.getName()+this.checkSpace(password.getName(),maxName));
            System.out.print("| "+ password.getCryptography()+this.checkSpace(""+ password.getCryptography(),maxCrypto));
            System.out.print("| "+this.cipherFacilityImpl.Decrypt(password.getPlain()));
            System.out.println(textColor.ANSI_RESET);

            counter++;
        }
        System.out.println("|__________________________________________________________________________________________________________________________|");

    }
    public void printSinglePassword() throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        int maxNr = 1;
        int maxCategory = 18;
        int maxName = 14;
        int maxCrypto = 16;
        if(this.masterKey.getPasswords() == null){
            System.out.println(textColor.ANSI_RED + "NO PASSWORD AVAILABLE" +textColor.ANSI_RESET);
            return;
        }
        int index = this.getInputID(this.masterKey.getPasswords().size()-1);

        System.out.println(" __________________________________________________________________________________________________________________________");
        System.out.println("|ID | Category           | Name           | Crypto           | Password                                                    |");
        IPassword password = masterKey.getPasswords().get(index);

        System.out.println("|---|--------------------|----------------|------------------|-------------------------------------------------------------|");
        System.out.print(textColor.ANSI_BLUE+"| "+index+this.checkSpace(""+index,maxNr));
        System.out.print("| "+ password.getCategory()+this.checkSpace(password.getCategory(),maxCategory));
        System.out.print("| "+ password.getName()+this.checkSpace(password.getName(),maxName));
        System.out.print("| "+ password.getCryptography()+this.checkSpace(""+ password.getCryptography(),maxCrypto));
        System.out.print("| "+this.cipherFacilityImpl.Decrypt(password.getPlain()));
        System.out.println(textColor.ANSI_RESET);
        System.out.println("|__________________________________________________________________________________________________________________________|");
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

    /** INPUT **/
    private int getInputID(int max){
            Scanner read = new Scanner(System.in);
            int input = 0;
        do{
            System.out.println("PW - ID ?");
            input = Integer.parseInt(String.valueOf(read.nextInt()));

        }while(input < 0 || input > max);


        return input;
    }
    public static String getFullPath() {
        return fullPath;
    }

    /** CREATE **/
    public void createNewPasswordMenu() throws Exception {
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
                System.out.println(textColor.ANSI_RED + "Invalid input!" + textColor.ANSI_RESET);
            }
        }while(!cryptInput.equals("1"));
        Cryptography crypt = null;
        switch (cryptInput){
            case "1": crypt = Cryptography.AES;
            default: crypt = Cryptography.AES;
        }

        this.addNewPassword(buildPassword(category,name,crypt,this.cipherFacilityImpl.HashText(crypt,password)));
    }
    /** BUILD **/
    private IPassword buildPassword(String category, String name, Cryptography cryptography, String password){
        return Password.builder()
                .name(name)
                .Category(category)
                .cryptography(cryptography)
                .plain(password)
                .build();
    }

    /** UPDATE **/ 
    public void updatePassword() throws Exception {
        if(this.masterKey.getPasswords() == null){
            System.out.println(textColor.ANSI_RED + "NO PASSWORD AVAILABLE" + textColor.ANSI_RESET);
            return;
        }
        int index = this.getInputID(this.masterKey.getPasswords().size()-1);


        ArrayList<IPassword> PasswordList =  this.masterKey.getPasswords();
        IPassword password = this.masterKey.getPasswords().get(index);
        IPassword newPassword = updatePasswordMenu(password);
        PasswordList.add(newPassword);
        deletePasswordByID(index);
        this.updatePasswordList();
    }
    private IPassword updatePasswordMenu(IPassword password) throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        String category = password.getCategory();
        String name = password.getName();
        String pw = this.cipherFacilityImpl.Decrypt(password.getPlain());
        Cryptography cryptography = password.getCryptography();

        this.printUpdateMenu(category,name, pw,cryptography);



        Scanner read = new Scanner(System.in);
        String line = "";
        do {
            line = read.nextLine().replaceAll(" ", "").replaceAll("\n", "");

            if (!line.equals("0") && !line.equals("1") && !line.equals("2") && !line.equals("3") && !line.equals("4")) {
                System.out.println(textColor.ANSI_RED + "Invalid input!" + textColor.ANSI_RESET);
            }

        } while (line.equals("0") || line.equals("1") || line.equals("2") || line.equals("3") || line.equals("4"));


        switch (line) {
            case "0":
                System.out.println("Back");
                break;
            case "1":
                System.out.println("Set new category: ");
                category = read.nextLine();
                break;
            case "2":
                System.out.println("Set new name: ");
                name = read.nextLine();
                break;
            case "3":
                System.out.println("Set new password: ");
                pw = read.nextLine();
                break;
            case "4": {
                System.out.println("Set new cryptography: AES (1) ");

                String cryptInput = "";
                do {
                    cryptInput = read.nextLine().replaceAll(" ", "").replaceAll("\n", "");
                    if (!cryptInput.equals("1")) {
                        System.out.println(textColor.ANSI_RED + "Invalid input!" + textColor.ANSI_RESET);
                    }
                } while (!cryptInput.equals("1"));
                switch (cryptInput) {
                    case "1":
                        cryptography = Cryptography.AES;
                    default:
                        cryptography = Cryptography.AES;
                }
            }

        }
            name = "as";
            password.setName(name);
            password.setCryptography(cryptography);
            password.setCategory(category);
            password.setPlain(this.cipherFacilityImpl.HashText(password.getCryptography(), pw));
            return password;

    }

    /** ADD  **/
    private void addNewPassword(IPassword newPassword) throws Exception {
        this.addNewPasswordToFile(convertPasswordToString(newPassword, countLine(this.fullPath)),this.fullPath);
    }
    private File addNewPasswordToFile(String password, String path) throws IOException {
        File file = new File(path);

        if (!file.exists()) {
            file.createNewFile();
            this.writeToFile(" __________________________________________________________________________________________________________________________",path);
            this.writeToFile("|ID | Category           | Name           | Crypto           | Password                                                    |",path);
            this.writeToFile("|---|--------------------|----------------|------------------|-------------------------------------------------------------|",path);
            this.writeToFile(password,path);
        }else{
            file.createNewFile();
            this.writeToFile(password,path);
        }
        return file;
    }

    /** CONVERT **/
    private String convertPasswordToString(IPassword newPassword,int number) {
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
    public void deletePasswordMenu() throws Exception {
    loadPasswords();
        System.out.println("DELETE - MENU");
        if(this.masterKey.getPasswords() == null){
            System.out.println(textColor.ANSI_RED + "NO PASSWORD AVAILABLE" + textColor.ANSI_RESET);
            return;
        }
        int index = this.getInputID(this.masterKey.getPasswords().size()-1);
        deletePasswordByID(index);
    }
    private void deletePasswordByID(int index) throws Exception {
        ArrayList<IPassword> newPasswordList = this.masterKey.getPasswords();
        newPasswordList.remove(index);
        this.masterKey.setPasswords(newPasswordList);
        updatePasswordList();
    }

    /** COUNTER **/
    private int countLine(String path) throws FileNotFoundException {
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










    /** WRITE FILE **/
    private void writeToFile(String text,String path) {

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
    private IPassword getPasswordByID(int id) throws IOException {
        return loadPasswords().get(id);
    }
    public ArrayList<IPassword> loadPasswords() throws IOException {
        File file = new File(this.fullPath);
        if(!file.exists()){return null;}

        ArrayList<IPassword> PWList = new ArrayList<>();
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
    public void updatePasswordList() throws IOException {
        File oldFile = new File(this.getFullPath());
        if(oldFile.exists()){
            oldFile.delete();
        }
        if(masterKey.getPasswords()==null){
            return;
        }

        File newFile = new File("./KEY/password/password.pw");
        int count = 0;
        for(IPassword password : this.masterKey.getPasswords()){
            addNewPasswordToFile(convertPasswordToString(password,count),newFile.getPath());
            count ++;
        }
        this.loadPasswords();
    }

}
