package step.learning.files;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileDemo {
    public void run() {
        System.out.println("Files");
        dirDemo();
        ioDemo();
    }

    /**
     * Демонстрація роботи з файловою системою
     */
    private void dirDemo() {        // File (java.io) - основний клас
        File currentDir =           // для роботи як з файлами, так і
                new File(           // папками. !! створення new File
                        "./");    // не впливає на ОС, лише створює об'єкт

        System.out.printf("File '%s' ", currentDir.getName());

        if (currentDir.exists()) {
            System.out.printf("exists %n");  // %n -> std::endl, \n -> один символ
        } else {
            System.out.printf("does not exist %n");
        }

        if (currentDir.isDirectory()) {
            System.out.println("Is Directory");
            File[] files = currentDir.listFiles();
            if (files != null) for (File file : files) {
                System.out.printf(
                        "%s\t%s %n",
                        file.getName(),
                        file.isDirectory() ? "<DIR>" : "file"
                );
            }
        } else {
            System.out.printf("Is File: %s%s%s %n",
                    currentDir.canRead() ? "r" : "R",     // 1  1
                    currentDir.canWrite() ? "w" : "W",    // 1  0
                    currentDir.canExecute() ? "x" : "X"   // 1  1
            );                                           // 7  5
        }
    }
    /**
     * Демонстрація введення/виведення з файлами
     */
    private void ioDemo() {
        String fileContent = "This is content of a file\n" +
                "This is a new line" ;
        String filename = "test-file.txt" ;
        try( FileWriter writer = new FileWriter( filename ) ) {
            writer.write( fileContent ) ;
            System.out.println(  "Write success" ) ;
        }
        catch( IOException ex ) {
            System.err.println( ex.getMessage() ) ;
        }
        System.out.println(  "Reading...." ) ;
        try( FileReader reader = new FileReader( filename ) ;
             Scanner scanner = new Scanner( reader ) ) {
            while( scanner.hasNext() ) {
                System.out.println( scanner.nextLine() ) ;
            }
        }
        catch( IOException ex ) {
            System.err.println( ex.getMessage() ) ;
        }
    }
}
/*
Робота з файлами
поділяється на дві групи:
 1. Робота з файловою системою - копіювання, видалення, створення файлів,
     пошук, тощо
 2. Використання файлів для збереження даних

 1 - див. dirDemo()
 2 - особливіть Java - наявність великої кількості засобів роботи з потоками (stream)
 InputStream - абстракція, що поєднує усі "читальні" класи
 FileReader - потокове читання по символах
 FileInputStream - --//-- по байтах
 BufferedReader - "оболонка", яка утворює проміжний буфер, який зменшує
  кількість прямих операцій читання з потоку
 Scanner - оболонка для читання різних типів даних

 OutputStream
 FileWriter
 FileOutputStream
 BufferedWriter
 PrintWriter - оболонка, яка надає засоби форматованого "друку" (переводить
  різні типи даних у символи для потоку)

 (int32)127 ->
  (bin) 01111111 00000000 00000000 00000000
  (txt) 00110001 00110010 00110111
          '1'      '2'      '7'

 try-with-resource:
 try( Resource res = new Resource() ) {  -- замість using (C#) -- AutoClosable
 }
 catch() {
 }*/
