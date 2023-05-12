package GUI.Controller;

import BE.Device;
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


    @Override
    public void setup() throws Exception {
        super.setTModel(new TechDocModel());
    }

    public void handleCreateDevice(ActionEvent actionEvent) throws SQLException {
        String name = tfDevice.getText();
        String username = tfUsername.getText();
        String password = tfPassword.getText();

        Device device = new Device(name, username, password);
        super.getTModel().addDevice(device);
        clearDeviceMenu();
    }

    private void clearDeviceMenu(){
        tfDevice.clear();
        tfUsername.clear();
        tfPassword.clear();
    }
}
