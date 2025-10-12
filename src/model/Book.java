package model;

import java.util.concurrent.atomic.AtomicInteger;

public class Book {
    private final int id;
    private String title;
    private String author;
    private int year;
    private int totalCopies;
    private int availableCopies;
    private static AtomicInteger counter = new AtomicInteger(1);

    public Book(String title, String author, int year, int totalCopies) {
        this.id = counter.getAndIncrement();
        this.title = title;
        this.author = author;
        this.year = year;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    @Override
    public String toString() {
        return id + "  ----  " + title + " (" + author + ", " + year + "), всего/доступно = " + totalCopies + "/" + availableCopies;
    }

}
