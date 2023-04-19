package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import network.Client;

public class mainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField itemSearch;

    @FXML
    private GridPane catalog;
    public Client client;

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
    void initialize() {
        assert itemSearch != null : "fx:id=\"itemSearch\" was not injected: check your FXML file 'MainPage.fxml'.";
        assert catalog != null : "fx:id=\"catalog\" was not injected: check your FXML file 'MainPage.fxml'.";

    }
}
