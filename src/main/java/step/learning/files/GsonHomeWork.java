package step.learning.files;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import step.learning.oop.*;
import java.util.*;

public class GsonHomeWork {
    private List<Literature> funds;
    public GsonHomeWork() {
        funds = new ArrayList<>();
        funds.add( new Book("Art of Programming", "D. Knuth"));
        funds.add(new Journal("Nature", 123));
        funds.add(new Newspaper("Dayle Mail", new Date(2020 -1900,9-1,10)));
        funds.add(new Hologram("Pectoral", new Date(1995 - 1900, 3 - 1, 07)));
        funds.add(new Hologram("Yaroslav the Wise", new Date(1990 - 1900, 11 - 1, 17)));
        funds.add(new Hologram("Frame the Gospel", new Date(1991 - 1900, 6 - 1, 23)));
        funds.add(new Hologram("Ornaments of Kyivan Rus", new Date(1994 - 1900, 12 - 1, 27)));
        funds.add(new Booklet("Kyivan Rus", "VisaviPrint"));
        funds.add(new Poster("Harry Potter"));
        funds.add(new Poster("Jack Sparrow"));
    }
    public void run(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create() ;
        List<LiteratureDeserializer> literatureDeserializers = new ArrayList<>();
        for (Literature literature : funds) {
            int count = literature.getClass().toString().lastIndexOf('.');
            String temp = literature.getClass().toString().substring(count+1);
            String temp2 = gson.toJson(literature);
            literatureDeserializers.add(new LiteratureDeserializer(temp,temp2));
        }
        System.out.println(gson.toJson(literatureDeserializers));
        String json = gson.toJson(literatureDeserializers);
        List<Literature> fundsResult = new ArrayList<>();
        List<LiteratureDeserializer> literatureDeserializers2 = gson.fromJson(json,new TypeToken<List<LiteratureDeserializer>>(){}.getType());
        for (LiteratureDeserializer literature : literatureDeserializers2) {
            switch (literature.type){
                case ("Book"):
                    Book book = gson.fromJson( literature.jsonElement, Book.class );
                    fundsResult.add(book);
                    break;
                case ("Newspaper"):
                    Newspaper newspaper = gson.fromJson( literature.jsonElement, Newspaper.class );
                    fundsResult.add(newspaper);
                    break;
                case ("Booklet"):
                    Booklet booklet = gson.fromJson( literature.jsonElement, Booklet.class );
                    fundsResult.add(booklet);
                    break;
                case ("Hologram"):
                    Hologram hologram = gson.fromJson( literature.jsonElement, Hologram.class );
                    fundsResult.add(hologram);
                    break;
                case ("Journal"):
                    Journal journal = gson.fromJson( literature.jsonElement, Journal.class );
                    fundsResult.add(journal);
                    break;
                case ("Poster"):
                    Poster poster = gson.fromJson( literature.jsonElement, Poster.class );
                    fundsResult.add(poster);
                    break;
            }
        }
        for (Literature literature : fundsResult) {
            System.out.println(literature.getCard());
        }
    }
}
