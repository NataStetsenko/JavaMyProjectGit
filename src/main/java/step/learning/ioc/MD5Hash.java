package step.learning.ioc;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Scanner;

public class MD5Hash implements MyHash{
    @Override
    public void transformation(String text) {
        try{ MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
            byte[] digest = md.digest();
            // Преобразуем байты хеш-значения в строку шестнадцатеричного представления
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b & 0xff));
            }
            System.out.println("MD5 -> " + hexString.toString());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
