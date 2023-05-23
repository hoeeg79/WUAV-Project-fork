package DAL;

import BE.*;

import java.sql.SQLException;
import java.util.List;

public class FacadeDAL {
    private final CustomerDAO customerDAO;
    private final UsersDAO usersDAO;
    private final TechDocDAO techDocDAO;
    private final LoginDAO loginDAO;

    /**
     * The constructor of the FacadeDal class.
     */
    public FacadeDAL() throws Exception {
        customerDAO = new CustomerDAO();
        usersDAO = new UsersDAO();
        techDocDAO = new TechDocDAO();
        loginDAO = new LoginDAO();
    }

    /**
     * A method that checks if a customer already exists, if it exists return null, otherwise it will create a customer.
     */
    public Customer createCustomer(Customer customer) throws SQLException {
        if (customerDAO.checkCustomer(customer)) {
            return null;
        }
        return customerDAO.createCustomer(customer);
    }

    /**
     * A method that will delete a customer.
     */
    public void deleteCustomer(Customer customer) throws SQLException {
        customerDAO.deleteCustomer(customer);
    }

    /**
     * A method that retrieves a list of customers.
     */
    public List<Customer> getCustomers() throws Exception{
        CustomerDAO customers = new CustomerDAO();
        return customers.returnCustomers();
    }

    /**
     * A method that will update a customer's information.
     */
    public void updateCustomer(Customer c) throws SQLException{
        customerDAO.updateCustomer(c);
    }

    /**
     * A method that will create a user.
     */
    public User createUser(User user) throws SQLException {
        return usersDAO.createUser(user);
    }

    /**
     * A method that will delete a user.
     */
    public void deleteUser(User user) throws SQLException {
        usersDAO.deleteUser(user);
    }

    /**
     * A method that retrieves a list of users.
     */
    public List<User> getUsers() throws SQLException {
        return usersDAO.returnUsers();
    }

    /**
     * A method that will update a user's information.
     */
    public void updateUser(User user) throws SQLException {
        usersDAO.updateUser(user);
    }

    /**
     * A method that gets a list of users linked to a tech document.
     */
    public List<User> getLinkedUsers(TechDoc techdoc) throws SQLException {
        return usersDAO.getLinkedUsers(techdoc);
    }

    /**
     * A method that creates a tech document.
     */
    public TechDoc createTechDoc(TechDoc techDoc) throws SQLException {
        return techDocDAO.createTechDoc(techDoc);
    }

    /**
     * A method that adds a tech document to a user.
     */
    public void addTech(TechDoc techDoc, User user) throws SQLException {
        techDocDAO.addTech(techDoc, user);
    }

    /**
     * A method that removes a tech document from a user.
     */
    public void removeTech(TechDoc techDoc, User user) throws SQLException {
        techDocDAO.removeTech(techDoc, user);
    }

    /**
     * A method that gets a list of tech documents associated with a specified customer or user.
     */
    public List<TechDoc> getTechDocs(Customer customer, User user) throws SQLException {
        return techDocDAO.getTechDocs(customer, user);
    }

    /**
     * A method that updates the information of a tech document.
     */
    public void updateTechDoc(TechDoc techDoc) throws SQLException {
        techDocDAO.updateTechDoc(techDoc);
    }

    /**
     * A method that deletes a tech document.
     */
    public void deleteTechDoc(TechDoc techDoc) throws SQLException {
        techDocDAO.deleteTechDoc(techDoc);
    }

    /**
     * A method that gets a specific tech document.
     */
    public TechDoc getTechDoc(TechDoc techDoc) throws SQLException {
        return techDocDAO.getTechdoc(techDoc);
    }

    /**
     * A method that updates the filepath of a drawing to a specified tech document.
     */
    public void updateDrawing(String filePath, TechDoc techDoc) throws SQLException {
        techDocDAO.updateDrawing(filePath, techDoc);
    }

    /**
     * A method used to add a tech picture to a specified tech document.
     */
    public Pictures addTechPictures(Pictures pictures, TechDoc techDoc) throws SQLException {
        return techDocDAO.addTechPictures(pictures, techDoc);
    }

    /**
     * A method that deletes a picture.
     */
    public void deletePicture(Pictures pictures) throws SQLException {
        techDocDAO.deletePicture(pictures);
    }

    /**
     * A method that adds a device to a specific tech document.
     */
    public Device addDevice(Device device, TechDoc techDoc) throws SQLException {
        return techDocDAO.addDevice(device, techDoc);
    }

    /**
     * A method that retrieves a list of devices associated to a specific tech document.
     */
    public List<Device> getDevices(TechDoc techDoc) throws Exception{
        TechDocDAO devices = new TechDocDAO();
        return devices.returnDevices(techDoc);
    }

    /**
     * A method that deletes a device.
     */
    public void deleteDevice(Device device) throws SQLException {
        techDocDAO.deleteDevice(device);
    }

    /**
     * A method used to check for an expiration date.
     */
    public void expirationDate() throws SQLException {
        techDocDAO.expirationDate();
    }

    /**
     * A method used to log onto the application.
     */
    public User login(String username) throws Exception {
        return loginDAO.login(username);
    }
}
