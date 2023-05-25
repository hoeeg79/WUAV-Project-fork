package GUI.Controller;

import BE.Pictures;
import BE.TechDoc;
import GUI.Model.TechDocModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;

public class PictureDescriptionController extends BaseController{
    @FXML
    private TextField txtFilePath;
    @FXML
    private Button btnCloseDescription;
    @FXML
    private Button btnSaveDescription;
    @FXML
    private TextArea txtPictureDescription;
    @FXML
    private Button btnBrowse;
    private File selectedFile;
    private Pictures picture;
    private TechDoc techDoc;

    /**
     * Setup is a method inherited by BaseController.
     * Used to instantiate the TechDocModel.
     */
    @Override
    public void setup() {
        try {
            super.setTModel(new TechDocModel());
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * Closes the window.
     */
    @FXML
    private void handleCloseDescription(ActionEvent actionEvent) {
        closeWindow(btnCloseDescription);
    }


    /**
     * Creates a new picture and sets its description and adds it to the tech-doc.
     */
    @FXML
    private void handleSaveDescription(ActionEvent actionEvent) {
        try {
            picture = new Pictures(selectedFile.toURI().toString());
            picture.setDescription(txtPictureDescription.getText());
            super.getTModel().addTechPictures(picture, techDoc);
            closeWindow(btnSaveDescription);
        } catch (SQLException e) {
            displayError(e);
        }
    }

    /**
     * Opens a filechooser that allows the user to pick an image file to add to the tech-doc.
     */
    @FXML
    private void handleBrowse(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Picture");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Files", "*.png", "*.jpg", "*.jpeg"));
        Stage stage = (Stage) btnBrowse.getScene().getWindow();
        selectedFile = fileChooser.showOpenDialog(stage);
        txtFilePath.setText(selectedFile.getPath());
    }

    /**
     * A setter for the Tech document.
     */
    public void setTechDoc(TechDoc techDoc) {
        this.techDoc = techDoc;
    }
}
