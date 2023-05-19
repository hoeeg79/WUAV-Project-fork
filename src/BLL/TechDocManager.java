package BLL;

import BE.*;
import DAL.FacadeDAL;

import java.sql.SQLException;
import java.util.List;

public class TechDocManager {
    private final FacadeDAL facadeDAL;

    /**
     * Constructor used to create a new instance of FacadeDAL.
     */
    public TechDocManager() throws Exception {
        facadeDAL = new FacadeDAL();
    }

    /**
     * A method that creates a new TechDoc object in the database and returns it.
     */
    public TechDoc createTechDoc(TechDoc techDoc) throws SQLException {
        return facadeDAL.createTechDoc(techDoc);
    }

    /**
     * A method that adds a TechDoc object to the database, together with a user.
     */
    public void addTech(TechDoc techDoc, User user) throws SQLException {
        facadeDAL.addTech(techDoc, user);
    }

    /**
     * A method that removes a techDoc object from the database.
     */
    public void removeTech(TechDoc techDoc, User user) throws SQLException {
        facadeDAL.removeTech(techDoc, user);
    }

    /**
     * A method that gets a list of TechDoc objects from the database.
     */
    public List<TechDoc> getTechDocs(Customer customer, User user) throws SQLException {
        return facadeDAL.getTechDocs(customer, user);
    }

    /**
     * A method that updates a techDoc object from the database.
     */
    public void updateTechDoc(TechDoc techDoc) throws SQLException {
        facadeDAL.updateTechDoc(techDoc);
    }

    /**
     * A method that adds a picture to a techDoc in the database.
     */
    public Pictures addTechPictures(Pictures pictures, TechDoc techDoc) throws SQLException {
        return facadeDAL.addTechPictures(pictures, techDoc);
    }

    /**
     * A method that deletes a picture object from the database.
     */
    public void deletePictures(Pictures pictures) throws SQLException {
        facadeDAL.deletePicture(pictures);
    }

    /**
     * A method that deletes a techDoc object from the database.
     */
    public void deleteTechDoc(TechDoc techDoc) throws SQLException {
        facadeDAL.deleteTechDoc(techDoc);
    }

    /**
     * A method that updates the drawing of a techDoc object in the database.
     */
    public void updateDrawing(String filePath, TechDoc techDoc) throws SQLException {
        facadeDAL.updateDrawing(filePath, techDoc);
    }

    /**
     * A method that gets the techDoc object from the database.
     */
    public TechDoc getTechDoc(TechDoc techDoc) throws SQLException {
        return facadeDAL.getTechDoc(techDoc);
    }

    /**
     * A method that adds a device object to a techDoc object in the database.
     */
    public Device addDevice(Device device, TechDoc techDoc) throws SQLException {
        return facadeDAL.addDevice(device, techDoc);
    }

    /**
     * A method that gets a list of device objects linked to a specific techDoc object.
     */
    public List<Device> getDevices(TechDoc techDoc) throws Exception{
        return facadeDAL.getDevices(techDoc);
    }

    /**
     * A method that deletes a device object from the database.
     */
    public void deleteDevice(Device device) throws SQLException {
        facadeDAL.deleteDevice(device);
    }

    /**
     * A method that checks the expiration date of the techDoc.
     */
    public void expirationDate() throws SQLException {
        facadeDAL.expirationDate();
    }
}
