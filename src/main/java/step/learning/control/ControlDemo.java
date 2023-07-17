package step.learning.control;

public class ControlDemo {
    public void run(){
        System.out.println("ControlDemo run");
        new HomeWork1().print();
        //Примитиви
        byte xb;
        short xs;
        int xi;
        long xl;
        float xf;
        double xd;
        boolean xbo;
        char cx;
        //З примитивами можуть вмникнути проблеми при створенні колекцій
        //Посилальні типи
        Byte b = 1;
        Short s = 2;
        Integer i = 3;
        Long l = 4L;
        Float f = 0.01F;
        Double d = 0.01;
        Boolean bo = true;
        Character x = 'A';
        //Масиви
        int arr1[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int arr2[] = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int arr3[]= new int[5];
        /*
        * Control flow instructions - шнструкції управлінням виконанням
        * операторів умовного та ціклічного виконання, а також переходів між інструкціями
        *  */
        // String
        String str1 = "Hello";  // String pool - одне значення стає
        String str2 = "Hello";  // одним об'єктом
        String str3 = new String("Hello");
        String str4 = new String("Hello");
        if( str1 == str2 ) {  // через пул рядків це насправді один і той самий об'єкт
            System.out.println("str1 == str2");
        }
        else {
            System.out.println("str1 != str2");
        }
        if( str3 == str4 ) {  // об'єкти рівні тільки якщо це один об'єкт (за посиланням)
            System.out.println("str3 == str4");
        }
        else {
            System.out.println("str3 != str4");
        }
        if( str3.equals( str4 ) ) {  // порівняння за контентом
            System.out.println("str3 equals str4");
        }
        else {
            System.out.println("str3 !equals str4");
        }
    }
}
