package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class libraryGUI extends Application {

    @Override
    public void start(Stage applicationStage) {
        // TODO: Implement this method
        try{
            FXMLLoader temp  = new FXMLLoader(getClass().getResource("LogInPage.fxml"));
            Parent root= FXMLLoader.load(getClass().getResource("LogInPage.fxml"));
            applicationStage.setScene(new Scene(root));
            applicationStage.show();
//        TemplateController gui = new TemplateController();
//        GUITestListener listener = new GUITestListener();
//        listener.testStarted("HELLOOO");


        }
        catch (java.io.IOException e){

        }



    }

    public static void main(String[] args) {
        launch(args); // Launch application
    }
}
