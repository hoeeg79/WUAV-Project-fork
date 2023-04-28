package GUI.Controller;

import BE.User;
import BLL.BCrypt;
import GUI.Model.UsersModel;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.sql.SQLException;

public class CreateUsersController extends BaseController{
    public TextField txtNameUser;
    public TextField txtUsernameUser;
    public TextField txtConfirmPwUser;
    public TextField txtPasswordUser;
    public Button saveUser;
    public Button deleteUser;
    public Button cancel;
    public TableView<User> userList;
    public RadioButton techChecker;
    public RadioButton salesChecker;
    public RadioButton managerChecker;
    private UsersModel userModel;

    public void handleSaveUser(ActionEvent actionEvent) {
        saveUser.setDisable(true);

        String username = txtUsernameUser.getText();
        String name = txtNameUser.getText();
        String confirmPassword = txtConfirmPwUser.getText();
        String password = txtPasswordUser.getText();
        int userType = -1;

        if(techChecker.isSelected()) {
            userType = 2;
        } else if (managerChecker.isSelected()) {
            userType = 1;
        } else if (salesChecker.isSelected()) {
            userType = 3;
        }

        if (username.isEmpty() || name.isEmpty() || confirmPassword.isEmpty() || password.isEmpty() || userType == -1) {
            saveUser.setDisable(true);
        } else if (!password.equals(confirmPassword)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Password mismatch");
            alert.setHeaderText(null);
            alert.setContentText("The passwords do not match. Please make sure the password and confirm-password fields match.");
            alert.showAndWait();
            saveUser.setDisable(true);
        } else {
            saveUser.setDisable(false);
        }

    }

    public void handleDeleteUser(ActionEvent actionEvent) {
        try {
            User user = userList.getSelectionModel().getSelectedItem();
            userModel.deleteUser(user);
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void handleCancelWindow(ActionEvent actionEvent) {
        closeWindow(cancel);
    }

    @Override
    public void setup() throws Exception {
        userModel = super.getUModel();
    }




}
