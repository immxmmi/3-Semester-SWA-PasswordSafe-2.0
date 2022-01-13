package utility;

import utility.Config;

import java.io.IOException;

public interface ConfigServices {
    Config loadConfigFile() throws IOException;
    Config configBuilder(String root, String masterFolder, String masterFileName, String passwordFolder, String passwordFileName);
}
