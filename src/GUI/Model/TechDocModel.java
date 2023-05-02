package GUI.Model;

import BE.Customer;
import BE.TechDoc;
import BE.User;
import BLL.TechDocManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class TechDocModel {
    private final TechDocManager techDocManager;
    private final ObservableList<TechDoc> techDocList;
    public TechDocModel() throws Exception {
        techDocManager = new TechDocManager();
        techDocList = FXCollections.observableArrayList();
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
}
