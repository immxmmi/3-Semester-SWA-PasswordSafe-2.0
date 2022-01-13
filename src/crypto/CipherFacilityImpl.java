package crypto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import utility.CipherFacility;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Builder (toBuilder = true)
public class CipherFacilityImpl implements CipherFacility {
    @Getter @Setter
    private String key;
    @Getter @Setter @Builder.Default
    private Cryptography cryptography = Cryptography.AES;


    @Override
    public String HashText(Cryptography cryptography, String plain) throws UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        switch (cryptography){
            case AES -> {
                return this.Encrypt(plain);
            }
            case SHA256 -> {
                return this.hashString(plain);
            }
        }
        return null;
    }
    @Override
    public String Decrypt(String decrypted) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(
                Cipher.DECRYPT_MODE,
                this.GetAESFormattedKeyFrom(this.key));
        byte[] tmp = this.GetByteArrayFromString(decrypted);
        byte[] plain = cipher.doFinal(this.GetByteArrayFromString(decrypted));
        return new String(plain);
    }
    private String Encrypt(String plain) throws UnsupportedEncodingException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(
                Cipher.ENCRYPT_MODE,
                this.GetAESFormattedKeyFrom(this.key));
        byte[] encrypted = cipher.doFinal(plain.getBytes());
        return this.GetStringFromByteArray(encrypted);
    }



    /**
     * HASH STRING
     ***********************************************/
    private static String toHexString(byte[] hash) {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }
    private String hashString(String text) {

        try {
            return toHexString(getSHA(text));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown for incorrect algorithm: " + e);
        }

        return text;
    }
    /**************************************************************/


    /** GETTER **/
    private static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }
    private SecretKeySpec GetAESFormattedKeyFrom(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] keyBytes = (key).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        keyBytes = sha.digest(keyBytes);
        keyBytes = Arrays.copyOf(keyBytes, 16);
        return new SecretKeySpec(keyBytes, "AES");
    }
    private String GetStringFromByteArray(byte[] source) {
        String tmp = "";
        for (int i = 0; i < source.length; i++) {
            tmp += source[i] + ",";
        }
        return tmp;
    }
    private byte[] GetByteArrayFromString(String source) {
        String[] bytesAsString = source.split(",");
        byte[] bytes = new byte[bytesAsString.length];
        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = Byte.parseByte(bytesAsString[i]);
        }
        return bytes;
    }
}
