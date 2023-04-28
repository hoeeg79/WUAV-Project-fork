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

    @Override
    public void setup() throws Exception {
        userModel = super.getUModel();
        saveUser.setDisable(true);

        txtUsernameUser.textProperty().addListener((observable, oldValue, newValue) -> handleSaveUser());
        txtNameUser.textProperty().addListener((observable, oldValue, newValue) -> handleSaveUser());
        txtPasswordUser.textProperty().addListener((observable, oldValue, newValue) -> handleSaveUser());
        txtConfirmPwUser.textProperty().addListener((observable, oldValue, newValue) -> handleSaveUser());

        techChecker.selectedProperty().addListener((observable, oldValue, newValue) -> handleSaveUser());
        managerChecker.selectedProperty().addListener((observable, oldValue, newValue) -> handleSaveUser());
        salesChecker.selectedProperty().addListener((observable, oldValue, newValue) -> handleSaveUser());
    }

    public void handleSaveUser(ActionEvent actionEvent) {
        String username = txtUsernameUser.getText();
        String name = txtNameUser.getText();
        String password = txtPasswordUser.getText();
        String confirmPassword = txtConfirmPwUser.getText();

        int userType = -1;
        if(techChecker.isSelected()) {
            userType = 2;
        } else if (managerChecker.isSelected()) {
            userType = 1;
        } else if (salesChecker.isSelected()) {
            userType = 3;
        }

       /* if (username.isEmpty() || name.isEmpty() || confirmPassword.isEmpty() || password.isEmpty() || userType == -1) {
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
        }*/

        String salt = BCrypt.gensalt(10);
        String hashedPassword1 = BCrypt.hashpw(password, salt);
        String hashedPassword2 = BCrypt.hashpw(confirmPassword, salt);
        try {
            if (hashedPassword1.equals(hashedPassword2)) {
                userModel.createUser();
                closeWindow(saveUser);
            }
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
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

}
