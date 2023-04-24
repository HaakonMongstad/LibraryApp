package database;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import javafx.scene.image.Image;

public class Item implements Serializable {
    public String type;
    public String title;
    public String author;
    public String pages;
    public String summary;
    public byte[] img;
    public int qnt;
    public Item(){
        this.type = "item";
    }
    public Item(String item,String title, String author, String pages, String summary, byte[] img, int qnt){
        this.type = item;
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.summary = summary;
        this.img = img;
        this.qnt = qnt;
    }
}
