package gui;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {
    @FXML
    private Button btNew;
    @FXML
    private TableView<Department> tableViewDepartment;
    @FXML
                        /*
                        Entitie  and Column Type
                         */
    private TableColumn<Department, Integer> tableColumnId;
    @FXML
    private TableColumn<Department, String> tableColumnName;

    @FXML
    public void onBtNewAction(){
        System.out.println("onBtNewAction");
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private void initializeNodes() {
        /*
        A pattern from javaFx to initialize the columns behaviors
         */
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
                                            //A window it's a superclass from Stage
        Stage stage = (Stage) Main.getMainScene().getWindow();
        //The table view will follow the hight from window
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());

    }
}
