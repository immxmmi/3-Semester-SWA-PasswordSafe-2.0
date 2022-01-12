package password.services;

import crypto.CipherFacility;
import crypto.Cryptography;
import master.MasterKey;
import password.IPassword;
import password.Password;
import tools.Tools;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class PasswordServices extends Tools{

    /** ROOT **/
    private static String root = "./KEY/password/";
    /** VAR **/
    private final CipherFacility cipherFacility;
    private static String fullPath = "";

    /** STATIC **/
    static {new File(root).mkdir();}

    /** CONSTRUCTOR **/
    public PasswordServices(String fileName, MasterKey masterKey) {
        this.cipherFacility = CipherFacility.builder()
                .masterKey(masterKey.getMasterPasswordPlain())
                .cryptography(Cryptography.AES)
                .build();
        this.fullPath =this.root + fileName;
    }

    /** PRINT **/
    public void printPasswordList() throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        int maxNr = 1;
        int maxCategory = 18;
        int maxName = 14;
        int maxCrypto = 16;
        System.out.println(" __________________________________________________________________________________________________________________________");
        System.out.println("|ID | Category           | Name           | Crypto           | Password                                                    |");

        if(getAllPW() != null){
            for(int i = 0; i < getAllPW().size(); i++){
                System.out.println("|---|--------------------|----------------|------------------|-------------------------------------------------------------|");
                System.out.print(ANSI_BLUE+"| "+i+this.checkSpace(""+i,maxNr));
                System.out.print("| "+getAllPW().get(i).getCategory()+this.checkSpace(getAllPW().get(i).getCategory(),maxCategory));
                System.out.print("| "+getAllPW().get(i).getName()+this.checkSpace(getAllPW().get(i).getName(),maxName));
                System.out.print("| "+getAllPW().get(i).getCryptography()+this.checkSpace(""+getAllPW().get(i).getCryptography(),maxCrypto));
                System.out.print("| "+this.cipherFacility.Decrypt(getAllPW().get(i).getPlain()));
                System.out.println(ANSI_RESET);
            }
        }
        System.out.println("|__________________________________________________________________________________________________________________________|");

    }
    public void printSinglePassword() throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        int index = getInputID();

        if(getAllPW() == null){
            return;
        }
        if(index < 0 || index > getAllPW().size()){
            System.out.println(ANSI_RED + "PW - NOT FOUND" + ANSI_RESET);
            return;
        }


        int maxNr = 1;
        int maxCategory = 18;
        int maxName = 14;
        int maxCrypto = 16;
        System.out.println(" __________________________________________________________________________________________________________________________");
        System.out.println("|ID | Category           | Name           | Crypto           | Password                                                    |");

        System.out.println("|---|--------------------|----------------|------------------|-------------------------------------------------------------|");
        System.out.print(ANSI_BLUE+"| "+index+this.checkSpace(""+index,maxNr));
        System.out.print("| "+getAllPW().get(index).getCategory()+this.checkSpace(getAllPW().get(index).getCategory(),maxCategory));
        System.out.print("| "+getAllPW().get(index).getName()+this.checkSpace(getAllPW().get(index).getName(),maxName));
        System.out.print("| "+getAllPW().get(index).getCryptography()+this.checkSpace(""+getAllPW().get(index).getCryptography(),maxCrypto));
        System.out.print("| "+this.cipherFacility.Decrypt(getAllPW().get(index).getPlain()));
        System.out.println(ANSI_RESET);
        System.out.println("|__________________________________________________________________________________________________________________________|");


    }

    /** INPUT **/
    private int getInputID(){
        System.out.println("PW - ID ?");
        Scanner read = new Scanner(System.in);
        return read.nextInt();
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
                System.out.println(ANSI_RED + "Invalid input!" + ANSI_RESET);
            }
        }while(!cryptInput.equals("1"));
        Cryptography crypt = null;
        switch (cryptInput){
            case "1": crypt = Cryptography.AES;
            default: crypt = Cryptography.AES;
        }

        this.addNewPassword(buildPassword(category,name,crypt,this.cipherFacility.HashText(crypt,password)));
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
        int index = this.getInputID();
        this.addNewPassword(this.updatePasswordMenu(index));
        deletePasswordByID(index);

    }
    private IPassword updatePasswordMenu(int index) throws IOException {
        IPassword password = getPasswordByID(index);
        System.out.println("Change PW");
        System.out.println("1. Category: " + password.getCategory());
        System.out.println("2. Name: " + password.getName());
        System.out.println("3. Password: " + password.getPlain());
        System.out.println("4. Cryptography: " + password.getCryptography());
        System.out.println("0. Back ");
        Scanner read = new Scanner(System.in);

        String line = "";

        do{
            line = read.nextLine().replaceAll(" ","").replaceAll("\n","");

            if(!line.equals("0")&&!line.equals("1")&&!line.equals("2")&&!line.equals("3")&&!line.equals("4")){
                System.out.println(ANSI_RED + "Invalid input!" + ANSI_RESET);
            }

        }while(line.equals("0")||line.equals("1")|| line.equals("2")|| line.equals("3")|| line.equals("4"));

        switch (line){
            case "0": ;break;
            case "1": System.out.println("Set new category: "); String input = read.nextLine();password.setCategory(input);break;
            case "2": System.out.println("Set new name: "); password.setName(read.nextLine());break;
            case "3": System.out.println("Set new password: "); password.setPlain(read.nextLine());break;
            case "4": System.out.println("Set new cryptography: "); password.setCryptography(Cryptography.valueOf(read.nextLine()));break;
        }

        return password;
    } // TODO: 06.01.2022 EINGABE PROBLEME

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
        System.out.println("DELETE - MENU");
        int index = getInputID();

        if(getAllPW() == null){return;}

        if(index < 0 || index > getAllPW().size()){return;}
        deletePasswordByID(index);
    }
    private void deletePasswordByID(int index) throws Exception {

        File currentFile = new File("./KEY/password/password.txt");
        if(!currentFile.exists()){System.out.println(ANSI_RED + "NO PASSWORD" + ANSI_RESET);return;}
        File rename = new File("./KEY/password/password1.txt");

        currentFile.renameTo(rename);
        File newFile = new File("./KEY/password/password.txt");
        newFile.createNewFile();


        BufferedReader reader = new BufferedReader(new FileReader(rename));
        copyFile(reader.readLine(),newFile);
        copyFile(reader.readLine(),newFile);
        copyFile(reader.readLine(),newFile);

        String line = reader.readLine();
        String text = "";
        while(line != null){
            text = line;
            text = text.split("#")[0].replaceAll(" ","");
            text = text.split(">")[0];
            text = text.split("<")[0];

            if(!text.contains(""+index)){
                copyFile(line,newFile);
            }

            line = reader.readLine();
        }
        reader.close();

        if(rename.exists()){
            System.out.println("rename");
        }

        if(currentFile.exists()){
            System.out.println("currentFile");
        }
        if(newFile.exists()){
            System.out.println("newFile");
        }

       // currentFile.delete();

    }

    /** COPY FILE - DELETE **/
    private void copyFile(String line,File newFile) throws IOException {
        FileWriter fileWriter = new FileWriter(newFile,true);
        fileWriter.write(line);
        fileWriter.write("\n");
        fileWriter.flush();
    }
    private boolean renameCopyFile(String oldFileName){
       String copy = this.root + "copy.pw";
       File file = new File(copy);
       File rename = new File(oldFileName);
       return file.renameTo(rename);
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

    /** GETTER **/
    public String[] GetStoredPasswords() throws Exception{
        File directory = new File(fullPath);
        if (!directory.isDirectory() && !directory.mkdir()) {
            throw new Exception("Unable to create directory");
        }


        List<File> files = Arrays.asList(directory.listFiles());

        return files.stream()
                .filter(s -> s.getName().endsWith(".pw"))
                .map(f -> f.getName().split("\\.")[0])
                .collect(Collectors.toList()).toArray(new String[0]);
    }
    public static String getRoot() {
        return root;
    }
    private File GetFileFromName(String name) {
        return new File(fullPath + "/" + name + ".pw");
    }



    private IPassword getPasswordByID(int id) throws IOException {
        return getAllPW().get(id);
    }
    private ArrayList<IPassword> getAllPW() throws IOException {
        File file = new File(this.fullPath);
        if(!file.exists()){
            System.out.println(ANSI_RED + "NO PASSWORD" + ANSI_RESET);
            return null;
        }

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
            password = line.split("#")[1].replaceAll(" ","");
            line = line.split("#")[0].replaceAll(" ","");
            crypto = Cryptography.valueOf(line.split(">")[1].replace("|",""));
            line = line.split(">")[0];
            name = line.split("<")[1].replace("|","");
            line = line.split("<")[0];
            category = line.split("~")[1].replace("|","");

            PWList.add(this.buildPassword(category,name,crypto,password));


            // System.out.println(text+password);

            line = reader.readLine();
        }
        reader.close();

        return PWList;
    }

}
