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
        String username = txtUsernameUser.getText();
        String name = txtNameUser.getText();
        String confirmPassword = txtConfirmPwUser.getText();
        String password = txtPasswordUser.getText();
        int userType = -1;

        String salt = BCrypt.gensalt(10);
        String hashedPassword1 = BCrypt.hashpw(password, salt);
        String hashedPassword2 = BCrypt.hashpw(confirmPassword, salt);

        //idk
        if(techChecker.isSelected()) {
            userType = 2;
        } else if (managerChecker.isSelected()) {
            userType = 1;
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
