package gui;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private MenuItem menuItemSeller;
    @FXML
    private MenuItem menuItemDepartment;
    @FXML
    private MenuItem menuItemAbout;
    @FXML
                //A event handler
    public void onMenuItemSellerAction(){
        System.out.println("OnMenuItemSellerAction");
    }
    @FXML
    //A event handler
    public void onMenuItemDepartmentAction(){
        System.out.println("onMenuItemDepartmentAction");
    }
    @FXML
    //A event handler
    public void onMenuItemAboutAction(){
       loadView("/gui/About.fxml");
    }
    private synchronized void loadView(String absoluteName){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newVbox = loader.load();

            Scene mainScene = Main.getMainScene();
                            /*
                                  The VBox reference from mainScene
                             */

            VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
                            /*
                            The first children from mainVBox;
                             */
            Node mainMenu = mainVBox.getChildren().get(0);

                    /*
                    Cleaning all childrens from mainVbox
                     */
            mainVBox.getChildren().clear();

            /*
            Adding all children from mainMenu to mainVbox, after that we add a collection of children from newVbox
             */
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVbox.getChildren());

        }catch (IOException ex){
            Alerts.showAlert("IO Exception", "Error loading view", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
