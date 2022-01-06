package master;

import crypto.Cryptography;

public interface IMasterKey {
    String getMasterPasswordPath();

    String getMasterPasswordPlain();

    Cryptography getCryptography();

    void setMasterPasswordPath(String masterPasswordPath);

    void setMasterPasswordPlain(String masterPasswordPlain);

    void setCryptography(Cryptography cryptography);
}
