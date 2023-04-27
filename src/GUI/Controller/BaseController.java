package GUI.Controller;

import BLL.CustomerManager;
import GUI.Model.CustomerModel;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public abstract class BaseController {

    private CustomerModel CModel;

    public abstract void setup() throws Exception;

    public void setCModel(CustomerModel cModel) {
        this.CModel = cModel;
    }

    public CustomerModel getCModel(){
        return CModel;
    }

    public void displayError(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }

    public void closeWindow(Button btn) {
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();
    }

}
