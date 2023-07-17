package step.learning.oop;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Newspaper extends Literature implements Periodic{
    public Date getDate() {
        return date;
    }

    public Newspaper(String title, Date date) {
        super.setTitle(title);
        this.setDate(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private Date date;
private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    @Override
    public String getCard() {
        return String.format("Newspaper: '%s' %s",dateFormat.format(this.getDate()), super.getTitle());//інтерполяція
    }

    @Override
    public String getPeriod() {
        return "Daily";
    }
}
