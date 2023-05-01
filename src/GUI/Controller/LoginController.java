package GUI.Controller;

import BE.User;
import GUI.Model.CustomerModel;
import GUI.Model.LoginModel;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    public TextField tfUsername;
    public PasswordField tfPassword;
    public Button btnLogin;
    public Label lblWarning;
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

    public void handleLogin(ActionEvent actionEvent) throws Exception {
        login();
    }
}
