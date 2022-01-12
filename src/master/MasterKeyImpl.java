package master;

import crypto.Cryptography;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import password.IPassword;

import java.util.ArrayList;


@Builder (toBuilder = true)
public class MasterKeyImpl implements MasterKey {

    /** Master VAR **/
    @Getter @Setter @Builder.Default
    private String masterPasswordPath = "/master.pw";
    @Getter @Setter
    private String masterPasswordPlain;
    @Getter @Setter @Builder.Default
    private ArrayList<IPassword> passwords = null;
    @Getter @Setter @Builder.Default
    private Cryptography cryptography = Cryptography.SHA256;


}
