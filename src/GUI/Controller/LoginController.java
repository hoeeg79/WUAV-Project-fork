package GUI.Controller;

import BE.User;
import GUI.Model.CustomerModel;
import GUI.Model.LoginModel;
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

public class LoginController extends BaseController {
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblWarning;
    private final LoginModel loginModel;

    /**
     * The constructor, used to instantiate LoginModel.
     */
    public LoginController() throws Exception {
        loginModel = new LoginModel();
    }

    /**
     * A method that loads the MainView fxml if the login credentials are correct.
     */
    private void login() {
        try {
            String username = tfUsername.getText();
            String password = tfPassword.getText();
            Stage primaryStage = (Stage) btnLogin.getScene().getWindow();
            User user = loginModel.login(username, password);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/MainView.fxml"));

            Parent root = loader.load();
            Scene scene = new Scene(root);

            MainViewController controller = loader.getController();
            controller.setCModel(new CustomerModel());
            controller.setUser(user);
            controller.setup();
            controller.expirationDate();

            primaryStage.setScene(scene);
            primaryStage.setTitle("WUAV Documentation System");
            primaryStage.centerOnScreen();
            primaryStage.show();

        } catch (Exception e) {
            lblWarning.setText("Username or password is invalid.");
        }
    }

    /**
     * A button that runs the login() method.
     */
    @FXML
    private void handleLogin(ActionEvent actionEvent) throws Exception {
        login();
    }

    /**
     * A method that logs you in by pressing enter.
     */
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

    /**
     * A key event that logs you in if you press enter while in the password text field.
     */
    @FXML
    private void handlePasswordCheckKey(KeyEvent keyEvent) {
        checkIfEnter(keyEvent);
    }

    /**
     * a key event that logs you in if you press enter while in the username text field.
     */
    @FXML
    private void handleUsernameCheckKey(KeyEvent keyEvent) {
        checkIfEnter(keyEvent);
    }

    /**
     * A method inherited from BaseController.
     */
    @Override
    public void setup() throws Exception {

    }
}
