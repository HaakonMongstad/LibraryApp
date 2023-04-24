package gui;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import backend.Item;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import network.Client;
import network.messageType;

public class logInController implements Initializable {
    @FXML
    private ResourceBundle resources;
    @FXML
    private PasswordField passField;

    @FXML
    private TextField userField;
    @FXML
    private URL location;

    @FXML
    private Button NewUserButton;

    @FXML
    private Button LogInButton;

    static Client client;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private ActionEvent recentEvent;



    public logInController() {
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


    public void loginSuccess(String user, ArrayList<Item> items){
        System.out.println("SUCCESS");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
            root = loader.load();
            mainController controller = loader.getController();
            controller.setClient(client);
            controller.setClientController();
            controller.setUpController(user);
            stage = (Stage) userField.getScene().getWindow();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    stage.setScene(new Scene(root));
                    stage.sizeToScene();
//                    controller.setUpController(user,items);
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

    public void sound(){
        Toolkit.getDefaultToolkit().beep();
    }

    @FXML
    void NewUserClicked(ActionEvent event) {
//        client.setUserPassword(userField.getText(),passField.getText());
        client.setMessage(messageType.REGISTER,userField.getText(),passField.getText(),null,0);
        userField.clear();
        passField.clear();
        client.setMessageCreated();

    }

    @FXML
    void LogInClicked(ActionEvent event) {
        client.setUserPassword(userField.getText(),passField.getText());
        client.setMessage(messageType.LOGIN,userField.getText(),passField.getText(),null,0);
        userField.clear();
        passField.clear();
        client.setMessageCreated();
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

