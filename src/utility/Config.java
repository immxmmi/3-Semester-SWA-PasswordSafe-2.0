package utility;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Config {

    void setRoot(String root);

    void setMasterFolder(String masterFolder);

    void setMasterFileName(String masterFileName);

    void setPasswordFolder(String passwordFolder);

    void setPasswordFileName(String passwordFileName);

    String getRoot();

    String getMasterFolder();

    String getMasterFileName();

    String getPasswordFolder();

    String getPasswordFileName();
}
