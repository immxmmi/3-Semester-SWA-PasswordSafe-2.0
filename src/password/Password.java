package password;

import crypto.Cryptography;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder(toBuilder = true)
public class Password implements IPassword {
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
