package step.learning.files;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.*;
import java.util.Scanner;


public class FileHomeWork {
public void pathToDir(){
    System.out.println("Введіть шлях до файлу.");
    Scanner scanner = new Scanner(System.in) ;
    String s = scanner.nextLine();
    File currentDir = new File(s);
  /*  File currentDir = new File("D:\\Working folder\\Java\\Новая папка");*/
    StringBuffer sb = new StringBuffer();
    searchResult(currentDir, sb);
}
    public void searchResult(File currentDir, StringBuffer sb) {
        if (currentDir.isDirectory()) {
            File[] files = currentDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        Path filePath = file.toPath();
                        try {
                            BasicFileAttributes attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
                            sb.append(String.format("%s %-10s %-60s %-10s", attributes.creationTime(), "<DIR>", file.getAbsolutePath(), file.getName()));
                            sb.append("\n");
                        } catch (Exception e) {
                            System.err.println(e);
                        }
                        currentDir = new File(filePath.toString());
                        searchResult(currentDir, sb);
                    } else if (file.isFile()) {
                        try {
                            Path filePath = file.toPath();
                            BasicFileAttributes attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
                            sb.append(String.format("%s %-10s %-60s %-10s", attributes.creationTime(), attributes.size(), file.getAbsolutePath(), file.getName()));
                            sb.append("\n");
                        } catch (Exception e) {
                            System.err.println(e);
                        }
                    }
                }
            }
        }
        try (FileOutputStream fos = new FileOutputStream("./dir.txt");) {
            fos.write(sb.toString().getBytes());
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
