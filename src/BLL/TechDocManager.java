package BLL;

import BE.*;
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

//    public void deletePictures(Pictures pictures) throws SQLException {
//        facadeDAL.deletePicture(pictures);
//    }

    public void deleteTechDoc(TechDoc techDoc) throws SQLException {
        facadeDAL.deleteTechdoc(techDoc);
    }

    public void updateDrawing(String filePath, TechDoc techDoc) throws SQLException {
        facadeDAL.updateDrawing(filePath, techDoc);
    }

    public TechDoc getTechDoc(TechDoc techDoc) throws SQLException {
        return facadeDAL.getTechDoc(techDoc);
    }

    public Device addDevice(Device device, TechDoc techDoc) throws SQLException {
        return facadeDAL.addDevice(device, techDoc);
    }

    public List<Device> getDevices(TechDoc techDoc) throws Exception{
        return facadeDAL.getDevices(techDoc);
    }

    public void deleteDevice(Device device) throws SQLException {
        facadeDAL.deleteDevice(device);
    }
}
