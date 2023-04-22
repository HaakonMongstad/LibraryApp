package gui;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import backend.Item;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import network.Client;
import network.messageType;
import javafx.scene.text.Text;
import javafx.scene.image.Image;

import javax.swing.text.Element;
import javafx.scene.image.ImageView;

public class mainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField itemSearch;

    @FXML
    private AnchorPane catalog;
    @FXML
    private ChoiceBox<String> typeSelect;
    public Client client;

    public ArrayList<backend.Item> items;
    private String[] choices = new String[] {"Book", "Movie"};
    public void setClient(Client client){
        this.client = client;
    }

    @FXML
    void filterItems(ActionEvent event) {
//        System.out.println("HI");
//        TextArea temp = new TextArea();
//        temp.appendText("HELLOOO");
//        catalog.add(temp,0,0);

    }

    @FXML
    void pickType(ActionEvent event){
        System.out.println("HERE");
        String type = typeSelect.getValue();
        client.setMessage(messageType.LOADCATALOG,type,"",null,0);
        client.setMessageCreated();
    }
    public void loadCatalog(ArrayList<Item> items){
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
              double offset = 5;
              for (Item item : items) {

                  try {
                      Label title = new Label("Title: " + item.title);
                      title.relocate(20, 5);
                      Button checkOut = new Button("Check Out");
                      checkOut.relocate(200, 5);
                      Label author = new Label("Author: " + item.author);
                      author.relocate(20, 25);
                      Label length = new Label("Length: " + item.pages);
                      length.relocate(20, 45);
                      ByteArrayInputStream is = new ByteArrayInputStream(item.img);
                      ImageView imgView = new ImageView(new Image(is));
                      imgView.relocate(200, 25);
                      AnchorPane page = new AnchorPane(title, checkOut, author, length,imgView);
                      page.relocate(10, 5 + offset);
                      page.setBackground(new Background(new BackgroundFill(Color.web("bb86fc"),
                              new CornerRadii(20), new Insets(0))));
                      catalog.getChildren().add(page);
                      offset = offset + 120;
                  }
                  catch(Exception e){

                  }
              }
          }
//              catalog.getChildren().clear();
//              int row = 0;
//              for (Item item : items){
//                  Label thisIsALabel = new Label();
//                  thisIsALabel.setWrapText(true);
//                  thisIsALabel.setPrefWidth(320);
//                  thisIsALabel.setText(item.title + "\n" + item.author + "\n" + item.pages + "\n" + item.summary);
//
//
//
//
//                  catalog.add(thisIsALabel,0,row);
//                  row ++;
//              }
//          }
      });
//        catalog.getChildren().clear();
//        int row = 0;
//        for (Item item : items){
//            catalog.add(new Text(item.title + "\n" + item.author + "\n" + item.pages + "\n" + item.summary),0,row);
//        }
    }
    public void setClientController(){
        client.controller2 = this;
    }

    @FXML
    void initialize() {
        assert itemSearch != null : "fx:id=\"itemSearch\" was not injected: check your FXML file 'MainPage.fxml'.";
        assert catalog != null : "fx:id=\"catalog\" was not injected: check your FXML file 'MainPage.fxml'.";

        typeSelect.getItems().setAll(choices);
        typeSelect.setOnAction(this::pickType);
    }
}
