package BLL;

import BE.TechDoc;
import DAL.FacadeDAL;

import java.sql.SQLException;

public class TechDocManager {
    private final FacadeDAL facadeDAL;

    public TechDocManager() throws Exception {
        facadeDAL = new FacadeDAL();
    }

    public TechDoc createTechDoc(TechDoc techDoc) throws SQLException {
        return facadeDAL.createTechDoc(techDoc);
    }
}
