package GUI.Model;

import BE.User;
import BLL.LoginManager;

public class LoginModel {
    private final LoginManager loginManager;

    public LoginModel() throws Exception {
        loginManager = new LoginManager();
    }

    public User login(String username, String password) throws Exception {
        return loginManager.login(username, password);
    }
}
