package BLL;

import BE.Customer;
import BE.Pictures;
import BE.TechDoc;
import BE.User;
import DAL.FacadeDAL;

import java.sql.SQLException;
import java.util.List;

public class TechDocManager {
    private final FacadeDAL facadeDAL;

    public TechDocManager() throws Exception {
        facadeDAL = new FacadeDAL();
    }

    public TechDoc createTechDoc(TechDoc techDoc) throws SQLException {
        return facadeDAL.createTechDoc(techDoc);
    }

    public void addTech(TechDoc techDoc, User user) throws SQLException {
        facadeDAL.addTech(techDoc, user);
    }
    public List<TechDoc> getTechDocs(Customer customer, User user) throws SQLException {
        return facadeDAL.getTechDocs(customer, user);
    }

    public void updateTechDoc(TechDoc techDoc) throws SQLException {
        facadeDAL.updateTechDoc(techDoc);
    }

    public Pictures addTechPictures(Pictures pictures, TechDoc techDoc) throws SQLException {
        return facadeDAL.addTechPictures(pictures, techDoc);
    }

    public void deletePictures(Pictures pictures) throws SQLException {
        facadeDAL.deletePicture(pictures);
    }

    public void deleteTechDoc(TechDoc techDoc) throws SQLException {
        facadeDAL.deleteTechdoc(techDoc);
    }
}
