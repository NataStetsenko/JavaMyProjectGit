package step.learning.ioc;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.Security;

public class Keccak256 implements MyHash{
    @Override
    public String transformation(String text) {

        try {
            Security.addProvider(new BouncyCastleProvider());
            final MessageDigest digest = MessageDigest.getInstance("Keccak-256");
            final byte[] encodedhash = digest.digest(
                    text.getBytes(StandardCharsets.UTF_8));
            String sha3Hex = bytesToHex(encodedhash);
            return sha3Hex.toString();
            //System.out.println("Keccak-256 -> " + );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
