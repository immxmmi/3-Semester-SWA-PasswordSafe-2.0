package password;

import crypto.Cryptography;

public interface Password {
    String getPlain();

    void setPlain(String plain);

    String getName();

    void setName(String name);

    String getCategory();

    void setCategory(String category);

    Cryptography getCryptography();

    void setCryptography(Cryptography cryptography);

}
