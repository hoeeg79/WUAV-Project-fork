package DAL;

public class FacadeDAL {
    private CustomerDAO customerDAO;

    public FacadeDAL() throws Exception {
        customerDAO = new CustomerDAO();
    }
}
