package database;

public class Book extends Item{
    public Book(String title, String author, String pages, String summary, String img){
        super(title,author,pages,summary,img);
        this.type = "book";
    }
}
