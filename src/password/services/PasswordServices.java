package password.services;

import master.MasterKey;
import password.Password;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public interface PasswordServices {

    String getFullPath();

    void printPasswordList(ArrayList<Password> passwords) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;

    void printSinglePassword() throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;

    void updatePasswordMenu() throws Exception;

    void addNewPasswordMenu() throws Exception;

    void deletePasswordMenu() throws Exception;

    MasterKey reloadData() throws IOException;

    void updatePasswordList() throws IOException;
}
