package password.services;

import crypto.CipherFacility;
import crypto.Cryptography;
import master.IMasterKey;
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
    public PasswordServices(String fileName, IMasterKey masterKey) {
        this.cipherFacility = CipherFacility.builder()
                .masterKey(masterKey.getMasterPasswordPlain())
                .cryptography(Cryptography.AES)
                .build();
        this.fullPath =this.root + fileName;

    }

    /** PRINT **/
    /**
    public void printPasswordList() throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        File file = new File(this.fullPath);
        if(!file.exists()){
            System.out.println(ANSI_RED + "NO PASSWORD" + ANSI_RESET);
            return;
        }

        BufferedReader reader;
        reader = new BufferedReader(new FileReader(this.fullPath));


        System.out.println(reader.readLine());
        System.out.println(reader.readLine());
        System.out.println(reader.readLine());
        String line = reader.readLine();
        String text = "";
        String password = "";
        while(line != null){
            text = line.split("#")[0];
            password = this.cipherFacility.Decrypt(line.split("#")[1]);
            System.out.println(text+password);

            line = reader.readLine();
        }
        reader.close();
        System.out.println("");
        System.out.println("");

    }
**/
    public void printPasswordList() throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        int maxNr = 1;
        int maxCategory = 18;
        int maxName = 14;
        int maxCrypto = 16;
        System.out.println(" __________________________________________________________________________________________________________________________");
        System.out.println("|ID | Category           | Name           | Crypto           | Password                                                    |");
        for(int i = 0; i < getAllPW().size(); i++){
            System.out.println("|---|--------------------|----------------|------------------|-------------------------------------------------------------|");
            System.out.print("| "+i+this.checkSpace(""+i,maxNr));
            System.out.print("| "+getAllPW().get(i).getCategory()+this.checkSpace(getAllPW().get(i).getCategory(),maxCategory));
            System.out.print("| "+getAllPW().get(i).getName()+this.checkSpace(getAllPW().get(i).getName(),maxName));
            System.out.print("| "+getAllPW().get(i).getCryptography()+this.checkSpace(""+getAllPW().get(i).getCryptography(),maxCrypto));
            System.out.print("| "+this.cipherFacility.Decrypt(getAllPW().get(i).getPlain()));
        }
        System.out.println("\n|__________________________________________________________________________________________________________________________|");



    }


    /** CREATE **/
    public void createNewPassword() throws Exception {
        Scanner read = new Scanner(System.in);
        System.out.println("Create NEW PW");
        System.out.println(" - CATEGORY - ");
        String category = read.nextLine();
        System.out.println(" - NAME - ");
        String name = read.nextLine();
        System.out.println(" - PASSWORD -");
        String password = read.nextLine();
        IPassword newPassword = Password.builder()
                .Category(category)
                .name(name)
                .plain(this.cipherFacility.HashText(Cryptography.AES,password))
                .cryptography(Cryptography.AES)
                .build();
        this.addNewPassword(newPassword);
    }

    private IPassword buildPassword(String category, String name, Cryptography cryptography, String password){
        return Password.builder()
                .name(name)
                .Category(category)
                .cryptography(cryptography)
                .plain(password)
                .build();
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


    /** UPDATE **/

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

        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    /** DELETE **/
    public void DeletePasswordByID(String passwordID) throws Exception {

    }

    /** UPDATE ID **/
    /** GETTER **/
    private File GetFileFromName(String name) {
        return new File(fullPath + "/" + name + ".pw");
    }
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
}
