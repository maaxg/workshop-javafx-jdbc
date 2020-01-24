package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage){
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
        //O scroll pane tem herança de parent
        ScrollPane scrollPane = loader.load();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        Scene mainScene = new Scene(scrollPane);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Sample JavaFX Main");
        primaryStage.show();
        }catch (IOException ex){
            System.err.println("Error: " + ex.getMessage());
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
