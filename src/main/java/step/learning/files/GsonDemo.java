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
import java.lang.reflect.Modifier;
import java.text.DateFormat;


public class GsonDemo {
            public void run() {
                Gson gson2 = new GsonBuilder()    // Builder - створює серіалізатор із додатковими налаштуваннями
                        .serializeNulls()         // значення null будуть додаватись до результату
                        .disableHtmlEscaping()    // відключення екранування HTML тегів
                        .setPrettyPrinting()      // форматування виведення - відступи та переводи рядків
                        .serializeSpecialFloatingPointValues()   // включення спец.значень на кшталт Infinity
                        .excludeFieldsWithModifiers(Modifier.ABSTRACT)   // зміна поведінки - замість ігнорування усіх модифікаторів, буде ігноруватись лише ABSTRACT (а STATIC - не буде)
                        .setDateFormat(DateFormat.MILLISECOND_FIELD)  // .LONG (Jul 20, 2023 11:51:04 AM)
                        .create() ;               //
                DataObject2 data = new DataObject2() ;
                data.setField1( 10 ) ;
                DataObject2.staticField = 20 ;
                System.out.println( gson2.toJson( data ) ) ;

                data.setField1( Double.POSITIVE_INFINITY ) ;
                System.out.println( gson2.toJson( data ) ) ;  // IllegalArgumentException: Infinity is not a valid double value
            }
            public void run3() {
                DataObject data = new DataObject()
                        .setField1("Value 1")    // fluid interface - всі методи повертають об'єкт
                        .setField2("Value 2") ;  // це дозволяє створювати ланцюг ініціалізації
                Gson gson = new Gson() ;
                System.out.printf( "%s  --  %s%n",
                        data, gson.toJson( data ) ) ;   // {"field1":"Value 1","field2":"Value 2"}
                data.setField1( null ) ;
                System.out.printf( "%s  --  %s%n",
                        data,                     // field1 = 'null', field2 = 'Value 2'
                        gson.toJson( data ) ) ;   // {"field2":"Value 2"} - null не потрапляє у JSON

                // перевіряємо, чи можлива десеріалізація з частковим зазначенням полів
                DataObject data2 = gson.fromJson( "{\"field2\":\"Value 2\"}", DataObject.class ) ;
                System.out.printf( "%s  --  %s%n",
                        data2, gson.toJson( data2 ) ) ;  // field1 = 'null', field2 = 'Value 2'  --  {"field2":"Value 2"}

                data.setField1( "<h1>Hello</h1>" ) ;  // JSON передбачає екранування певних символів, зокрема тегів

                Gson gson2 = new GsonBuilder()    // Builder - створює серіалізатор із додатковими налаштуваннями
                        .serializeNulls()         // значення null будуть додаватись до результату
                        .disableHtmlEscaping()    // відключення екранування HTML тегів
                        .setPrettyPrinting()      // форматування виведення - відступи та переводи рядків
                        .create() ;               //

                System.out.printf( "%s  --  %s%n", data2, gson2.toJson( data2 ) ) ;  // {"field1":null,"field2":"Value 2"}
                System.out.printf( "%s  --  %s%n", data, gson.toJson( data ) ) ;     // {"field1":"\u003ch1\u003eHello\u003c/h1\u003e","field2":"Value 2"}
                System.out.printf( "%s  --  %s%n", data, gson2.toJson( data ) ) ;    // {"field1":"<h1>Hello</h1>","field2":"Value 2"}
            }

            public void run2() {
                System.out.println( "Gson demo" ) ;
                Gson gson = new Gson() ;
                Book book = new Book( "Art of Programming", " D. Knuth" ) ;
                System.out.println( gson.toJson( book ) ) ;
                String json = "{\"author\":\" D. Knuth\",\"title\":\"Art of Programming\"}" ;
                Book book2 = gson.fromJson( json, Book.class ) ;
                System.out.println( book2.getCard() ) ;

                try( FileWriter writer = new FileWriter("book.txt") ) {
                    writer.write( json ) ;
                }
                catch( IOException ex ) {
                    System.err.println( ex.getMessage() ) ;
                }

                try {
                    FileReader reader = new FileReader("book.txt") ;
                    Book book3 = gson.fromJson( reader, book.getClass() ) ;
                    reader.close() ;
                    System.out.println( book3.getCard() ) ;
                }
                catch( IOException ex ) {
                    System.err.println( ex.getMessage() ) ;
                }
            }
        }
/*
 Залежності Maven на прикладі Gson
Maven має свій репозиторій - колекцію додаткових пакетів класів - mvnrepository.com
На цьому сайті знаходимо шуканий пакет, обираємо версію (як правило останню), копіюємо
інструкцію залежності, наприклад
    <!-- https://mvnrepository.com/artifact/com.google.code.gson/gson -->
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>

Вставляємо ці інструкції у файл pom.xml у секцію <dependencies>
Після чого необхідно оновити-завантажити залежності
або) з'являється кнопка M (Maven) при будь-яких змінах у pom.xml
або) відкриває панель (інструмент) Maven і натискаємо Reload

Сама функціональність - це бібліотека класів (.JAR) - аналог DLL
Вона завантажується у локальний проєкт і включається до компіляції
Також вона має бути у Production коді (у деплої)
 */
