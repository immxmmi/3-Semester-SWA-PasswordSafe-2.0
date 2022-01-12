import config.ConfigImpl;
import config.Config;
import menu.Menu;
import menu.MenuImpl;

import java.io.File;
import java.io.IOException;

public class Main {

    /** ROOT **/
    static Config config;
    static {
        try {
            config = new ConfigImpl(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static final private String root = config.getRoot();

    /** STATIC **/
    static {new File(root).mkdir();}

    public static void main(String [] args) throws Exception {
        System.out.println("Welcome to Passwordsafe");
        Menu menu = new MenuImpl();
        menu.startMenu();
        System.out.println("Good by !");
    }

}
