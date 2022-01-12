import menu.Menu;
import menu.MenuImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.jar.JarOutputStream;

public class Main {

    /** ROOT **/
    static final private String root = "./KEY";

    /** STATIC **/
    static {new File(root).mkdir();}

    public static void main(String [] args) throws Exception {

       System.out.println("Welcome to Passwordsafe");
        Menu menu = new MenuImpl();
        menu.startMenu();
        System.out.println("Good by !");

    }

}
