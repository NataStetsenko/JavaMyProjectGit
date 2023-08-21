package step.learning.ioc;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.WhirlpoolDigest;
import org.bouncycastle.util.encoders.Hex;
public class Whirlpool implements MyHash{
    @Override
    public void transformation(String text) {
        try {
            Digest digest = new WhirlpoolDigest();
            byte[] data = text.getBytes();
            digest.update(data, 0, data.length);
            byte[] output = new byte[digest.getDigestSize()];
            digest.doFinal(output, 0);
            String whirlpoolHex = new String(Hex.encode(output));
            System.out.println("Whirlpool -> " + whirlpoolHex);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
