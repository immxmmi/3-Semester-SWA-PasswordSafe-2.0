package menu;

import master.IMasterKey;
import master.MasterKey;
import master.services.MasterKeyServices;
import password.services.PasswordServices;
import tools.Tools;

import java.io.File;
import java.util.Scanner;

public class Menu extends Tools implements IMenu{


    private static PasswordServices passwordServices;
    private static final MasterKeyServices masterKeyServices = new MasterKeyServices("master.pw",passwordServices.getRoot());
    private static IMasterKey masterKey = MasterKey.builder().build();


    /** PRINTER **/
    private void printMenu(){
        System.out.println("Enter master (1), show all (2), show single (3), add (4), delete(5), set new master (6), Abort (0)");
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
                case 1:{
                            System.out.println(ANSI_BLUE + "Enter master password" + ANSI_RESET);
                            String masterKey = read.next();
                            locked = !masterKeyServices.checkMasterKey(masterKey);
                            if(!locked){
                                this.masterKey.setMasterPasswordPlain(masterKey);
                                this.passwordServices = new PasswordServices("password.txt",this.masterKey);
                                System.out.println(ANSI_GREEN + "unlocked" + ANSI_RESET);
                            }else{
                                System.out.println(ANSI_RED + "master password did not match ! Failed to unlock." + ANSI_RESET);
                            }
                            break;
                }
                /** PRINT PW-LIST **/
                case 2: {
                    if (locked) {
                        System.out.println(ANSI_RED + "Please unlock first by entering the master password." + ANSI_RESET);
                    } else {
                        passwordServices.printPasswordList();
                        //passwordServices.printPasswordList();
                    }break;
                }
                /** PRINT PW **/
                case 3: {
                    if (locked) {
                        System.out.println("Please unlock first by entering the master password.");
                    } else {
                        //Arrays.stream(passwordSafeEngine.)
                    }
                    break;
                }
                /**  CREATE NEW PW **/
                case 4: {
                    if (locked) {
                        System.out.println("Please unlock first by entering the master password.");
                    } else {
                        PasswordServices passwordServices = new PasswordServices("password.txt",masterKey);
                        passwordServices.createNewPassword();
                    }
                    break;
                }
                /** DELETE PW **/
                case 5: {
                    if (locked) {
                        System.out.println("Please unlock first by entering the master password.");
                    } else {
                        System.out.println("Enter password name");
                        String passwordName = read.next();
                     //   this.passwordServices.DeletePassword(passwordName);
                    }
                    break;
                }
                /** CREATE SET NEW MASTER KEY**/
                case 6:{
                    locked = true;
                    this.passwordServices = null;
                    masterKeyServices.setNewMasterKey();
                    break;
                }
                /** INVALID INPUT **/
                default: System.out.println(ANSI_RED + "Invalid input" + ANSI_RESET);
            }
        }
    }

}
