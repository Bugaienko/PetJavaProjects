package ua.bugaienko.springmvcapp.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author Sergii Bugaienko
 */

public class Book {
    private int book_id;


    @NotEmpty
    @Size(min = 1, message = "Name should be greater than 2")
    private String title;
    @NotEmpty
    @Size(min = 1, message = "Author should be greater than 2")
    private String author;
    @Min(value = 1000, message = "Year should be greater than 0" )
    private int year;

    public Book(int book_id, String title, String author, int year) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public  Book() {}

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
