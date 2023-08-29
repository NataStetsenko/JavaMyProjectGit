package step.learning;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import step.learning.control.ControlDemo;
import step.learning.db.DbDemo;
import step.learning.files.FileDemo;
import step.learning.files.FileHomeWork;
import step.learning.files.GsonDemo;
import step.learning.files.GsonHomeWork;
import step.learning.ioc.ConfigModule;
import step.learning.ioc.IocApp;
import step.learning.oop.Library;
import step.learning.treading.PercentDemo;
import step.learning.treading.PercentHomework;
import step.learning.treading.PercentHomework2;
import step.learning.treading.ThreadDemo;

import java.io.File;
import java.util.Scanner;

/*Java ВступJava - ООП мова програмування, на сьогодні курується OracleМова типу "транслятор"
- компілюється у байт-код (проміжнийкод), який виконується спеціальною платформою (JRE -Java Runtime Environment)
або JVM (Virtual Machine)Ця платформа встановлюється як окреме ПЗ. Для перевіркиможна виконати у терміналі
командуjava -versionУ Java гарна "зворотна" сумісність - старші платформи нормальновиконують код, створений
ранніми платформамиЄ визначна версія - Java8 (1.8), яка оновлюється, але немодифікується. На ній працює
більшість програмних комплексівтипу ЕЕJava SE (Standart Edition) - базовий набірJava EE (Enterprise Edition) -
 базовий набір + розширені засобиДля створення програм необхідний додатковий пакет -
 JDK (JavaDevelopment Kit)Після встановлення JRE, JDK встановлюємо IDE (Intellij Idea)
 Створюємо новий проєкт.Як правило, проєкти базуються на шаблонах, орієнтованих напростоту
 збирання проєкту - підключення додаткових модулів,формування команд компілятору та виконавцю,
 тощоПоширені системи - Maven, Gradle, Ant, IdeaПри створенні нового проєкту -
 вибираємо Maven Archetypeтип - org.apache.maven.archetypes:maven-archetype-quickstart

Після створення проєкту конфігуруємо запуск (від початку
налаштовано запуск Current File) - Edit Configuration -
- Create new - Application -- вводимо назву (Арр) та
вибираємо головний клас - Арр
У Java сувора прив'язка до структури файлів та папок
- папка - це package (пакет, аналог namespace)
   назва папки один-до-одного має збігатись із іменем
   пакету. Прийнято lowercase для назви пакетів
- файл - це клас. Обмеження - один файл - один public клас
   зауваження: в одному файлі може бути декілька класів,
    але лише один public, тобто видимий у цьому файлі
    а також є внутрішні (Nested) класи - класи у класах
   Назва класа має один-до-одного збігатись з іменем файла
   Для імен класів прийнято CapitalCamelCase
  */
public class App
{
    public static void main( String[] args )
    {
      /* new DbDemo().run();*/
         /* new GsonHomeWork().run();
       new GsonDemo().run();
        new FileHomeWork().pathToDir();
        new ControlDemo().run();
        new Library().showCatalog();
        new FileDemo().run();*/
        Injector injector = Guice.createInjector(
                // модулі конфігурації - довільна кількість
                new ConfigModule()
        ) ;
        injector.getInstance(
                // IocApp.class
                //  ThreadDemo.class
                //PercentDemo.class
                //   PercentHomework.class
               PercentHomework2.class
        ).run() ;  // Передача управління головному класу
    }
}
