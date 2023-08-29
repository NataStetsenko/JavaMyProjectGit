package step.learning.treading;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class PercentHomework2 {
    private final Random random = new Random();
    private double sum;
    private final Object sumLocker = new Object() ;
    private final ExecutorService pool = Executors.newFixedThreadPool(4);
    public void run() {
        sum = 100;
        for (EnumMonth.Month month : EnumMonth.Month.values()) {
            printPercent( month ) ;
        }
        try {
            // очікування завершення усіх задач, але не довше часового обмеження
            pool.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println( e.getMessage()  );
        }
        pool.shutdown() ;
        System.out.println("Final sum: " + String.format("%.2f", sum));
    }
    Future<?> printPercent( EnumMonth.Month month ) {
        return pool.submit(() -> {
            double localSum;
            double startSum;
            StringBuffer sb = new StringBuffer();
            int percent = random.nextInt(20)+1;
            synchronized (sumLocker) {
                localSum = sum;
                startSum = sum;
                localSum *= 1 + (percent * 0.01);
                sum = localSum;
            }
            sb.append(month + ": percent = "  + percent
                    +"%; started with sum = " + String.format("%.2f", startSum)
                    + "; finished with sum = "+ String.format("%.2f", localSum));
            System.out.println(sb);
        });
    }
}


