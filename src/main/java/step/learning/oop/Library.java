package step.learning.oop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Library {
    private List<Literature> funds;

    public Library() {
        funds = new ArrayList<>();
        funds.add( new Book("Art of Programming", "D. Knuth"));
        funds.add(new Journal("Nature", 123));
        funds.add(new Newspaper("Dayle Mail", new Date(2020 -1900,9-1,10)));
        funds.add(new Hologram("Pectoral", new Date(1995 - 1900, 3 - 1, 07)));
        funds.add(new Hologram("Yaroslav the Wise", new Date(1990 - 1900, 11 - 1, 17)));
        funds.add(new Hologram("Frame the Gospel", new Date(1991 - 1900, 6 - 1, 23)));
        funds.add(new Hologram("Ornaments of Kyivan Rus", new Date(1994 - 1900, 12 - 1, 27)));
        funds.add(new Booklet("Kyivan Rus", "VisaviPrint"));
        funds.add(new Poster("Harry Potter"));
        funds.add(new Poster("Jack Sparrow"));
    }
    public void getLiterature(Literature literature) {
        funds.add(literature);
    }

    public void showCatalog() {
        System.out.println( "--------Exhibits-------------" );
        Exhibits();
        System.out.println( "--------NonExhibits-------------" );
        NonExhibits();
        System.out.println( "--------CopyAble-------------" );
        showCopyAble();
        System.out.println( "--------NoCopyAble-------------" );
        showNoCopyAble();
        System.out.println("--------Periodic-------------");
        Periodic();
        System.out.println("--------Books Authors-------------");
        ShowBooks();

    }

    public void showCopyAble() {
        for (Literature literature : funds) {
            if (literature instanceof CopyAble) {
                System.out.println(literature.getCard());
            }
        }
    }

    public void showNoCopyAble() {
        for (Literature literature : funds) {
            if (!(literature instanceof CopyAble)) {
                System.out.println(literature.getCard());
            }
        }
    }

    public void Periodic() {
        for (Literature literature : funds) {
            if (literature instanceof Periodic) {
                System.out.print(literature.getCard());
                System.out.printf(" Periodic with period: %s\n",
                        ((Periodic) literature).getPeriod());//аналог as
            }
        }
    }
    public void ShowBooks() {
        for (Literature literature : funds) {
            if (literature instanceof Book) { // аналог is
                Book book = (Book) literature;   // cast - 'перетворення' типів
                System.out.println(book.getAuthor());
            }
        }
    }

    public void Exhibits() {
        for (Literature literature : funds) {
            if (literature instanceof Expo) {
                System.out.println(literature.getCard());
            }
        }
    }

    public void NonExhibits() {
        for (Literature literature : funds) {
            if (!(literature instanceof Expo)) {
                System.out.println(literature.getCard());
            }
        }
    }
}
/*
Проєкт "Бібліотека"
Бібліотека - сховище літератури різного типу: газети, журнали, книги, тощо
По кожному виду літератури має бути аркуш-каталог із назвою та іншими даними

Розширення: послуга копіювання
можна- книги та журнали, газети - ні
періодичність:
 книги  - ні,  журнали та газети - так
 */