package GUI.Controller;

import BE.TechDoc;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TechDocEditorController extends BaseController{
    @FXML
    private TextArea taDeviceInfo;
    @FXML
    private TextArea taSetupDescription;
    @FXML
    private TextField tfTitle;
    @FXML
    private Button btnClose;
    private TechDoc techDoc;

    @Override
    public void setup() throws Exception {
        fillFields();
    }

    private void fillFields() {

    }

    @FXML
    private void handleClose(ActionEvent actionEvent) {
        closeWindow(btnClose);
    }

public void setTechDoc(TechDoc techDoc) {
        this.techDoc = techDoc;
    }

    @FXML
    private void handleSave(ActionEvent actionEvent) {
    }
}
