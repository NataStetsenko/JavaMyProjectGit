package step.learning.oop;

public class Poster extends Literature implements CopyAble, Expo{
    public Poster(String title){
        super.setTitle(title);
    }
    @Override
    public String getCard() {
        return String.format("Poster: %s", super.getTitle());
    }
}
