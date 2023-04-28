package GUI.Controller;

import javafx.event.ActionEvent;

import BE.Customer;
import GUI.Model.CustomerModel;
import javafx.animation.TranslateTransition;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class MainViewController extends BaseController implements Initializable {
    @FXML
    private Button btnOpenCustomer;
    @FXML
    private Button btnDeleteCustomer;
    @FXML
    private ListView<Customer> lvPriv;
    @FXML
    private ListView<Customer> lvCorp;
    @FXML
    private ListView<Customer> lvGov;

    @FXML
    private ComboBox cbCustomerTypes;
    @FXML
    private TextField tfCustomerName;
    @FXML
    private TextField tfCustomerEmail;
    @FXML
    private TextField tfCustomerPhonenumber;
    @FXML
    private TextField tfCustomerImage;
    @FXML
    private Button btnCustomerImage;
    @FXML
    private Button btnCancelCustomer;
    @FXML
    private Button btnCreateCustomersMenu;
    @FXML
    private Button btnCreateCustomer;
    @FXML
    private TextField searchBar;
    @FXML
    private Pane createCustomerMenu;
    private Customer selectedCustomer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void setup() throws Exception {
        try {
            loadLists(super.getCModel());
        } catch (Exception e) {
            throw new Exception(e);
        }
        clearCustomerMenu();
        cbCustomerTypes.setItems(FXCollections.observableArrayList("Business", "Government", "Private"));
        changeSelectedCustomer();
    }

    @FXML
    private void handleCreateCustomersMenu(ActionEvent actionEvent) {
        customerMenu();
    }

    @FXML
    private void handleCreateCustomer(ActionEvent actionEvent) throws SQLException {
        String name = tfCustomerName.getText();
        String email = tfCustomerEmail.getText();
        String tlf = tfCustomerPhonenumber.getText();
        String image = tfCustomerImage.getText();
        int customerType = cbCustomerTypes.getSelectionModel().getSelectedIndex() + 1;

        Customer customer = new Customer(name, email, tlf, image, customerType);

        super.getCModel().createCustomer(customer);
    }

    @FXML
    private void handleCancelCustomer(ActionEvent actionEvent) {
        customerMenu();
    }

    private void clearCustomerMenu(){
        tfCustomerName.clear();
        tfCustomerEmail.clear();
        tfCustomerPhonenumber.clear();
        tfCustomerImage.clear();
    }

    private void customerMenu() {
        if (createCustomerMenu.isVisible()) {
            TranslateTransition slideOut = new TranslateTransition(Duration.seconds(0.5), createCustomerMenu);
            slideOut.setToX(-createCustomerMenu.getWidth());
            slideOut.setOnFinished(e -> createCustomerMenu.setVisible(false));
            slideOut.play();
        } else {
            createCustomerMenu.setTranslateX(-createCustomerMenu.getWidth());
            createCustomerMenu.toFront();
            createCustomerMenu.setVisible(true);

            TranslateTransition slideIn = new TranslateTransition(Duration.seconds(0.5), createCustomerMenu);
            slideIn.setToX(0);
            slideIn.play();
        }
    }

    @FXML
    public void handleCreateUsers(ActionEvent actionEvent) throws Exception {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/CreateUsersView.fxml"));
        Parent root = loader.load();

        CreateUsersController controller = loader.getController();
        controller.setup();

        stage.setScene(new Scene(root));
        stage.setTitle("Create User");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        stage.show();
    }

    private void loadLists(CustomerModel model) throws Exception {
        lvPriv.setItems(model.getPrivateCustomer());
        lvCorp.setItems(model.getBusinessCustomer());
        lvGov.setItems(model.getGovernmentCustomer());
    }

    @FXML
    private void handlePickImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(btnCustomerImage.getScene().getWindow());
        tfCustomerImage.setText(selectedFile.getAbsolutePath());
    }

    private void changeSelectedCustomer(){
        lvCorp.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) ->{
            if (newValue != null) {
                lvGov.getSelectionModel().clearSelection();
                lvPriv.getSelectionModel().clearSelection();
                selectedCustomer = newValue;
            }
        }));
        lvGov.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                lvCorp.getSelectionModel().clearSelection();
                lvPriv.getSelectionModel().clearSelection();
                selectedCustomer = newValue;
            }
        }));
        lvPriv.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                lvGov.getSelectionModel().clearSelection();
                lvCorp.getSelectionModel().clearSelection();
                selectedCustomer = newValue;
            }
        }));
    }

    private void setSceneSelectCompany(Button btn, Customer customer){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/CustomerView.fxml"));
            Parent root = loader.load();

            CustomerViewController controller = loader.getController();
            controller.setCustomer(customer);
            controller.setup();

            Stage currentStage = (Stage) btn.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load CustomerView.fxml");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDeleteCustomer(ActionEvent actionEvent) {
        
    }

    @FXML
    private void handleOpenCustomer(ActionEvent actionEvent) {
        setSceneSelectCompany(btnOpenCustomer, selectedCustomer);
    }
}
