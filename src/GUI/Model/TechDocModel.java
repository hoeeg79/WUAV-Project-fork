package GUI.Model;

import BE.*;
import BLL.TechDocManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class TechDocModel {
    private final TechDocManager techDocManager;
    private final ObservableList<TechDoc> techDocList;
    private final ObservableList<Pictures> techPictures;
    private final ObservableList<Device> techDevice;

    /**
     * A constructor of the TechDocModel, used to instantiate techDocManager and declare techDocList, Pictures and device
     * as an observableArrayList.
     */
    public TechDocModel() throws Exception {
        techDocManager = new TechDocManager();
        techDocList = FXCollections.observableArrayList();
        techPictures = FXCollections.observableArrayList();
        techDevice = FXCollections.observableArrayList();
    }

    /**
     * A method used to create a new tech document, and add it to the techDocList.
     */
    public TechDoc createTechDoc(TechDoc techDoc) throws SQLException {
        TechDoc newDoc = techDocManager.createTechDoc(techDoc);
        techDocList.add(newDoc);
        return newDoc;
    }

    /**
     * A method used to add a tech document to a user.
     */
    public void addTech(TechDoc techDoc, User user) throws SQLException {
        techDocManager.addTech(techDoc, user);
    }

    /**
     * A method used to remove a tech document from a user.
     */
    public void removeTech(TechDoc techDoc, User user) throws SQLException {
        techDocManager.removeTech(techDoc, user);
    }

    /**
     * A method used to retrieve a list of tech documents associated to specific customers and users.
     */
    public ObservableList<TechDoc> getTechDocs(Customer customer, User user) throws SQLException {
        techDocList.addAll(techDocManager.getTechDocs(customer, user));
        return techDocList;
    }

    /**
     * A method used to update a specific tech document.
     */
    public void updateTechDoc(TechDoc techDoc) throws SQLException {
        techDocManager.updateTechDoc(techDoc);
    }

    /**
     * A method used to add a picture to a specified tech document.
     */
    public Pictures addTechPictures(Pictures pictures, TechDoc techDoc) throws SQLException {
        Pictures newPicture = techDocManager.addTechPictures(pictures, techDoc);
        techPictures.add(newPicture);
        return newPicture;
    }

    /**
     * A method used to delete a picture from a tech document.
     */
    public void deletePictures(Pictures pictures) throws SQLException {
        techDocManager.deletePictures(pictures);
        techPictures.remove(pictures);
    }

    /**
     * A method used to delete a tech document.
     */
    public void deleteTechDoc(TechDoc techDoc) throws SQLException {
        techDocManager.deleteTechDoc(techDoc);
    }

    /**
     * A method used to update a drawings filepath with a specified tech document.
     */
    public void updateDrawing(String filePath, TechDoc techDoc) throws SQLException {
        techDocManager.updateDrawing(filePath, techDoc);
    }

    /**
     * A method that retrieves a tech document.
     */
    public TechDoc getTechDoc(TechDoc techDoc) throws SQLException {
        return techDocManager.getTechDoc(techDoc);
    }

    /**
     * A method used to add a device to a specified tech document.
     */
    public Device addDevice(Device device, TechDoc techDoc) throws SQLException {
        Device newDevice = techDocManager.addDevice(device, techDoc);
        techDevice.add(newDevice);
        return newDevice;
    }

    /**
     *  A method used to retrieve the device objects on a specified tech document.
     */
    public ObservableList<Device> getObservableDevices(TechDoc techDoc) throws Exception{
        techDevice.clear();
        techDevice.addAll(techDocManager.getDevices(techDoc));
        return techDevice;
    }

    /**
     * A method used to delete a device from a specified tech document.
     */
    public void deleteDevice(Device device) throws SQLException {
        techDocManager.deleteDevice(device);
    }

    /**
     *  A method used calculate when a tech document should automatically be deleted.
     */
    public void expirationDate() throws SQLException {
        techDocManager.expirationDate();
    }

}
