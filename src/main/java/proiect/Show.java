package proiect;

import enums.CategoryEnum;

import java.time.LocalDate;

public class Show {
    private String title;
    CategoryEnum category;
    LocalDate releaseDate;
    int likes;

    public Show(String title, CategoryEnum category, LocalDate releaseDate, int likes) {
        this.title = title;
        this.category = category;
        this.releaseDate = releaseDate;
        this.likes = likes;
    }

    public Show() {

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getLikes() {
        return likes;
    }
}
