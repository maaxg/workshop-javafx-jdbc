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
import model.services.DepartmentService;
import model.services.SellerService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
       loadView("/gui/SellerList.fxml", (SellerListController controller) ->{
           controller.setSellerService(new SellerService());
           controller.updateTableView();
        });
    }
    @FXML
    //A event handler
    public void onMenuItemDepartmentAction(){
        //Temporary loadview
        loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
            controller.setDepartmentService(new DepartmentService());
            controller.updateTableView();

            });
    }
    @FXML
    //A event handler
    public void onMenuItemAboutAction(){
       loadView("/gui/About.fxml", x -> {});
    }
                        //Generics o loadView virou uma função generica parametrizada como um tipo qualquer
    private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction){
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
                    //Take the controller from the action that was passed;
            T controller = loader.getController();
            initializingAction.accept(controller);

        } catch (IOException ex){
            Alerts.showAlert("IO Exception", "Error loading view", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
