package step.learning.db;

import java.util.Random;

public class RandomValue {

    public Integer randomInteger(){
        Random random = new Random();
        return random.nextInt(100);
    }
    public Float randomFloat(){
        Random random = new Random();
        return random.nextFloat()*(100.0F);
    }

    public String randomString() {
        StringBuilder sb = new StringBuilder(25);
        Random random = new Random();
        String charArray = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 25; i++) {
            int index = random.nextInt(charArray.length());
            char randomChar = charArray.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
