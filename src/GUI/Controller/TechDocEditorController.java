package GUI.Controller;

import BE.TechDoc;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class TechDocEditorController extends BaseController{
    @FXML
    private Button btnClose;
    private TechDoc techDoc;

    @Override
    public void setup() throws Exception {

    }

    @FXML
    private void handleClose(ActionEvent actionEvent) {
        closeWindow(btnClose);
    }

    public void setTechDoc(TechDoc techDoc) {
        this.techDoc = techDoc;
    }
}
