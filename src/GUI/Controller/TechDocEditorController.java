package GUI.Controller;

import BE.Customer;
import BE.TechDoc;
import BE.User;
import GUI.Model.TechDocModel;
import GUI.Model.UsersModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class TechDocEditorController extends BaseController{
    @FXML
    private TextArea taDeviceInfo;
    @FXML
    private TextArea taSetupDescription;
    @FXML
    private TextField tfTitle;
    @FXML
    private Button btnClose;
    private TechDoc techDoc;
    private Customer customer;
    private User user;
    private boolean isEdit;

    @Override
    public void setup() throws Exception {
        super.setTModel(new TechDocModel());
    }

    @FXML
    private void handleClose(ActionEvent actionEvent) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/CustomerView.fxml"));
        Parent root = loader.load();

        CustomerViewController controller = loader.getController();
        controller.setCustomer(customer);
        controller.setUModel(new UsersModel());
        controller.setUser(user);
        controller.setup();

        Stage currentStage = (Stage) btnClose.getScene().getWindow();
        currentStage.setScene(new Scene(root));
        currentStage.show();
    }

    @FXML
    private void handleSave(ActionEvent actionEvent) throws SQLException {
        if (isEdit) {
            doEditOfDoc();
        } else {
            saveNewDoc();
            addTech(techDoc, user);
        }
    }

    private void saveNewDoc() throws SQLException {
        TechDoc newDoc = new TechDoc(tfTitle.getText(), customer.getId());
        newDoc.setSetupDescription(taSetupDescription.getText());
        newDoc.setDeviceLoginInfo(taDeviceInfo.getText());
        techDoc = getTModel().createTechDoc(newDoc);
    }

    private void doEditOfDoc() throws SQLException {
        techDoc.setSetupDescription(taSetupDescription.getText());
        techDoc.setDeviceLoginInfo(taDeviceInfo.getText());
        getTModel().updateTechDoc(techDoc);
    }

    public void setIsEdit(TechDoc techDoc) {
        this.techDoc = techDoc;
        isEdit = true;
        fillFields();
        tfTitle.setDisable(true);
    }

    private void fillFields() {
        taSetupDescription.setText(techDoc.getSetupDescription());
        taDeviceInfo.setText(techDoc.getDeviceLoginInfo());
        tfTitle.setText(techDoc.getSetupName());
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private void addTech(TechDoc techDoc, User user) {
        try {
            super.getTModel().addTech(techDoc, user);
        } catch (SQLException e) {
            e.printStackTrace();
            displayError(e);
        }
    }
}
