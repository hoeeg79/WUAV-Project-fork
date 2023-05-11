package BLL;

import BE.User;
import DAL.FacadeDAL;
import com.microsoft.sqlserver.jdbc.SQLServerException;

public class LoginManager {

    private FacadeDAL facadeDAL;

    public LoginManager() throws Exception {
        facadeDAL = new FacadeDAL();
    }

    public User login(String username, String password) throws Exception {
        User user = facadeDAL.login(username);

        String hashedPassword = user.getPassword();
        if (BCrypt.checkpw(password, hashedPassword)){
            return user;
        } else {
            System.out.println("Invalid username or password.");
            throw new Exception();
        }
    }
}
