package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class PictureDescriptionController extends BaseController{
    public Button btnCloseDescription;
    public Button btnSaveDescription;
    public TextArea txtPictureDescription;

    @Override
    public void setup() throws Exception {

    }

    public void handleCloseDescription(ActionEvent actionEvent) {
        closeWindow(btnCloseDescription);
    }

    public void handleSaveDescription(ActionEvent actionEvent) {
    }
}
