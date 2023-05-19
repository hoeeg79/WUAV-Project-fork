package DAL;

import BE.*;

import java.sql.SQLException;
import java.util.List;

public class FacadeDAL {
    private final CustomerDAO customerDAO;
    private final UsersDAO usersDAO;
    private final TechDocDAO techDocDAO;
    private final LoginDAO loginDAO;

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

    public void updateCustomer(Customer c) throws SQLException{
        customerDAO.updateCustomer(c);
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

    public void updateUser(User user) throws SQLException {
        usersDAO.updateUser(user);
    }

    public List<User> getLinkedUsers(TechDoc techdoc) throws SQLException {
        return usersDAO.getLinkedUsers(techdoc);
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

    public List<TechDoc> getTechDocs(Customer customer, User user) throws SQLException {
        return techDocDAO.getTechDocs(customer, user);
    }

    public void updateTechDoc(TechDoc techDoc) throws SQLException {
        techDocDAO.updateTechDoc(techDoc);
    }

    public void deleteTechDoc(TechDoc techDoc) throws SQLException {
        techDocDAO.deleteTechDoc(techDoc);
    }

    public TechDoc getTechDoc(TechDoc techDoc) throws SQLException {
        return techDocDAO.getTechdoc(techDoc);
    }

    public void updateDrawing(String filePath, TechDoc techDoc) throws SQLException {
        techDocDAO.updateDrawing(filePath, techDoc);
    }

    public Pictures addTechPictures(Pictures pictures, TechDoc techDoc) throws SQLException {
        return techDocDAO.addTechPictures(pictures, techDoc);
    }

    public void deletePicture(Pictures pictures) throws SQLException {
        techDocDAO.deletePicture(pictures);
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

    public User login(String username) throws Exception {
        return loginDAO.login(username);
    }
}
