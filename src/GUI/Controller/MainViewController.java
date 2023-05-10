package GUI.Controller;

import BE.User;
import GUI.Model.UsersModel;
import javafx.event.ActionEvent;

import BE.Customer;
import GUI.Model.CustomerModel;
import javafx.animation.TranslateTransition;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TableView tvMain;
    @FXML
    private TableColumn tcName;
    @FXML
    private TableColumn tcEmail;
    @FXML
    private TableColumn tcPhoneNumber;
    @FXML
    private TableColumn tcStreetName;
    @FXML
    private TableColumn tcZipcode;
    @FXML
    private Button btnCreateUsers;
    @FXML
    private Button btnLogOut;
    @FXML
    private Button btnOpenCustomer;
    @FXML
    private Button btnDeleteCustomer;

    @FXML
    private TextField tfCustomerName;
    @FXML
    private TextField tfCustomerEmail;
    @FXML
    private TextField tfCustomerPhonenumber;
    @FXML
    private TextField tfCustomerStreetName;
    @FXML
    private TextField tfCustomerZipcode;
    @FXML
    private TextField tfSearchBar;
    @FXML
    private Button btnCancelCustomer;
    @FXML
    private Button btnCreateCustomersMenu;
    @FXML
    private Button btnCreateCustomer;
    @FXML
    private Pane createCustomerMenu;
    private Customer selectedCustomer;
    private User user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void setup() throws Exception {
        try {
            loadLists(super.getCModel());
            searchBar();
            clearCustomerMenu();
            checkUserType();
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void setUser(User user){
        this.user = user;
    }

    @FXML
    private void handleCreateCustomersMenu(ActionEvent actionEvent) {
        customerMenu();
        clearCustomerMenu();
    }

    @FXML
    private void handleCreateCustomer(ActionEvent actionEvent) throws Exception {
        String name = tfCustomerName.getText();
        String email = tfCustomerEmail.getText();
        String tlf = tfCustomerPhonenumber.getText();
        String streetName = tfCustomerStreetName.getText();
        String zipcode = tfCustomerZipcode.getText();

        Customer customer = new Customer(name, email, tlf, streetName, zipcode);

        super.getCModel().createCustomer(customer);
        clearCustomerMenu();
        customerMenu();
        tvMain.getItems().clear();
        tvMain.setItems(super.getCModel().getObservableCustomers());
    }

    @FXML
    private void handleCancelCustomer(ActionEvent actionEvent) {
        customerMenu();
    }

    private void clearCustomerMenu(){
        tfCustomerName.clear();
        tfCustomerEmail.clear();
        tfCustomerPhonenumber.clear();
        tfCustomerStreetName.clear();
        tfCustomerZipcode.clear();
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
        stage.centerOnScreen();
        stage.show();
    }

    private void loadLists(CustomerModel model) throws Exception {
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("tlf"));
        tcStreetName.setCellValueFactory(new PropertyValueFactory<>("streetName"));
        tcZipcode.setCellValueFactory(new PropertyValueFactory<>("zipcode"));

        tvMain.getItems().clear();
        tvMain.setItems(super.getCModel().getObservableCustomers());
    }

    private void searchBar(){
        tfSearchBar.textProperty().addListener(((observable, oldValue, newValue) -> {
            try{
                super.getCModel().searchCustomer(newValue);
            }catch(Exception e){
                displayError(e);
            }
        }));
    }

    private void setSceneSelectCompany(Button btn, Customer customer){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/CustomerView.fxml"));
            Parent root = loader.load();

            CustomerViewController controller = loader.getController();
            controller.setCustomer(customer);
            controller.setUModel(new UsersModel());
            controller.setUser(user);
            controller.setup();

            Stage currentStage = (Stage) btn.getScene().getWindow();
            double currentWidth = currentStage.getWidth();
            double currentHeight = currentStage.getHeight();

            currentStage.setScene(new Scene(root));
            currentStage.setWidth(currentWidth);
            currentStage.setHeight(currentHeight);

            currentStage.show();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load CustomerView.fxml");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteCustomer(ActionEvent actionEvent) throws SQLException {
        super.getCModel().deleteCustomer(selectedCustomer);
    }

    @FXML
    private void handleOpenCustomer(ActionEvent actionEvent) {
        setSceneSelectCompany(btnOpenCustomer, selectedCustomer);
    }

    @FXML
    private void handleLogOut(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) btnLogOut.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("GUI/View/LoginView.fxml"));

            stage.setScene(new Scene(root));
            stage.setTitle("WUAV Documentation System");
            stage.show();
        } catch (Exception e) {
            displayError(e);
        }
    }

    private void checkUserType() {
        if (user.getUserType().getId() == 2) {
            btnCreateCustomersMenu.setVisible(false);
            btnDeleteCustomer.setVisible(false);
            btnCreateUsers.setVisible(false);
        } else if (user.getUserType().getId() == 1) {
            btnCreateCustomersMenu.setVisible(false);
            btnDeleteCustomer.setVisible(false);
        } else {
            btnCreateUsers.setVisible(false);
        }
    }
}
