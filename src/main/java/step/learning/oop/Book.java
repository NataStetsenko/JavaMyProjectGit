package step.learning.oop;

public class Book extends Literature{
    private String author;

    public Book(String title, String author) {
        super.setTitle(title);
        this.setAuthor(author);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
