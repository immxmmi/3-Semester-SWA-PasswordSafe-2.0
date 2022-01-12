package menu;

import master.MasterKey;
import master.MasterKeyImpl;
import master.services.MasterKeyServices;
import master.services.MasterKeyServicesImpl;
import password.services.PasswordServicesImpl;
import tools.TextColor;
import tools.Tools;

import java.util.Scanner;

public class MenuImpl extends Tools implements Menu {


    /** VAR **/
    private static String passwordFileName = "password.pw";
    private static String masterFileName = "master.pw";

    /** INSTANCE **/
    private static PasswordServicesImpl passwordServicesImpl;
    private static MasterKeyServices masterKeyServices = new MasterKeyServicesImpl(masterFileName, passwordServicesImpl.getFullPath());
    private static MasterKey masterKey = MasterKeyImpl.builder().build();
    private static TextColor textColor = new TextColor();





    /** PRINTER **/
    private void printMenu(){
        System.out.println(textColor.ANSI_BLUE + "Enter master (1), show all (2), show single (3), add (4), delete(5), update(6), set new master (7), Abort (0)" + textColor.ANSI_RESET);
    }

    private boolean login() throws Exception {
        Scanner read = new Scanner(System.in);
        System.out.println(textColor.ANSI_PURPLE + "Enter master password" + textColor.ANSI_RESET);
        String masterKey = read.next().replaceAll("\n","");
        boolean locked = !masterKeyServices.checkMasterKey(masterKey);
        if(!locked){
            this.masterKey.setMasterPasswordPlain(masterKey);
            this.passwordServicesImpl = new PasswordServicesImpl(this.passwordFileName,this.masterKey);
            this.passwordServicesImpl.loadPasswords();
            System.out.println(textColor.ANSI_GREEN + "unlocked success" + textColor.ANSI_RESET);
        }else{
            System.out.println(textColor.ANSI_RED + "Master password did not match or don't exist! Failed to unlock." + textColor.ANSI_RESET);
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
                        System.out.println(textColor.ANSI_RED + "Please unlock first by entering the master password." + textColor.ANSI_RESET);
                    } else {
                        passwordServicesImpl.printPasswordList();
                        //passwordServices.printPasswordList();
                    }break;
                }
                /** PRINT PW **/
                case 3: {
                    if (locked) {
                        System.out.println(textColor.ANSI_RED + "Please unlock first by entering the master password." + textColor.ANSI_RESET);
                    } else {
                        passwordServicesImpl.printSinglePassword();
                    }
                    break;
                }
                /**  CREATE NEW PW **/
                case 4: {
                    if (locked) {
                        System.out.println(textColor.ANSI_RED + "Please unlock first by entering the master password." + textColor.ANSI_RESET);
                    } else {
                        PasswordServicesImpl passwordServicesImpl = new PasswordServicesImpl(passwordFileName,masterKey);
                        passwordServicesImpl.createNewPasswordMenu();
                    }
                    break;
                }
                /** DELETE PW **/
                case 5: {
                    if (locked) {
                        System.out.println(textColor.ANSI_RED + "Please unlock first by entering the master password." + textColor.ANSI_RESET);
                    } else {
                        System.out.println("Enter password name");
                        this.passwordServicesImpl.deletePasswordMenu();
                    }
                    break;
                }
                /** UPDATE PW **/
                case 6: {
                    if (locked) {
                        System.out.println(textColor.ANSI_RED + "Please unlock first by entering the master password." + textColor.ANSI_RESET);
                    } else {
                        this.passwordServicesImpl.updatePassword();
                    }
                    break;
                }
                /** CREATE SET NEW MASTER KEY**/
                case 7:{
                    locked = true;
                    this.masterKeyServices = new MasterKeyServicesImpl(this.masterFileName,this.passwordServicesImpl.getFullPath());
                    this.masterKeyServices.setNewMasterKey();
                    this.masterKey.setPasswords(null);
                    break;
                }
                /** INVALID INPUT **/
                default: System.out.println(textColor.ANSI_RED + "Invalid input" + textColor.ANSI_RESET);
            }
        }
    }

}
