import service.ConfigServicesImpl;
import utility.Config;
import utility.Menu;
import service.MenuImpl;

import java.io.File;
import java.io.IOException;

public class Main {

    /** ROOT **/
    private static ConfigServicesImpl configServices = new ConfigServicesImpl();
    static Config config;
    static {
        try {
            config = configServices.loadConfigFile();
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
