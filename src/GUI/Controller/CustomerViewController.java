package GUI.Controller;

import BE.Customer;
import BE.TechDoc;
import GUI.Model.CustomerModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class CustomerViewController extends BaseController{
    @FXML
    private ListView<TechDoc> lvTechDocs;
    @FXML
    private Button btnHome;
    @FXML
    private TextField tfPictureFilepath;
    @FXML
    private Button btnPictureFinder;
    @FXML
    private ComboBox cbCustomerTypes;
    @FXML
    private Button btnEditCustomer;
    @FXML
    private Button btnCancelCustomer;
    @FXML
    private Button btnCreateCustomer;
    @FXML
    private TextField tfCustomerName;
    @FXML
    private TextField tfCustomerEmail;
    @FXML
    private TextField tfCustomerPhoneNumber;
    private Customer customer;

    @Override
    public void setup() throws Exception {
        lockFieldsAndButtons();
        cbCustomerTypes.setItems(FXCollections.observableArrayList("Business", "Government", "Private"));
        fillFields();
    }

    private void lockFieldsAndButtons() {
        tfCustomerEmail.setDisable(true);
        tfCustomerPhoneNumber.setDisable(true);
        tfCustomerName.setDisable(true);
        tfPictureFilepath.setDisable(true);
        btnCancelCustomer.setDisable(true);
        btnCreateCustomer.setDisable(true);
        btnPictureFinder.setDisable(true);
        cbCustomerTypes.setDisable(true);
    }

    private void fillFields() {
        tfCustomerEmail.setText(customer.getEmail());
        tfCustomerName.setText(customer.getName());
        tfCustomerPhoneNumber.setText(String.valueOf(customer.getTlf()));
        tfPictureFilepath.setText(customer.getPicture());
        cbCustomerTypes.getSelectionModel().select(customer.getCustomerType() - 1);
    }

    @FXML
    private void handleSave(ActionEvent actionEvent) {
        btnEditCustomer.setDisable(false);
        lockFieldsAndButtons();

        int customerType = cbCustomerTypes.getSelectionModel().getSelectedIndex() + 1;
    }

    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        btnEditCustomer.setDisable(false);
        fillFields();
        lockFieldsAndButtons();
    }

    @FXML
    private void handleEdit(ActionEvent actionEvent) {
        btnEditCustomer.setDisable(true);
        tfCustomerEmail.setDisable(false);
        tfCustomerPhoneNumber.setDisable(false);
        tfCustomerName.setDisable(false);
        tfPictureFilepath.setDisable(false);
        btnCancelCustomer.setDisable(false);
        btnCreateCustomer.setDisable(false);
        btnPictureFinder.setDisable(false);
        cbCustomerTypes.setDisable(false);
    }

    @FXML
    private void handlePictureFinder(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Picture");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Files", "*.png", "*.jpg", "*.jpeg"));
        Stage stage = (Stage) btnPictureFinder.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            tfPictureFilepath.setText(String.valueOf(selectedFile));
        }
    }

    @FXML
    private void handleHome(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/MainView.fxml"));
            Parent root = loader.load();

            MainViewController controller = loader.getController();
            controller.setCModel(new CustomerModel());
            controller.setup();

            Stage currentStage = (Stage) btnHome.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load MainView.fxml");
            alert.showAndWait();
        }
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    private void openSelection() {
        lvTechDocs.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            openTechDocEditor(btnCreateCustomer, newValue);
        }));
    }

    private void openTechDocEditor(Button btn, TechDoc techDoc){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/CustomerView.fxml"));
            Parent root = loader.load();

            TechDocEditorController controller = loader.getController();
            controller.setTechDoc(techDoc);
            controller.setup();

            Stage currentStage = (Stage) btn.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();
            //Skal vi åbne et nyt vindue, og lade det andet være brugbart samtidigt?

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load CustomerView.fxml");
            alert.showAndWait();
        }
    }
}
