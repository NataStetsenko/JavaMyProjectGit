package step.learning.files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import step.learning.oop.Book;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

public class GsonDemo {
    public void run(){

        String json = "{\"brand\" : \"Toyota\", \"doors\" : 5}";
        JsonReader jsonReader = new JsonReader(new StringReader(json));
        try {
            while(jsonReader.hasNext()){
                JsonToken nextToken = jsonReader.peek();
                System.out.println(nextToken);

                if(JsonToken.BEGIN_OBJECT.equals(nextToken)){

                    jsonReader.beginObject();

                } else if(JsonToken.NAME.equals(nextToken)){

                    String name  =  jsonReader.nextName();
                    System.out.println(name);

                } else if(JsonToken.STRING.equals(nextToken)){

                    String value =  jsonReader.nextString();
                    System.out.println(value);

                } else if(JsonToken.NUMBER.equals(nextToken)){

                    long value =  jsonReader.nextLong();
                    System.out.println(value);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run2(){
        System.out.println("GsonDemo");
        Gson gson = new Gson();
        Book book = new Book("Art of Programming", "D. Knuth");
        System.out.println(gson.toJson(book));
        String json = "{\"author\":\"D. Knuth\",\"title\":\"Art of Programming\"}";
        Book book2 = gson.fromJson(json, Book.class);
        System.out.println(book2.getCard());
        try (FileWriter fileWriter = new FileWriter("book.txt")) {
            fileWriter.write(json);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        try (FileReader fileReader = new FileReader("book.txt")) {
            Book book3 = gson.fromJson(fileReader, Book.class);
            System.out.println(book3.getCard());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
