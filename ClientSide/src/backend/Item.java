package backend;

public class Item {
    public String type;
    public String title;
    public String author;
    public String pages;
    public String summary;
    public String img;
    public Item(){
        this.type = "item";
    }
    public Item(String item,String title, String author, String pages, String summary, String img){
        this.type = item;
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.summary = summary;
        this.img = img;
    }
}
