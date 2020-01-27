package gui;

import com.mysql.cj.ServerVersion;
import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    private Department entity;

    private DepartmentService service;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private Label labelErrorName;
    @FXML
    private Button btSave;
    @FXML
    private Button btCancel;

    public void setDepartment(Department entity){
        this.entity = entity;
    }

    public void setDepartmentService(DepartmentService service){
        this.service = service;
    }

    public void subscribeDataChangeListenes(DataChangeListener listener){
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtSaveAction(ActionEvent ev){
        if (entity == null){
            throw new IllegalStateException("Entity was null");
        }
        if (service == null){
            throw new IllegalStateException("Service was null");
        }
        try {
            entity = getFormData();
            service.saveOrUpdate(entity);
            notifyDataChangeListeners();
            Utils.currentStage(ev).close();
        }catch (DbException ex){
            Alerts.showAlert("Error saving object", null, ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners){
            listener.onDataChanged();
        }
    }

    private Department getFormData() {
        Department obj = new Department();
        obj.setId(Utils.tryParseToInt(txtId.getText()));
        obj.setName(txtName.getText());

        return obj;
    }

    @FXML
    public  void onBtCancelAction(ActionEvent ev){
        Utils.currentStage(ev).close();;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }
    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }
    public void updateFormData(){
        if (entity == null){
            throw  new IllegalStateException("Entity was null");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
    }
}
