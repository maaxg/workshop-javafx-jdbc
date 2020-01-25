package gui;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {
    private DepartmentService service  /* acoplamento forte new DepartmentService) */;
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

    private ObservableList<Department> obsList;
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
    //Acoplamento fraco
    public void setDepartmentService(DepartmentService service){
        this.service = service;
    }

    public void updateTableView(){
        if (service == null){
            throw new IllegalStateException("Service was null");
        }
        List<Department> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewDepartment.setItems(obsList);

    }
}
