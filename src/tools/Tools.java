package tools;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Tools {


    /**
     * HASH STRING
     ***********************************************/
    private static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

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

    public String hashString(String text) {

        try {
            return toHexString(getSHA(text));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown for incorrect algorithm: " + e);
        }

        return text;
    }

    /**************************************************************/


    protected String addSpace(int spaceSize) {
        String space = "";
        for (int i = 0; i <= spaceSize; i++) {
            space = space + " ";
        }
        return space;
    }

    protected String checkSpace(String word, int size) {
        int a = size - word.length();
        if (a < 0) {
            return "word too long";
        }
        return addSpace(a);
    }

}
