package step.learning.ioc;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.Scanner;

public class IocApp {
  /*  @Inject
    private GreetingService helloService;
    @Inject @Named("bye")
    private PartingService byeService ;
    @Inject @Named("goodbye")
    private PartingService goodbyeService ;*/
    private final GreetingService helloService ;
    private final PartingService byeService ;
    private final PartingService goodbyeService ;
    private final MyHash md5Hash;
    private final MyHash sha256Hash;
    private final MyHash keccak;
    private final MyHash whirlpool;
    @Inject
    public IocApp(GreetingService helloService,
                  @Named("bye") PartingService byeService,
                  @Named("goodbye") PartingService goodbyeService,
                  @Named("md5") MyHash md5Hash,
                  @Named("sha256") MyHash sha256Hash,
                  @Named("keccak")MyHash keccak,
                  @Named("whirlpool") MyHash whirlpool) {
        this.helloService = helloService;
        this.byeService = byeService;
        this.goodbyeService = goodbyeService;
        this.md5Hash = md5Hash;
        this.sha256Hash = sha256Hash;
        this.keccak = keccak;
        this.whirlpool = whirlpool;
    }

    public void run(){
        System.out.println("App");
        helloService.sayHello();
        byeService.sayGoodbye();
        goodbyeService.sayGoodbye();
        String text = getText();
        md5Hash.transformation(text);
        sha256Hash.transformation(text);
        keccak.transformation(text);
        whirlpool.transformation(text);
    }
    private String getText(){
        System.out.println("Enter the text:");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
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
/*
Д.З. Реалізувати принаймні 2 хеш-сервіси (MD5, SHA) та впровадити їх
у проєкт шляхом інжекції залежностей.
Забезпечити можливість роботи з обома сервісами.
Перевірити виведенням хешу рядка, що його вводить користувач
з клавіатури
 */