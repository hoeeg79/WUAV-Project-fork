package GUI.Controller;

import BE.Customer;
import BE.TechDoc;
import BE.User;
import GUI.Model.TechDocModel;
import GUI.Model.UsersModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class TechDocEditorController extends BaseController{
    @FXML
    private TextArea taExtraInfo;
    @FXML
    private Label lblSaveStatus;
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
        generateTechDoc();
    }

    @FXML
    private void handleClose(ActionEvent actionEvent) throws Exception {
        if (!isEdit) {
            //super.getTModel().deleteTechDoc(techDoc); //IMPLEMENT THIS!
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/CustomerView.fxml"));
        Parent root = loader.load();

        CustomerViewController controller = loader.getController();
        controller.setCustomer(customer);
        controller.setUModel(new UsersModel());
        controller.setUser(user);
        controller.setup();

        Stage currentStage = (Stage) btnClose.getScene().getWindow();
        currentStage.setScene(new Scene(root));
        currentStage.centerOnScreen();
        currentStage.show();
    }

    @FXML
    private void handleSave(ActionEvent actionEvent) throws SQLException {
        if (isEdit) {
            doEditOfDoc();
        } else {
            isEdit = true;
            doEditOfDoc();
            addTech(techDoc, user);
        }
        lblSaveStatus.setText("Saved successfully!");
        clearSavedLabelText();
    }

    private void generateTechDoc() throws SQLException {
        TechDoc newDoc = new TechDoc("not saved yet", customer.getId());
        techDoc = getTModel().createTechDoc(newDoc);
    }

    private void doEditOfDoc() throws SQLException {
        techDoc.setSetupName(tfTitle.getText());
        techDoc.setSetupDescription(taSetupDescription.getText());
        techDoc.setDeviceLoginInfo(taDeviceInfo.getText());
        techDoc.setExtraInfo(taExtraInfo.getText());
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
        taExtraInfo.setText(techDoc.getExtraInfo());
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

    private void clearSavedLabelText(){
        Timer timer = new Timer();

        TimerTask task = new TimerTask(){
            @Override
            public void run() {
                Platform.runLater(() -> lblSaveStatus.setText(""));
            }
        };
        timer.schedule(task, 5000);
    }
}
