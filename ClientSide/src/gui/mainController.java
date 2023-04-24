package gui;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


import backend.Item;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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
    private AnchorPane inventory;
    @FXML
    private ChoiceBox<String> typeSelect;
    public Client client;
    public String userName;
    public ArrayList<String> reservations;

    private String[] choices = new String[] {"Book", "Movie"};
    public void setUpController(String user){
        this.userName = user;
        reservations = new ArrayList<>();
    }
    public void setClient(Client client){
        this.client = client;
    }
    public void setReservations(ArrayList<String> reservations){
        this.reservations = reservations;
    }

    @FXML
    void filterItems(ActionEvent event) {
//        System.out.println("HI");
//        TextArea temp = new TextArea();
//        temp.appendText("HELLOOO");
//        catalog.add(temp,0,0);

    }
    public void sound(){
        Toolkit.getDefaultToolkit().beep();
    }

    @FXML
    void pickType(ActionEvent event){
        System.out.println("HERE");
        String type = typeSelect.getValue();
        itemSearch.setPromptText("Search " + typeSelect.getValue() + "s");
        client.setMessage(messageType.LOADCATALOG,type,"",null,0);
        client.setMessageCreated();
    }
    public void loadCatalog(ArrayList<Item> items){
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
              catalog.getChildren().clear();
              double offset = 5;
              for (Item item : items) {
                  if (!item.type.equals(typeSelect.getValue())){
                      continue;
                  }
                  if (itemSearch.getText().length() > 0 ){
                      if (!itemSearch.getText().equals(item.title)){
                          continue;
                      }
                  }
                  try {
                      System.out.println(catalog.getChildren().size());
                      Label title = new Label("Title: " + item.title);
                      title.relocate(100, 20);
                      Button checkOut;
                      if (item.qnt > 0) {
                          checkOut = new Button("Check Out");
                          checkOut.relocate(95, 80);
                          checkOut.setBackground(new Background(new BackgroundFill(Color.web("#bb86fc"),
                                  new CornerRadii(20), new Insets(0))));
                          checkOut.setOnAction(new EventHandler<ActionEvent>() {
                              @Override
                              public void handle(ActionEvent event) {
                                  handleCheckOut(item);
                              }
                          });
                      }
                      else{
                          if(!reservations.contains(item.title)) {
                              checkOut = new Button("Reserve");
                              checkOut.relocate(95, 80);
                              checkOut.setBackground(new Background(new BackgroundFill(Color.web("#bb86fc"),
                                      new CornerRadii(20), new Insets(0))));
                              checkOut.setOnAction(new EventHandler<ActionEvent>() {
                                  @Override
                                  public void handle(ActionEvent event) {
                                      handleReserve(item);
                                  }
                              });
                          }
                          else{
                              checkOut = new Button("UnReserve");
                              checkOut.relocate(95, 80);
                              checkOut.setBackground(new Background(new BackgroundFill(Color.web("#cf6679"),
                                      new CornerRadii(20), new Insets(0))));
                              checkOut.setOnAction(new EventHandler<ActionEvent>() {
                                  @Override
                                  public void handle(ActionEvent event) {
                                      handleUnReserve(item);
                                  }
                              });
                          }
                      }
                      Label quantity = new Label(item.qnt + " Remaining");
                      quantity.relocate(180,82);
                      quantity.setTextFill(Paint.valueOf("#cf6679"));

                      Label author = new Label("Author: " + item.author);
                      author.relocate(100, 40);
                      Label length = new Label("Length: " + item.pages);
                      length.relocate(100, 60);
                      ByteArrayInputStream is = new ByteArrayInputStream(item.img);
                      ImageView imgView = new ImageView(new Image(is));
                      imgView.relocate(20, 10);
                      Label summary = new Label("Summary: " + item.summary + "\n\n");
                      summary.setWrapText(true);
                      summary.setMaxWidth(320);
                      summary.relocate(20,120);



                      title.setTextFill(Paint.valueOf("#FFFFFF"));
                      author.setTextFill(Paint.valueOf("#FFFFFF"));
                      length.setTextFill(Paint.valueOf("#FFFFFF"));
                      summary.setTextFill(Paint.valueOf("#FFFFFF"));

                      AnchorPane page = new AnchorPane(title, checkOut, author, length,imgView,summary,quantity);
                      page.setMinWidth(340);

                      page.relocate(30, 5 + offset);

                      page.setBackground(new Background(new BackgroundFill(Color.web("#333333"),
                              new CornerRadii(20), new Insets(0))));
                      // new BackGroundFill(Color.web(#bb86fc))
                      catalog.getChildren().add(page);
                      System.out.println(page.getHeight());
                      offset = offset + ((item.summary.length() / 50) * 20) + 140;
                  }
                  catch(Exception e){

                  }
              }
          }
      });
    }
    void handleReserve(Item item){
        ArrayList<Item> temp =  new ArrayList<>();
        temp.add(item);
        client.setMessage(messageType.RESERVE,this.userName,"",temp,0);
        client.setMessageCreated();
    }
    void handleUnReserve(Item item){
        ArrayList<Item> temp =  new ArrayList<>();
        temp.add(item);
        client.setMessage(messageType.UNRESERVE,this.userName,"",temp,0);
        client.setMessageCreated();
    }

    void handleCheckOut(Item item){
        ArrayList<Item> temp =  new ArrayList<>();
        temp.add(item);
        client.setMessage(messageType.CHECKOUT,this.userName,"",temp,0);
        client.setMessageCreated();
    }
    void handleReturnItem(Item item){
        ArrayList<Item> temp =  new ArrayList<>();
        temp.add(item);
        client.setMessage(messageType.RETURN,this.userName,"",temp,0);
        client.setMessageCreated();
    }

    public void displayInventory(ArrayList<Item> items){
        System.out.println(inventory);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                inventory.getChildren().clear();
                double offset = 5;
                for (Item item : items) {
                    try {
                        Label title = new Label("Title: " + item.title);
                        title.relocate(100, 20);
                        Button ret = new Button("Return");
                        ret.relocate(95, 80);
                        ret.setBackground(new Background(new BackgroundFill(Color.web("#bb86fc"),
                                new CornerRadii(20), new Insets(0))));
                        ret.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                handleReturnItem(item);
                            }
                        });
                        Label author = new Label("Author: " + item.author);
                        author.relocate(100, 40);
                        Label length = new Label("Length: " + item.pages);
                        length.relocate(100, 60);
                        ByteArrayInputStream is = new ByteArrayInputStream(item.img);
                        ImageView imgView = new ImageView(new Image(is));
                        imgView.relocate(20, 10);
                        Label summary = new Label("Summary: " + item.summary + "\n\n");
                        summary.setWrapText(true);
                        summary.setMaxWidth(320);
                        summary.relocate(20,120);



                        title.setTextFill(Paint.valueOf("#FFFFFF"));
                        author.setTextFill(Paint.valueOf("#FFFFFF"));
                        length.setTextFill(Paint.valueOf("#FFFFFF"));
                        summary.setTextFill(Paint.valueOf("#FFFFFF"));

                        AnchorPane page = new AnchorPane(title, ret, author, length,imgView,summary);
                        page.setMinWidth(340);

                        page.relocate(30, 5 + offset);

                        page.setBackground(new Background(new BackgroundFill(Color.web("#333333"),
                                new CornerRadii(20), new Insets(0))));
                        // new BackGroundFill(Color.web(#bb86fc))
                        inventory.getChildren().add(page);
                        System.out.println(page.getHeight());
                        offset = offset + ((item.summary.length() / 50) * 20) + 140;
                    }
                    catch(Exception e){

                    }
                }
            }
        });
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
