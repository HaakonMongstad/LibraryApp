package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import backend.Item;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import network.Client;
import network.messageType;
import javafx.scene.text.Text;

public class mainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField itemSearch;

    @FXML
    private GridPane catalog;
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
        System.out.println("HI");
        TextArea temp = new TextArea();
        temp.appendText("HELLOOO");
        catalog.add(temp,0,0);

    }

    @FXML
    void pickType(ActionEvent event){
        String type = typeSelect.getValue();
        client.setMessage(messageType.LOADCATALOG,type,"",null,0);
        client.setMessageCreated();
    }
    public void loadCatalog(ArrayList<Item> items){
        catalog.getChildren().clear();
        int row = 0;
        for (Item item : items){
            catalog.add(new Text(item.title + "\n" + item.author + "\n" + item.pages + "\n" + item.summary),0,row);
        }
    }
    public void setClientController(){
        client.controller2 = this;
    }

    @FXML
    void initialize() {
        assert itemSearch != null : "fx:id=\"itemSearch\" was not injected: check your FXML file 'MainPage.fxml'.";
        assert catalog != null : "fx:id=\"catalog\" was not injected: check your FXML file 'MainPage.fxml'.";

        typeSelect.getItems().setAll(choices);
    }
}
