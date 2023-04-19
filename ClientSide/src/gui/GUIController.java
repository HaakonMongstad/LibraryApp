package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
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
import javafx.stage.WindowEvent;
import network.Client;
import backend.logInBackend;
import javafx.event.EventHandler;

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

    static Client client;
    logInBackend backend;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private ActionEvent recentEvent;



    public GUIController() {
        try{
//            backend = new logInBackend(this);
//            backend.startBackend();
            if (client == null) {
                client = new Client();
                client.setUpNetworking(this);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public void loginSuccess(){
        System.out.println("SUCCESS");
        try {
            root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
            stage = (Stage) userField.getScene().getWindow();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    stage.setScene(new Scene(root));
                    stage.sizeToScene();
                }
            });
//            scene = new Scene(root);
//            stage.setScene(scene);
//            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }



    @FXML
    void NewUserClicked(ActionEvent event) {
        client.setUserPassword(userField.getText(),passField.getText());
        userField.clear();
        passField.clear();
        client.registerPressed = true;

    }

    @FXML
    void LogInClicked(ActionEvent event) {
        client.setUserPassword(userField.getText(),passField.getText());
        userField.clear();
        passField.clear();
        client.setLogInPressed();
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert NewUserButton != null : "fx:id=\"NewUserButton\" was not injected: check your FXML file 'LogInPage.fxml'.";
        assert LogInButton != null : "fx:id=\"LogInButton\" was not injected: check your FXML file 'LogInPage.fxml'.";

//        stage = (Stage) userField.getScene().getWindow();
//        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent event) {
//                client.closeSocket();
//            }
//        });
    }
}

