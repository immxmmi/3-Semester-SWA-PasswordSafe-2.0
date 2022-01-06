import menu.IMenu;
import menu.Menu;

import java.io.File;

public class Main {

    /** ROOT **/
    static final private String root = "./KEY";

    /** STATIC **/
    static {new File(root).mkdir();}

    public static void main(String [] args) throws Exception {

       // File file = new File("./KEY/password/password.txt");
       // File file1 = new File()
        System.out.println("Welcome to Passwordsafe");
        IMenu menu = new Menu();
        menu.startMenu();
        System.out.println("Good by !");
    }

}
