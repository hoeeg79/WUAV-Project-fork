package GUI.Model;

import BE.User;
import BLL.LoginManager;
import com.microsoft.sqlserver.jdbc.SQLServerException;

public class LoginModel {
    private LoginManager loginManager;

    public LoginModel() throws Exception {
        loginManager = new LoginManager();
    }

    public User login(String username, String password) throws Exception {
        return loginManager.login(username, password);
    }
}
