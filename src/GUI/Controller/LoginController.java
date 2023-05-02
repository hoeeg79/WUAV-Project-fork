package GUI.Controller;

import BE.User;
import GUI.Model.CustomerModel;
import GUI.Model.LoginModel;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblWarning;
    private LoginModel loginModel = new LoginModel();

    public LoginController() throws Exception {
    }

    private void login() throws Exception {
        String username = tfUsername.getText();
        String password = tfPassword.getText();
        Stage primaryStage = (Stage) btnLogin.getScene().getWindow();
        User user = loginModel.login(username, password);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/MainView.fxml"));

        if (user == null) {
            lblWarning.setText("Username or password is invalid.");
        } else {
            Parent root = loader.load();
            Scene scene = new Scene(root);

            MainViewController controller = loader.getController();
            controller.setCModel(new CustomerModel());
            controller.setUser(user);
            controller.setup();

            primaryStage.setScene(scene);
            primaryStage.setTitle("Main View");
            primaryStage.show();
        }
    }

    @FXML
    private void handleLogin(ActionEvent actionEvent) throws Exception {
        login();
    }
    private void checkIfEnter(KeyEvent event) {
        try {
            if (event.getCode() == KeyCode.ENTER) {
                login();
            }
        } catch (Exception e) {
            //displayError(e);
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePasswordCheckKey(KeyEvent keyEvent) {
        checkIfEnter(keyEvent);
    }

    @FXML
    private void handleUsernameCheckKey(KeyEvent keyEvent) {
        checkIfEnter(keyEvent);
    }
}
