package GUI.Model;

import BE.TechDoc;
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

    public void createTechDoc(TechDoc techDoc) throws SQLException {
        TechDoc newDoc = techDocManager.createTechDoc(techDoc);
        techDocList.add(newDoc);
    }
}
