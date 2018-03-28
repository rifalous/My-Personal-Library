package com.a4visionmedia.personallibrary;

/**
 * Created by Rifal on 28/03/2018.
 */

public class Book {
    private String title;
    private String author;
    private String publisher;
    private String category;
    private String noIsbn;
    private String cover;

    public Book(String title, String author, String publisher, String category, String noIsbn, String cover) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
        this.noIsbn = noIsbn;
        this.cover = cover;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNoIsbn() {
        return noIsbn;
    }

    public void setNoIsbn(String noIsbn) {
        this.noIsbn = noIsbn;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
