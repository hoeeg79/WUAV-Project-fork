package GUI.Model;

import BE.Customer;
import BE.Pictures;
import BE.TechDoc;
import BE.User;
import BLL.TechDocManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class TechDocModel {
    private final TechDocManager techDocManager;
    private final ObservableList<TechDoc> techDocList;
    private final ObservableList<Pictures> techPictures;

    public TechDocModel() throws Exception {
        techDocManager = new TechDocManager();
        techDocList = FXCollections.observableArrayList();
        techPictures = FXCollections.observableArrayList();
    }

    public TechDoc createTechDoc(TechDoc techDoc) throws SQLException {
        TechDoc newDoc = techDocManager.createTechDoc(techDoc);
        techDocList.add(newDoc);
        return newDoc;
    }

    public void addTech(TechDoc techDoc, User user) throws SQLException {
        techDocManager.addTech(techDoc, user);
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

}
