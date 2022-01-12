package modle;

import crypto.Cryptography;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import master.MasterKey;
import password.Password;

import java.util.ArrayList;


@Builder (toBuilder = true)
public class MasterKeyImpl implements MasterKey {

    /** Master VAR **/
    @Getter @Setter @Builder.Default
    private String masterPasswordPath = "/master.pw";
    @Getter @Setter
    private String masterPasswordPlain;
    @Getter @Setter @Builder.Default
    private ArrayList<Password> passwords = null;
    @Getter @Setter @Builder.Default
    private Cryptography cryptography = Cryptography.SHA256;


}
