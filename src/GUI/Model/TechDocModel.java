package GUI.Model;

import BE.*;
import BLL.TechDocManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;

public class TechDocModel {
    private final TechDocManager techDocManager;
    private final ObservableList<TechDoc> techDocList;
    private final ObservableList<Pictures> techPictures;
    private final ObservableList<Device> techDevice;

    public TechDocModel() throws Exception {
        techDocManager = new TechDocManager();
        techDocList = FXCollections.observableArrayList();
        techPictures = FXCollections.observableArrayList();
        techDevice = FXCollections.observableArrayList();
    }

    public TechDoc createTechDoc(TechDoc techDoc) throws SQLException {
        TechDoc newDoc = techDocManager.createTechDoc(techDoc);
        techDocList.add(newDoc);
        return newDoc;
    }

    public void addTech(TechDoc techDoc, User user) throws SQLException {
        techDocManager.addTech(techDoc, user);
    }

    public void removeTech(TechDoc techDoc, User user) throws SQLException {
        techDocManager.removeTech(techDoc, user);
    }

    public ObservableList<TechDoc> getTechDocs(Customer customer, User user) throws SQLException {
        techDocList.addAll(techDocManager.getTechDocs(customer, user));
        return techDocList;
    }

    public void updateTechDoc(TechDoc techDoc) throws SQLException {
        techDocManager.updateTechDoc(techDoc);
    }

    public Pictures addTechPictures(Pictures pictures, TechDoc techDoc) throws SQLException {
        Pictures newPicture = techDocManager.addTechPictures(pictures, techDoc);
        techPictures.add(newPicture);
        return newPicture;
    }

    public void deletePictures(Pictures pictures) throws SQLException {
        techDocManager.deletePictures(pictures);
        techPictures.remove(pictures);
    }

    public void deleteTechDoc(TechDoc techDoc) throws SQLException {
        techDocManager.deleteTechDoc(techDoc);
    }

    public void updateDrawing(String filePath, TechDoc techDoc) throws SQLException {
        techDocManager.updateDrawing(filePath, techDoc);
    }

    public TechDoc getTechDoc(TechDoc techDoc) throws SQLException {
        return techDocManager.getTechDoc(techDoc);
    }

    public Device addDevice(Device device, TechDoc techDoc) throws SQLException {
        Device newDevice = techDocManager.addDevice(device, techDoc);
        techDevice.add(newDevice);
        return newDevice;
    }

    public ObservableList<Device> getObservableDevices(TechDoc techDoc) throws Exception{
        techDevice.clear();
        techDevice.addAll(techDocManager.getDevices(techDoc));
        return techDevice;
    }

    public void deleteDevice(Device device) throws SQLException {
        techDocManager.deleteDevice(device);
    }

}
