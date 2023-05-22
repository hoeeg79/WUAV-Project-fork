package GUI.Model;

import BE.User;
import BLL.LoginManager;

public class LoginModel {
    private final LoginManager loginManager;

    /**
     * Constructor of the LoginModel.
     */
    public LoginModel() throws Exception {
        loginManager = new LoginManager();
    }

    /**
     * A method used that logs you into the program, using username and password.
     */
    public User login(String username, String password) throws Exception {
        return loginManager.login(username, password);
    }
}
