package GUI.Controller;

import GUI.Model.CustomerModel;
import GUI.Model.TechDocModel;
import GUI.Model.UsersModel;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public abstract class BaseController {

    private CustomerModel CModel;

    private UsersModel UModel;
    private TechDocModel TModel;

    public abstract void setup() throws Exception;

    public void setCModel(CustomerModel cModel) {
        this.CModel = cModel;
    }

    public void setUModel(UsersModel uModel){
        this.UModel = uModel;
    }

    public CustomerModel getCModel(){
        return CModel;
    }

    public UsersModel getUModel() {
        return UModel;
    }

    public TechDocModel getTModel() {
        return TModel;
    }

    public void setTModel(TechDocModel TModel) {
        this.TModel = TModel;
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
