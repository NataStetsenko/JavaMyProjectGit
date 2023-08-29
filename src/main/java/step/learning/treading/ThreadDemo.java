package step.learning.treading;

public class ThreadDemo {
    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println( "Start" ) ;
                try {
                    Thread.sleep(1000 ) ;
                } catch( InterruptedException ex ) {
                    System.err.println( ex.getMessage() ) ;
                }
                System.out.println( "Finish Thread" ) ;
            }
        }).start() ;

        InfoRunnable info = new InfoRunnable("Hello");
        Thread infoThread = new Thread( info ) ;
        infoThread.start() ;

        new Thread( () -> { System.out.println( "Start Lambda" ) ; } ).start() ;
        new Thread( this::forThread ).start() ;

        try {
            infoThread.join();  // wait for infoThread ( ~ await infoThread )
            System.out.println( "Output from infoThread: " + info.getOutputData() ) ;
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println( "Main finishes" ) ;
    }

    private void forThread() {
        System.out.println( "Start forThread" ) ;
    }

    static class InfoRunnable implements Runnable {
        private final String inputData ;
        private String outputData ;

        public InfoRunnable(String inputData) {
            this.inputData = inputData;
        }

        public String getOutputData() {
            return outputData;
        }

        @Override
        public void run() {
            System.out.println("Processing " + inputData);
            try {
                Thread.sleep(1000 ) ;
            }
            catch( InterruptedException ex ) {
                System.err.println( ex.getMessage() ) ;
            }
            System.out.println( "Finish infoThread" ) ;
            outputData = "Processing result" ;
        }
    }
}

/*
Багатопоточність, багатозадачність та асинхронність

(А)Синхронність - розподіл коду у часі
 - синхронність (синхронний код) - код виконується послідовно, інструкція за інструкцією
 - асинхронність - можливість у певний проміжок часу одночасного виконання інструкцій

Багатопоточність - програмування з використанням системних об'єктів "потоки"

Багатопроцессність - використання кількох системних об'єктів "процес"

Grid-, Network- технології - використання вузлів, поєднаних у мережу

Багатозадачність - реалізація коду (програми) з використанням програмних сутностей "задача"
Реалізація багатозадачності може бути як в одному потоці, так і в кількох

 */
/*
Багатопоточність - робота з об'єктами Thread
Конструктор класу приймає те, що буде виконуватись в окремому потоці, зокрема
реалізацію інтерфейсу Runnable (функціональний інтерфейс - і. з однією функцією).
Запускається потік методом start() [ ! метод run() запускає синхронно ]
 - !! запуск через start() утворює потік з пріоритетом NORMAL, це значить, що він
       не залежить від головного потоку і буде продовжувати роботу після його завершення
       (завершення всієї програми)
Метод Runnable::run() не приймає параметри, не повертає значення
Для обміну даними робиться переозначення класу (Thread чи Runnable), до них додаються
поля через які здійснюється прийом та повернення даних
!! Якщо вимагається повернення даних слід забезпечити очікування завершення потоку,
інакше потік, з якого було запущено новий потік, вже завершить свою роботу.

Використання багатопоточності:
- Тривалі задачі
    Thread t1 = new ConnectionToDb()
    t1.start();  запуск підключення до БД
    ... робимо інші задачі, не пов'язані з БД
    t1.join()  коли задачі без БД закінчились, очікуємо завершення потоку
    t1.getConnection() використовуємо його дані

   За наявності більшої кількості задач їх стартують у зворотному порядку до
   їх тривалості: спочатку найбільш тривала і так далі
   ! і навпаки, поганим стилем є послідовне очікування задач/потоків
    t1.start();
    t1.join()
    t2.start();
    t2.join()

- Задачі "порядконезалежні" - послідовність дій у яких може бути довільною (переставленою)
   наприклад, задача підрахунку суми: порядок складання доданків не грає ролі.
   Ідея: порахувати річну інфляцію якщо відомі відсотки за кожен місяць.
   ( 100 + 10% ) + 20%  =?= ( 100 + 20% ) + 10%
   100 * 1.1 * 1.2  == 100 * 1.2 * 1.1
 */