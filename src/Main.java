import menu.IMenu;
import menu.Menu;

import java.io.File;

public class Main {

    /** ROOT **/
    static final private String root = "./KEY";

    /** STATIC **/
    static {new File(root).mkdir();}

    public static void main(String [] args) throws Exception {

        System.out.println("Welcome to Passwordsafe");
        IMenu menu = new Menu();
        menu.startMenu();
        System.out.println("Good by !");
    }

}
