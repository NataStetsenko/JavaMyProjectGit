package step.learning.files;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import step.learning.oop.*;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;


import java.lang.reflect.Method;


public class GsonHomeWork {
    private List<Literature> funds;

    public GsonHomeWork() {
        funds = new ArrayList<>();
        funds.add(new Book("Art of Programming", "D. Knuth"));
        funds.add(new Journal("Nature", 123));
        funds.add(new Newspaper("Dayle Mail", new Date(2020 - 1900, 9 - 1, 10)));
        funds.add(new Hologram("Pectoral", new Date(1995 - 1900, 3 - 1, 07)));
        funds.add(new Hologram("Yaroslav the Wise", new Date(1990 - 1900, 11 - 1, 17)));
        funds.add(new Hologram("Frame the Gospel", new Date(1991 - 1900, 6 - 1, 23)));
        funds.add(new Hologram("Ornaments of Kyivan Rus", new Date(1994 - 1900, 12 - 1, 27)));
        funds.add(new Booklet("Kyivan Rus", "VisaviPrint"));
        funds.add(new Poster("Harry Potter"));
        funds.add(new Poster("Jack Sparrow"));
    }

    public JSONArray myJSONArray() {
        JSONArray jsonArray = new JSONArray();
        Gson gson = new GsonBuilder().create();
        for (Literature literature : funds) {
            JSONObject obj = new JSONObject();
            obj.put("type", literature.getClass().toString()).put("jsonElement", gson.toJson(literature));
            jsonArray.put(obj);
        }
        return jsonArray;
    }

    public void save() {
        try (FileOutputStream fos = new FileOutputStream("./gson.txt");) {
            fos.write(myJSONArray().toString().getBytes());
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public List<Literature> load() {
        List<Literature> fundsResult = new ArrayList<>();
        String name = "gson.txt";
        Gson gson = new GsonBuilder().create();
        try {
            JSONArray jsonArray = readJSONArrayFromFile(name);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String className = object.getString("type").substring(6);
                try {
                    Type myType = Class.forName(className);
                    Object obj = gson.fromJson(object.getString("jsonElement"), myType);
                    fundsResult.add((Literature) obj);
                } catch (ReflectiveOperationException e) {
                    System.out.println("Error creating instance: " + e.getMessage());
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return fundsResult;
    }

    public void run() {
        save();
        List<Literature> fundsResult = load();
        for (Literature literature : fundsResult) {
            System.out.println(literature.getCard());
        }
    }

    /* try {
        Class<?> bookClass = Class.forName(className);
        Constructor<?> constructor = bookClass.getConstructor();            //конструктор пустой
        constructor.setAccessible(true);                                    // для приватного конструктора
        Object book = constructor.newInstance();
        try {                                                                //методы
            Method someMethod = bookClass.getMethod("setAuthor", String.class);
            someMethod.invoke(book,"42");
            Method someMethod2 = bookClass.getMethod("setTitle", String.class);
            someMethod2.invoke(book,"44");
            fundsResult.add((Literature) book);
        } catch (NoSuchMethodException e) {
            System.out.println("Method not found: someMethod");
        }
    } catch (ReflectiveOperationException e) {
        System.out.println("Error creating instance: " + e.getMessage());
    }*/
    public void run2() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<LiteratureDeserializer> literatureDeserializers = new ArrayList<>();
        for (Literature literature : funds) {
            int count = literature.getClass().toString().lastIndexOf('.');
            String temp = literature.getClass().toString().substring(count + 1);
            String temp2 = gson.toJson(literature);
            literatureDeserializers.add(new LiteratureDeserializer(temp, temp2));
        }
        System.out.println(gson.toJson(literatureDeserializers));
        String json = gson.toJson(literatureDeserializers);
        List<Literature> fundsResult = new ArrayList<>();
        List<LiteratureDeserializer> literatureDeserializers2 = gson.fromJson(json, new TypeToken<List<LiteratureDeserializer>>() {
        }.getType());
        for (LiteratureDeserializer literature : literatureDeserializers2) {
            switch (literature.type) {
                case ("Book"):
                    Book book = gson.fromJson(literature.jsonElement, Book.class);
                    fundsResult.add(book);
                    break;
                case ("Newspaper"):
                    Newspaper newspaper = gson.fromJson(literature.jsonElement, Newspaper.class);
                    fundsResult.add(newspaper);
                    break;
                case ("Booklet"):
                    Booklet booklet = gson.fromJson(literature.jsonElement, Booklet.class);
                    fundsResult.add(booklet);
                    break;
                case ("Hologram"):
                    Hologram hologram = gson.fromJson(literature.jsonElement, Hologram.class);
                    fundsResult.add(hologram);
                    break;
                case ("Journal"):
                    Journal journal = gson.fromJson(literature.jsonElement, Journal.class);
                    fundsResult.add(journal);
                    break;
                case ("Poster"):
                    Poster poster = gson.fromJson(literature.jsonElement, Poster.class);
                    fundsResult.add(poster);
                    break;
            }
        }
        for (Literature literature : fundsResult) {
            System.out.println(literature.getCard());
        }
    }

    public static JSONArray readJSONArrayFromFile (String name) throws IOException, JSONException{
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(name))) {
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
       /* catch (IOException | JSONException e){
            e.printStackTrace();
        }*/
        String jsonString = stringBuilder.toString();
        return new JSONArray(jsonString);
    }
}



