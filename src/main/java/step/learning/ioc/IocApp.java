package step.learning.ioc;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IocApp {
  /*  @Inject
    private GreetingService helloService;
    @Inject @Named("bye")
    private PartingService byeService ;
    @Inject @Named("goodbye")
    private PartingService goodbyeService;*/
    private final GreetingService helloService ;
    private final GreetingService hiService ;
    private final PartingService byeService;
    private final PartingService goodbyeService ;
    private final MyHash md5Hash;
    private final MyHash sha256Hash;
    private final MyHash keccak;
    private final MyHash whirlpool;
    private final Random random;
    @Inject @Named("connect")// Можлива "змішана" інжекція - і через конструктор
    private String connect; // і через поля - обидві працюють одночасно
    @Inject @Named("logFile")
    private String logFile;
    @Inject @Named("java.util")
    private Random random2;
    private final Logger logger;
    @Inject
    public IocApp(@Named("hello")GreetingService helloService,
                  GreetingService hiService,
                  @Named("bye") PartingService byeService,
                  @Named("goodbye") PartingService goodbyeService,
                  @Named("md5") MyHash md5Hash,
                  @Named("sha256") MyHash sha256Hash,
                  @Named("keccak")MyHash keccak,
                  @Named("whirlpool") MyHash whirlpool,
                  @Named("java.util") Random random,
                  Logger logger) {
        this.helloService = helloService;
        this.hiService = hiService;
        this.byeService = byeService;
        this.goodbyeService = goodbyeService;
        this.md5Hash = md5Hash;
        this.sha256Hash = sha256Hash;
        this.keccak = keccak;
        this.whirlpool = whirlpool;
        this.random = random;
        this.logger = logger;
    }

    public void run(){
        NullExceptionByeService();
        NullExceptionGoodbyeService();
        NullExceptionHiService();
        NullExceptionHelloService();
        NullExceptionRandomService();
        NullExceptionConnectService();
        byeService.sayGoodbye();
        goodbyeService.sayGoodbye();
        helloService.sayHello();
        hiService.sayHello();

      /*  System.out.println(connect);
        System.out.println(logFile);
        System.out.println(random.nextInt());
        System.out.println(random.hashCode() + " " + random2.hashCode());*/

       /* logger.log(Level.INFO, "Logger INFO");
        logger.log(Level.WARNING, "Logger WARNING");
        logger.log(Level.SEVERE, "Logger SEVERE");*/


      /*  String text = getText();
        System.out.println("MD5 -> " + md5Hash.transformation(text));
        System.out.println("SHA-256 -> " + sha256Hash.transformation(text));
        System.out.println("Keccak-256 -> " + keccak.transformation(text));
        System.out.println("Whirlpool -> " +  whirlpool.transformation(text));*/

    }
    private String getText(){
        System.out.println("Enter the text:");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
    private void NullExceptionByeService(){

        if (byeService != null) {
            logger.log(Level.INFO, "Служба PartingService ініціалізована");

        } else {
            logger.log(Level.SEVERE, "Служба PartingService NULL");
        }
    }
    private void NullExceptionGoodbyeService(){

        if (goodbyeService != null) {
            logger.log(Level.INFO, "Служба PartingService ініціалізована");

        } else {
            logger.log(Level.SEVERE, "Служба PartingService NULL");
        }
    }
    private void NullExceptionHelloService(){

        if (helloService != null) {
            logger.log(Level.INFO, "Служба GreetingService ініціалізована");

        } else {
            logger.log(Level.SEVERE, "Служба GreetingService NULL");
        }
    }
    private void NullExceptionHiService(){

        if (hiService != null) {
            logger.log(Level.INFO, "Служба GreetingService ініціалізована");

        } else {
            logger.log(Level.SEVERE, "Служба GreetingService NULL");
        }
    }
    private void NullExceptionConnectService(){

        if (connect != null) {
            logger.log(Level.INFO, "Служба @Named: Connect ініціалізована");

        } else {
            logger.log(Level.SEVERE, "Служба @Named: Connect NULL");
        }
    }
    private void NullExceptionRandomService(){

        if (random != null) {
            logger.log(Level.INFO, "Служба Random ініціалізована");

        } else {
            logger.log(Level.SEVERE, "Служба Random NULL");
        }
    }
}
/*
Інверсія управління (Inversion of control), інжекція залежностей (DI - dependency injection)

Управління чим? Життєвим циклом об'єктів
- Без інверсії (звичайне управління)
   instance = new Type() - створення об'єкту
   instance = null  - "знищення" об'єкту - віддача до GC
- З інверсією
   service <- Type [Singleton]  (Реєстрація)
   @Inject instance  (Резолюція)  --- Засіб IoC надасть посилання на об'єкт

SOLID
O - open/close - доповнюй, але не змінюй
D - DIP dependency inversion (!не injection) principle
     не рекомендується залежність від конкретного типу,
     рекомендується - від інтерфейсу (абстрактного типу)
Приклад:
  розробляємо нову версію (покращуємо шифратор Cipher)
  - (не рекомендовано) - вносимо зміни у клас Cipher
  - (рекомендовано) - створюємо нащадка CipherNew та замінюємо
     залежності від Cipher на CipherNew
  = для спрощення другого етапу вживається DIP:
     замість того, щоб створювати залежність від класу (Cipher)
     private Cipher cipher ;
     бажано утворити інтерфейс та впроваджувати залежність через нього
     ICipher
     Cipher : ICipher           / new CipherNew()
     private ICipher cipher ;   \ new Cipher()
     а у IoC зазначаємо, що під інтерфейсом ICipher буде клас Cipher
      це значно спростить заміну нової реалізації на CipherNew і
      зворотні зміни

Техніка:
До проєкту додається інвертор (інжектор), наприклад, Google Guice або Spring
Стартова точка налаштовує сервіси, вирішує (resolve) перший клас
 (частіше за все Арр)
Інші класи замість створення нових об'єктів-служб вказують залежності
 від них

- Google Guice  - Maven залежність <!-- https://mvnrepository.com/artifact/com.google.inject/guice -->
- клас IocApp (цей файл)
- Main (див. його)
 */
/*
Принципи інжекції
а) якщо клас відомий у проєкті (є частиною проєкту), то інжектор може впраджувати
об'єкт даного класу без додаткових конфігурацій, достатньо додати анотацію @Inject
(приватні поля - інжектуються)
 недолік інжекції через поля - вони залишаються змінними (не константами),
 відповідно, можуть бути замінені навмисно або випадково
б) через конструктор - можлива ініціалізація незмінних (final) полів
 переваги
   - захист посилань на служби
   - захист від створення об'єкту без служб (наявність конструктора
      прибирає конструктор за замовчуванням)
*) правило інжекторів - якщо у класа є декілька конструкторів, то брати з
    найбільшою кількістю параметрів
 */