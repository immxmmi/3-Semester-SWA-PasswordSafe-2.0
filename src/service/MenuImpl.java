package service;

import utility.ConfigServices;
import utility.Config;
import utility.MasterKey;
import utility.Menu;
import modle.MasterKeyImpl;
import utility.MasterKeyServices;
import utility.PasswordServices;
import tools.TextColor;
import tools.Tools;

import java.io.IOException;
import java.util.Scanner;

public class MenuImpl extends Tools implements Menu {

    /** config **/

    private static Config config;
    private static ConfigServices configServices = new ConfigServicesImpl();

    static {
        try {
            config = configServices.loadConfigFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** VAR **/
    private static String passwordFileName = config.getPasswordFileName();
    private static String masterFileName = config.getMasterFileName();

    /** INSTANCE **/
    private static PasswordServices passwordServices = new PasswordServicesImpl();
    private static MasterKeyServices masterKeyServices = new MasterKeyServicesImpl(masterFileName, passwordServices.getFullPath());
    private static MasterKey masterKey = MasterKeyImpl.builder().build();
    private static TextColor textColor = new TextColor();

    public MenuImpl() {
    }


    /** PRINTER **/
    private void printMenu(){
        System.out.println(textColor.BLUE + "Enter master (1), show all (2), show single (3), add (4), delete(5), update(6), set new master (7), Abort (0)" + textColor.RESET);
    }

    private boolean login() throws Exception {
        Scanner read = new Scanner(System.in);
        System.out.println(textColor.PURPLE + "Enter master password" + textColor.RESET);
        String masterKey = read.next().replaceAll("\n","");
        boolean locked = !masterKeyServices.checkMasterKey(masterKey);
        if(!locked){
            this.masterKey.setMasterPasswordPlain(masterKey);
            this.passwordServices = new PasswordServicesImpl(this.passwordFileName,this.masterKey);
            this.passwordServices.reloadData();
            System.out.println(textColor.GREEN + "unlocked success" + textColor.RESET);
        }else{
            System.out.println(textColor.RED + "Master password did not match or don't exist! Failed to unlock." + textColor.RESET);
        }
        return locked;
    }

    @Override
    public void startMenu() throws Exception {

        boolean abort = false;
        boolean locked = true;
        Scanner read = new Scanner(System.in);

        while(!abort){
            printMenu();
            int input = read.nextInt();
            switch (input){
                case 0:{abort = true; break;}
                /** ENTER MASTER KEY **/
                case 1:{locked = login();break;}
                /** PRINT PW-LIST **/
                case 2: {
                    if (locked) {
                        System.out.println(textColor.RED + "Please unlock first by entering the master password." + textColor.RESET);
                    } else {
                        this.masterKey = passwordServices.reloadData();
                        passwordServices.printPasswordList(this.masterKey.getPasswords());
                    }break;
                }
                /** PRINT PW **/
                case 3: {
                    if (locked) {
                        System.out.println(textColor.RED + "Please unlock first by entering the master password." + textColor.RESET);
                    } else {
                         passwordServices.printSinglePassword();
                    }
                    break;
                }
                /**  CREATE NEW PW **/
                case 4: {
                    if (locked) {
                        System.out.println(textColor.RED + "Please unlock first by entering the master password." + textColor.RESET);
                    } else {
                        PasswordServicesImpl passwordServicesImpl = new PasswordServicesImpl(passwordFileName,masterKey);
                        passwordServicesImpl.addNewPasswordMenu();
                    }
                    break;
                }
                /** DELETE PW **/
                case 5: {
                    if (locked) {
                        System.out.println(textColor.RED + "Please unlock first by entering the master password." + textColor.RESET);
                    } else {
                        System.out.println("Enter password name");
                        this.passwordServices.deletePasswordMenu();
                    }
                    break;
                }
                /** UPDATE PW **/
                case 6: {
                    if (locked) {
                        System.out.println(textColor.RED + "Please unlock first by entering the master password." + textColor.RESET);
                    } else {
                        this.passwordServices.updatePasswordMenu();
                    }
                    break;
                }
                /** CREATE SET NEW MASTER KEY**/
                case 7:{
                    locked = true;
                    this.masterKeyServices = new MasterKeyServicesImpl(this.masterFileName, passwordServices.getFullPath());
                    this.masterKeyServices.setNewMasterKey();
                    this.masterKey.setPasswords(null);
                    break;
                }
                /** INVALID INPUT **/
                default: System.out.println(textColor.RED + "Invalid input" + textColor.RESET);
            }
        }
    }

}
