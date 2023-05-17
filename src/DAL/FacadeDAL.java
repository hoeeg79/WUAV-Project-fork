package DAL;

import BE.*;

import java.sql.SQLException;
import java.util.List;

public class FacadeDAL {
    private CustomerDAO customerDAO;
    private UsersDAO usersDAO;
    private TechDocDAO techDocDAO;
    private LoginDAO loginDAO;

    public FacadeDAL() throws Exception {
        customerDAO = new CustomerDAO();
        usersDAO = new UsersDAO();
        techDocDAO = new TechDocDAO();
        loginDAO = new LoginDAO();
    }

    public Customer createCustomer(Customer customer) throws SQLException {
        if (customerDAO.checkCustomer(customer)) {
            return null;
        }

        return customerDAO.createCustomer(customer);
    }

    public void deleteCustomer(Customer customer) throws SQLException {
        customerDAO.deleteCustomer(customer);
    }

    public List<Customer> getCustomers() throws Exception{
        CustomerDAO customers = new CustomerDAO();
        return customers.returnCustomers();
    }

    public User createUser(User user) throws SQLException {
        return usersDAO.createUser(user);
    }

    public void deleteUser(User user) throws SQLException {
        usersDAO.deleteUser(user);
    }

    public List<User> getUsers() throws SQLException {
        return usersDAO.returnUsers();
    }

    public void updateCustomer(Customer c) throws SQLException{
        customerDAO.updateCustomer(c);
    }

    public TechDoc createTechDoc(TechDoc techDoc) throws SQLException {
        return techDocDAO.createTechDoc(techDoc);
    }

    public void addTech(TechDoc techDoc, User user) throws SQLException {
        techDocDAO.addTech(techDoc, user);
    }

    public void removeTech(TechDoc techDoc, User user) throws SQLException {
        techDocDAO.removeTech(techDoc, user);
    }

    public User login(String username) throws Exception {
        return loginDAO.login(username);
    }

    public void updateUser(User user) throws SQLException {
        usersDAO.updateUser(user);
    }

    public List<TechDoc> getTechDocs(Customer customer, User user) throws SQLException {
        return techDocDAO.getTechDocs(customer, user);
    }

    public void updateTechDoc(TechDoc techDoc) throws SQLException {
        techDocDAO.updateTechDoc(techDoc);
    }

    public List<User> getLinkedUsers(TechDoc techdoc) throws SQLException {
        return usersDAO.getLinkedUsers(techdoc);
    }

    public Pictures addTechPictures(Pictures pictures, TechDoc techDoc) throws SQLException {
        return techDocDAO.addTechPictures(pictures, techDoc);
    }


    public void deletePicture(Pictures pictures) throws SQLException {
        techDocDAO.deletePicture(pictures);
    }

    public void deleteTechdoc(TechDoc techDoc) throws SQLException {
        techDocDAO.deleteTechDoc(techDoc);
    }

    public void updateDrawing(String filePath, TechDoc techDoc) throws SQLException {
        techDocDAO.updateDrawing(filePath, techDoc);
    }

    public TechDoc getTechDoc(TechDoc techDoc) throws SQLException {
        return techDocDAO.getTechdoc(techDoc);
    }

    public Device addDevice(Device device, TechDoc techDoc) throws SQLException {
        return techDocDAO.addDevice(device, techDoc);
    }

    public List<Device> getDevices(TechDoc techDoc) throws Exception{
        TechDocDAO devices = new TechDocDAO();
        return devices.returnDevices(techDoc);
    }

    public void deleteDevice(Device device) throws SQLException {
        techDocDAO.deleteDevice(device);
    }

    public void expirationDate() throws SQLException {
        techDocDAO.expirationDate();
    }
}
