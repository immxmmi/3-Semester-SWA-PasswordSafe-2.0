package master;

import crypto.Cryptography;
import password.Password;

import java.util.ArrayList;

public interface MasterKey {
    String getMasterPasswordPath();

    String getMasterPasswordPlain();

    Cryptography getCryptography();

    public ArrayList<Password> getPasswords();

    public void setPasswords(ArrayList<Password> passwords);

    void setMasterPasswordPath(String masterPasswordPath);

    void setMasterPasswordPlain(String masterPasswordPlain);

    void setCryptography(Cryptography cryptography);
}
