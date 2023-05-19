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
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;


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
    private Pattern emailPattern;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * A method inherited from the baseController class, used to instantiate models
     * and call methods from the class.
     * @throws Exception
     */
    @Override
    public void setup() {
        try {
            loadList(super.getCModel());
            searchBar();
            clearCustomerMenu();
            checkUserType();
            btnCreateCustomer.setDisable(true);
            checkCreateCustomerFields();
            checkCustomerAddressFields();
            createEmailPattern(tfCustomerEmail);
            addNumericalListener(tfCustomerPhonenumber);
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void setUser(User user){
        this.user = user;
    }

    /**
     * a button that opens/closes the customer menu and clears the text fields.
     * @param actionEvent
     */
    @FXML
    private void handleCreateCustomersMenu(ActionEvent actionEvent) {
        customerMenu();
        clearCustomerMenu();
    }

    /**
     * A button used to create customers, with the specified information.
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    private void handleCreateCustomer(ActionEvent actionEvent) {
        try {
            String name = tfCustomerName.getText();
            String email = tfCustomerEmail.getText();
            String tlf = tfCustomerPhonenumber.getText();
            String streetName = tfCustomerStreetName.getText();
            String zipcode = tfCustomerZipcode.getText();
            String city = tfCustomerCity.getText();

            Customer customer = new Customer(name, email, tlf, streetName, zipcode, city);

            if (!checkEmailPattern(customer)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Email is wrong");
                alert.setContentText("Please supply a correct email address");
                alert.show();
                return;
            }

            if (!super.getCModel().createCustomer(customer)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Customer Exist");
                alert.setContentText("Customer Already Exists, please give it a different name");
                alert.show();
            } else {
                clearCustomerMenu();
                customerMenu();
            }
            //refreshList();
        }
        catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * A button used to close the customer menu.
     * @param actionEvent
     */
    @FXML
    private void handleCancelCustomer(ActionEvent actionEvent) {
        customerMenu();
    }

    /**
     * A method used to clear the text fields of the customer menu.
     */
    private void clearCustomerMenu(){
        tfCustomerName.clear();
        tfCustomerEmail.clear();
        tfCustomerPhonenumber.clear();
        tfCustomerStreetName.clear();
        tfCustomerZipcode.clear();
        tfCustomerCity.clear();
    }

    /**
     * A method used to slide the customer menu in or out, making it visible or invisible.
     */
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

    /**
     * A button that opens the create user view.
     */
    @FXML
    public void handleCreateUsers(ActionEvent actionEvent) {
        try {
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
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * A method that adds a listener to the search bar.
     */
    private void searchBar(){
        tfSearchBar.textProperty().addListener(((observable, oldValue, newValue) -> {
            try{
                super.getCModel().searchCustomer(newValue);
            }catch(Exception e){
                displayError(e);
            }
        }));
    }

    /**
     * A method used to open the customer view.
     */
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
            double currentStageWidth = currentStage.getWidth();
            double currentStageHeight = currentStage.getHeight();

            currentStage.setScene(new Scene(root));
            if (!currentStage.isFullScreen()) {
                currentStage.setWidth(currentStageWidth);
                currentStage.setHeight(currentStageHeight);
                currentStage.centerOnScreen();
            }

            currentStage.show();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load CustomerView.fxml");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    /**
     * A button used to delete a customer.
     */
    @FXML
    private void handleDeleteCustomer(ActionEvent actionEvent) {
        try {
            super.getCModel().deleteCustomer(tvMain.getSelectionModel().getSelectedItem());
            refreshList();
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * A button used to open the customer view.
     * @param actionEvent
     */
    @FXML
    private void handleOpenCustomer(ActionEvent actionEvent) {
        setSceneSelectCompany(btnOpenCustomer, tvMain.getSelectionModel().getSelectedItem());
    }

    /**
     * A logout button, in case you want to change user.
     * @param actionEvent
     */
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

    /**
     * Checks which user type is logged into the program, enabling or disabling buttons dependent on access.
     */
    private void checkUserType() {
        try {
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
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * a method that calls two other methods. One for filling the table columns, and the other to refresh the lists.
     */
    private void loadList(CustomerModel model) {
        try {
            prepareCells();
            refreshList();
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * Fills the main view table column with name, email, phone number, street name, zipcode and city.
     */
    private void prepareCells() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("tlf"));
        tcStreetName.setCellValueFactory(new PropertyValueFactory<>("streetName"));
        tcZipcode.setCellValueFactory(new PropertyValueFactory<>("zipcode"));
        tcCity.setCellValueFactory(new PropertyValueFactory<>("city"));
    }

    /**
     * A method used to refresh lists.
     */
    private void refreshList() {
        try {
            tvMain.getItems().clear();
            tvMain.setItems(super.getCModel().getObservableCustomers());
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * a method that calls the customer model check customer for docs
     * After that it calls the two methods docsForApprovalNotifier, and customerHighlighter.
     */
    private void checkCustomers() {
        try {
            if (super.getCModel().checkCustomerForDocs()) {

                docsForApprovalNotifier();
                customerHighlighter();

            }
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * A notification that says if there are documents ready for approval
     */
    private void docsForApprovalNotifier() {
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

    /**
     * A method that highlights a document that is ready for approval. Once it have been approved it removes the highlight.
     */
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

    /**
     * A method that calls the expiration date method from the tech documents model.
     */
    protected void expirationDate() {
        try {
            super.setTModel(new TechDocModel());
            super.getTModel().expirationDate();
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * A method that adds a listener to the customer name, email and phoneNumber fields,
     * returns true if they are not empty, else return false.
     */
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

    /**
     * A method that checks Customer name, email and phoneNumber, returns false if there is nothing in the fields
     */
    private boolean checksForGeneralInfo() {
        if (txtInCustomerName && txtInCustomerEmail && txtInCustomerPhoneNumber) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * A method that adds a listener to all the address fields, returns false if empty, else true.
     */
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

    /**
     * Returns true if the text fields for streetname, zipcode and city are filled, else it will return false
     * @return
     */
    private boolean checksForAddress() {
        if (txtInCustomerStreetName && txtInCustomerZipcode && txtInCustomerCity) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Enables or disables a button dependent on whether checksForGenelInfo & checksForAddress are true or not.
     */
    private void enableTheButtons() {
        if (checksForGeneralInfo() && checksForAddress()) {
            btnCreateCustomer.setDisable(false);
        }
        else {
            btnCreateCustomer.setDisable(true);
        }
    }

    /**
     * A boolean that checks if the emailPattern matches the email from Customer.
     */
    private boolean checkEmailPattern(Customer customer) {
        return emailPattern.matcher(customer.getEmail()).matches();
    }

    /**
     * States that an email pattern containts the given letters, numbers and signs.
     */
    private void createEmailPattern(TextField textField){
        emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", Pattern.CASE_INSENSITIVE);
    }

    /**
     * adds a listener to the text property, ensuring only numbers are allowed
     */
    private void addNumericalListener(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")){
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if(newValue.length() >= 8){
                textField.setText(newValue.substring(0, 8));
            }
        });
    }
}
