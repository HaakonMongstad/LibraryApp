package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class LogInController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button NewUserButton;

    @FXML
    private Button LogInButton;

    @FXML
    void NewUserClicked(ActionEvent event) {

    }

    @FXML
    void LogInClicked(ActionEvent event) {

    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert NewUserButton != null : "fx:id=\"NewUserButton\" was not injected: check your FXML file 'LogInPage.fxml'.";
        assert LogInButton != null : "fx:id=\"LogInButton\" was not injected: check your FXML file 'LogInPage.fxml'.";

    }
}

