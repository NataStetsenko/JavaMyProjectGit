package step.learning.oop;

public class Journal extends Literature{
    private Integer number;

    public Journal(String title, Integer number) {
        super.setTitle(title);
        this.setNumber(number);
    }
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
