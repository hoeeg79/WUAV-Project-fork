package GUI.Controller;

import BE.Customer;
import BE.TechDoc;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class TechDocEditorController extends BaseController{
    @FXML
    private Button btnClose;
    private TechDoc techDoc;
    private Customer customer;

    @Override
    public void setup() throws Exception {

    }

    @FXML
    private void handleClose(ActionEvent actionEvent) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/CustomerView.fxml"));
        Parent root = loader.load();

        CustomerViewController controller = loader.getController();
        controller.setCustomer(customer);
        controller.setup();

        Stage currentStage = (Stage) btnClose.getScene().getWindow();
        currentStage.setScene(new Scene(root));
        currentStage.show();
    }

    public void setTechDoc(TechDoc techDoc) {
        this.techDoc = techDoc;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
