package BLL;

import BE.User;
import DAL.FacadeDAL;
import com.microsoft.sqlserver.jdbc.SQLServerException;

public class LoginManager {

    private FacadeDAL facadeDAL;

    /**
     * Constructor used to create a new instance of FacadeDAL
     * @throws Exception
     */
    public LoginManager() throws Exception {
        facadeDAL = new FacadeDAL();
    }

    /**
     * A method used to log in to a specific account using a username and a password
     * Checks if the hashed password is matching the password given, to log in the user
     */
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
