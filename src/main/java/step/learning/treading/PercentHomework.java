package step.learning.treading;

import java.util.Random;

public class PercentHomework {
    private final Random random = new Random();
    private double sum;
    public void run() {
        sum = 100;
        Thread[] threads = new Thread[EnumMonth.Month.values().length];
        for (EnumMonth.Month month : EnumMonth.Month.values()) {
            threads[month.getMonthNumber()] = new PercentAdder(month);
            threads[month.getMonthNumber()].start();
        }
        try {
            for (EnumMonth.Month month : EnumMonth.Month.values()) {
                threads[month.getMonthNumber()].join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Final sum: " + String.format("%.2f", sum));
    }

    class PercentAdder extends Thread {
        EnumMonth.Month month;

        public PercentAdder(EnumMonth.Month month) {
            this.month = month;
        }

        @Override
        public void run() {

            int receivingTime = random.nextInt(300) + 200;
            try {
                Thread.sleep(receivingTime);
            } catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
            }
            int percent = random.nextInt(20)+1;
            String str = month + ": percent = "  + percent +"%; started with sum = " + String.format("%.2f", sum) + "; finished with sum = ";
            sum *= 1 + (percent * 0.01);
            System.out.println(str + String.format("%.2f", sum));
        }
    }
}

class EnumMonth {
    public enum Month {
        JANUARY(0), FEBRUARY(1), MARCH(2), APRIL(3), MAY(4), JUNE(5), JULY(6),
        AUGUST(7), SEPTEMBER(8), OCTOBER(9), NOVEMBER(10), DECEMBER(11);
        private int monthNumber;
        Month(int monthNumber) {
            this.monthNumber = monthNumber;
        }
        public int getMonthNumber() {
            return monthNumber;
        }
    }
}