package modle;

import crypto.Cryptography;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import password.Password;


@Builder(toBuilder = true)
public class PasswordImpl implements Password {
    @Getter
    @Setter
    private String plain;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    @Builder.Default
    private String Category = "Home";
    @Getter
    @Setter
    @Builder.Default
    private Cryptography cryptography = Cryptography.AES;

}
