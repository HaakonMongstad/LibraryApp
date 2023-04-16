package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.stage.Stage;
import network.Client;
import backend.logInBackend;

public class GUIController implements Initializable {

    @FXML
    private ResourceBundle resources;
    @FXML
    private TextField passField;

    @FXML
    private TextField userField;
    @FXML
    private URL location;

    @FXML
    private Button NewUserButton;

    @FXML
    private Button LogInButton;

    Client client;
    logInBackend backend;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private ActionEvent recentEvent;



    public GUIController() {
        try{
//            backend = new logInBackend(this);
//            backend.startBackend();
            client = new Client();
            client.setUpNetworking(this);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loginSuccess(){
        System.out.println("SUCCESS");
        try {
            root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
            stage = (Stage)((Node)recentEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e){

        }


    }


    @FXML
    void NewUserClicked(ActionEvent event) {
        String temp1 = userField.getText();
        String temp2 = passField.getText();
        userField.clear(); passField.clear();

    }

    @FXML
    void LogInClicked(ActionEvent event) {
        recentEvent = event;
        client.setUserPassword(userField.getText(),passField.getText());
        userField.clear();
        passField.clear();
        client.setLogInPressed();
//        while (client.loginSuccess == false &&
//                client.loginFail == false){
//
//        }
//        if (client.loginSuccess == true){
//            System.out.println("SUCCESS");
//            client.loginSuccess = false;
//        }
//        else if (client.loginFail == true){
//            System.out.println("FAIL");
//            client.loginFail = false;
//        }
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert NewUserButton != null : "fx:id=\"NewUserButton\" was not injected: check your FXML file 'LogInPage.fxml'.";
        assert LogInButton != null : "fx:id=\"LogInButton\" was not injected: check your FXML file 'LogInPage.fxml'.";

    }
}

