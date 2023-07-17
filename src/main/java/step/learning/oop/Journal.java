package step.learning.oop;

public class Journal extends Literature implements CopyAble, Periodic{
    private Integer number;

    public Journal(String title, Integer number) {
        super();// делегування конструктору
        super.setTitle(title);
        this.setNumber(number);
    }
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String getCard() {
        return   String.format("Journal No %d %s",this.getNumber(), super.getTitle());
    }

    @Override
    public String getPeriod() {
        return "Monthly";
    }
}
