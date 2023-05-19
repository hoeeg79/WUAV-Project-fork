package GUI.Controller;

import BE.Device;
import BE.TechDoc;
import GUI.Model.TechDocModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class CreateDeviceController extends BaseController {
    @FXML
    private TextField tfDevice;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfPassword;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnCancel;
    private TechDoc techDoc;


    /**
     * Setup - a method from the basecontroller, used to initialize the TechDocModel
     */
    @Override
    public void setup() {
        try {
            super.setTModel(new TechDocModel());
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * A method used to handle the create device button, when pressed it will create a new device
     */
    @FXML
    private void handleCreateDevice(ActionEvent actionEvent)  {
        try {
            String name = tfDevice.getText();
            String username = tfUsername.getText();
            String password = tfPassword.getText();

            Device device = new Device(name, username, password);
            super.getTModel().addDevice(device, techDoc);
            clearDeviceMenu();
            closeWindow(btnCreate);
        } catch (SQLException e) {
            displayError(e);
        }
    }

    /**
     * A method used to clear the device text fields
     */
    private void clearDeviceMenu(){
        tfDevice.clear();
        tfUsername.clear();
        tfPassword.clear();
    }

    /**
     * A setter for tech document
     */
    public void setTechDoc(TechDoc techDoc) {
        this.techDoc = techDoc;
    }

    /**
     * A method used to handle the cancel button, when pressed it will close the window.
     */
    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        closeWindow(btnCancel);
    }
}
