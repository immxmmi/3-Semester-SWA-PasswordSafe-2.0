package master;

import crypto.Cryptography;
import password.IPassword;

import java.util.ArrayList;

public interface MasterKey {
    String getMasterPasswordPath();

    String getMasterPasswordPlain();

    Cryptography getCryptography();

    public ArrayList<IPassword> getPasswords();

    public void setPasswords(ArrayList<IPassword> passwords);

    void setMasterPasswordPath(String masterPasswordPath);

    void setMasterPasswordPlain(String masterPasswordPlain);

    void setCryptography(Cryptography cryptography);
}
