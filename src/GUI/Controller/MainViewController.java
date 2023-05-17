package GUI.Controller;

import BE.User;
import GUI.Model.TechDocModel;
import GUI.Model.UsersModel;
import javafx.application.Platform;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;


public class MainViewController extends BaseController implements Initializable {


    @FXML
    private Pane docApprovalNotification;
    @FXML
    private TableView<Customer> tvMain;
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
    private TableColumn tcCity;
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
    private TextField tfCustomerCity;
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
    private User user;
    private boolean txtInCustomerName;
    private boolean txtInCustomerEmail;
    private boolean txtInCustomerPhoneNumber;
    private boolean txtInCustomerStreetName;
    private boolean txtInCustomerZipcode;
    private boolean txtInCustomerCity;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void setup() throws Exception {
        try {
            loadList(super.getCModel());
            searchBar();
            clearCustomerMenu();
            checkUserType();
            btnCreateCustomer.setDisable(true);
            checkCreateCustomerFields();
            checkCustomerAddressFields();
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
        String city = tfCustomerCity.getText();

        Customer customer = new Customer(name, email, tlf, streetName, zipcode, city);

        super.getCModel().createCustomer(customer);
        clearCustomerMenu();
        customerMenu();
        refreshList();
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
        tfCustomerCity.clear();
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
    private void handleDeleteCustomer(ActionEvent actionEvent) throws Exception {
        super.getCModel().deleteCustomer(tvMain.getSelectionModel().getSelectedItem());
        refreshList();
    }

    @FXML
    private void handleOpenCustomer(ActionEvent actionEvent) {
        setSceneSelectCompany(btnOpenCustomer, tvMain.getSelectionModel().getSelectedItem());
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

    private void checkUserType() throws Exception {
        if (user.getUserType().getId() == 2) {
            btnCreateCustomersMenu.setVisible(false);
            btnDeleteCustomer.setVisible(false);
            btnCreateUsers.setVisible(false);
        } else if (user.getUserType().getId() == 1) {
            btnCreateCustomersMenu.setVisible(false);
            btnDeleteCustomer.setVisible(false);
            checkCustomers();
        } else {
            btnCreateUsers.setVisible(false);
            checkCustomers();
        }
    }

    private void loadList(CustomerModel model) throws Exception {
        prepareCells();

        refreshList();
    }

    private void prepareCells() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("tlf"));
        tcStreetName.setCellValueFactory(new PropertyValueFactory<>("streetName"));
        tcZipcode.setCellValueFactory(new PropertyValueFactory<>("zipcode"));
        tcCity.setCellValueFactory(new PropertyValueFactory<>("city"));
    }

    private void refreshList() throws Exception {
        tvMain.getItems().clear();
        tvMain.setItems(super.getCModel().getObservableCustomers());
    }

    private void checkCustomers() throws Exception {
        if (super.getCModel().checkCustomerForDocs()) {
            docsForApprovalNotifier();
            customerHighlighter();
        }
    }

    private void docsForApprovalNotifier() throws Exception {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
                Platform.runLater(() -> {
                    docApprovalNotification.setTranslateY(docApprovalNotification.getHeight());
                    docApprovalNotification.toFront();
                    docApprovalNotification.setVisible(true);

                    TranslateTransition slideIn = new TranslateTransition(Duration.seconds(1), docApprovalNotification);
                    slideIn.setToY(0);
                    slideIn.play();
                });

            } catch (Exception e) {
                displayError(e);
            }
        });
        thread.start();
    }

    private void customerHighlighter() {
        tcName.setCellFactory(column -> new TableCell<Customer, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    Customer customer = getTableView().getItems().get(getIndex());
                    setText(item);

                    if (customer.isDocReadyForApproval()) {
                        setStyle("-fx-font-weight: bold;-fx-text-fill: red; -fx-font-size: 13");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
    }

    protected void expirationDate() throws Exception {
        super.setTModel(new TechDocModel());
        super.getTModel().expirationDate();
    }
    private void checkCreateCustomerFields() {
        tfCustomerName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                txtInCustomerName = true;
            } else {
                txtInCustomerName = false;
            }
            enableTheButtons();
        });

        tfCustomerEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                txtInCustomerEmail = true;
            } else {
                txtInCustomerEmail = false;
            }
            enableTheButtons();
        });

        tfCustomerPhonenumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                txtInCustomerPhoneNumber = true;
            } else {
                txtInCustomerPhoneNumber = false;
            }
            enableTheButtons();
        });
    }

    private boolean checksForGeneralInfo() {
        if (txtInCustomerName && txtInCustomerEmail && txtInCustomerPhoneNumber) {
            return true;
        }
        else {
            return false;
        }
    }

    private void checkCustomerAddressFields() {
        tfCustomerStreetName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                txtInCustomerStreetName = true;
            } else {
                txtInCustomerStreetName = false;
            }
            enableTheButtons();
        });

        tfCustomerZipcode.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                txtInCustomerZipcode = true;
            } else {
                txtInCustomerZipcode = false;
            }
            enableTheButtons();
        });

        tfCustomerCity.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                txtInCustomerCity = true;
            } else {
                txtInCustomerCity = false;
            }
            enableTheButtons();
        });
    }

    private boolean checksForAddress() {
        if (txtInCustomerStreetName && txtInCustomerZipcode && txtInCustomerCity) {
            return true;
        } else {
            return false;
        }
    }

    private void enableTheButtons() {
        if (checksForGeneralInfo() && checksForAddress()) {
            btnCreateCustomer.setDisable(false);
        }
        else {
            btnCreateCustomer.setDisable(true);
        }
    }
}
