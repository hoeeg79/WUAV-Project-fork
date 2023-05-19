package GUI.Controller;

import BE.Customer;
import BE.TechDoc;
import BE.User;
import GUI.Model.CustomerModel;
import GUI.Model.TechDocModel;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class CustomerViewController extends BaseController{

    @FXML
    private Pane paneRight;
    @FXML
    private Pane addTechMenu;
    @FXML
    private ListView<User> lvTechsWorking;
    @FXML
    private ListView<User> lvTechs;
    @FXML
    private ListView<TechDoc> lvTechDocs;
    @FXML
    private Button btnDeleteTechDoc;
    @FXML
    private Button btnManageTech;
    @FXML
    private Button btnRemoveTech;
    @FXML
    private Button btnAddTech;
    @FXML
    private Button btnCreateNewTech;
    @FXML
    private Button btnEditTechDoc;
    @FXML
    private Button btnHome;
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
    @FXML
    private TextField tfCustomerStreetName;
    @FXML
    private TextField tfCustomerZipcode;
    private Customer customer;
    private User user;
    private Pattern emailPattern;


    /**
     * Setup is a method inherited from BaseController.
     * It is used for instantiating models, locking fields and buttons, adding listeners, checking users,
     * filling fields and adding listeners to textfields.
     */
    @Override
    public void setup() {
        try {
            lockFieldsAndButtons();
            addListener();
            checkUser();
            fillFields();
            checkEmailPattern(tfCustomerEmail);
            addNumericalListener(tfCustomerPhoneNumber);
            addAlphabeticListener(tfCustomerName);
            super.setCModel(new CustomerModel());
            super.setTModel(new TechDocModel());
            loadTechDocs();
            btnEditTechDoc.setDisable(true);
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * A setter for user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Loads the customer and the user of a tech document into the list view.
     */
    private void loadTechDocs() {
        try {
            lvTechDocs.setItems(super.getTModel().getTechDocs(customer, user));
        } catch (SQLException e) {
            displayError(e);
        }
    }

    /**
     * A button used to save a customer with the information that have been inputted.
     */
    @FXML
    private void handleSave(ActionEvent actionEvent) {
        if(!emailPattern.matcher(tfCustomerEmail.getText()).matches()) {
            String alertString = "Email is not typed correct";
            Alert alert = new Alert(Alert.AlertType.WARNING, alertString);
            alert.showAndWait();
            return;
        }
        try{
            btnEditCustomer.setDisable(false);
            lockFieldsAndButtons();

            String name = tfCustomerName.getText();
            String email = tfCustomerEmail.getText();
            String phoneNumber = tfCustomerPhoneNumber.getText();
            String streetName = tfCustomerStreetName.getText();
            String zipcode = tfCustomerZipcode.getText();

            customer.setName(name);
            customer.setEmail(email);
            customer.setTlf(phoneNumber);
            customer.setStreetName(streetName);
            customer.setZipcode(zipcode);

            super.getCModel().updateCustomer(customer);
        }catch (Exception e){
            displayError(e);
            e.printStackTrace();
        }
    }

    /**
     * A cancel button that enables the edit customer button, and disables the edit fields.
     */
    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        btnEditCustomer.setDisable(false);
        fillFields();
        lockFieldsAndButtons();
    }

    /**
     * A method used to disable all the text fields in the customer view controller,
     * as well as the cancel customer and create customer button.
     */
    private void lockFieldsAndButtons() {
        tfCustomerEmail.setDisable(true);
        tfCustomerPhoneNumber.setDisable(true);
        tfCustomerName.setDisable(true);
        tfCustomerStreetName.setDisable(true);
        tfCustomerZipcode.setDisable(true);
        btnCancelCustomer.setDisable(true);
        btnCreateCustomer.setDisable(true);
    }

    /**
     * A method used to fill the fields with customer information.
     */
    private void fillFields() {
        tfCustomerEmail.setText(customer.getEmail());
        tfCustomerName.setText(customer.getName());
        tfCustomerPhoneNumber.setText(String.valueOf(customer.getTlf()));
        tfCustomerStreetName.setText(customer.getStreetName());
        tfCustomerZipcode.setText(customer.getZipcode());
    }

    /**
     * A method used to disable the edit customer button, and enable all the text fields,
     * the cancel customer button and the create customer button.
     */
    @FXML
    private void handleEdit(ActionEvent actionEvent) {
        btnEditCustomer.setDisable(true);
        tfCustomerEmail.setDisable(false);
        tfCustomerPhoneNumber.setDisable(false);
        tfCustomerName.setDisable(false);
        tfCustomerStreetName.setDisable(false);
        tfCustomerZipcode.setDisable(false);
        btnCancelCustomer.setDisable(false);
        btnCreateCustomer.setDisable(false);
    }

    /**
     * A button used to load the MainView window.
     */
    @FXML
    private void handleHome(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/MainView.fxml"));
            Parent root = loader.load();

            MainViewController controller = loader.getController();
            controller.setCModel(new CustomerModel());
            controller.setUser(user);
            controller.setup();

            Stage currentStage = (Stage) btnHome.getScene().getWindow();
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
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load MainView.fxml");
            alert.showAndWait();
        }
    }

    /**
     * A setter for customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * A method used to open the TechDocEditor view.
     */
    private void openTechDocEditor(Button btn, TechDoc techDoc){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/TechDocEditor.fxml"));
            Parent root = loader.load();

            TechDocEditorController controller = loader.getController();
            if (techDoc != null) {
                controller.setIsEdit(techDoc);
            }
            controller.setCustomer(customer);
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
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load TechDocEditor.fxml");
            alert.showAndWait();
        }
    }

    /**
     * A button used to open the tech doc editor.
     */
    @FXML
    private void handleCreateNew(ActionEvent actionEvent) {
        openTechDocEditor(btnCreateNewTech, null);
    }

    /**
     * Gets the selected tech document from the list view
     * Opens the selected tech document in the techDocEditor
     */
    @FXML
    private void handleEditTechDoc(ActionEvent actionEvent) {
        TechDoc techDoc = lvTechDocs.getSelectionModel().getSelectedItem();
        openTechDocEditor(btnEditTechDoc, techDoc);
    }

    /**
     * Uses the techMenu method on the manageTechs button5
     */
    @FXML
    private void handleManageTechs(ActionEvent actionEvent) {
        techMenu();
    }

    /**
     * a button used to add a technician to a tech document in the tech menu.
     */
    @FXML
    private void handleAddTechMenu(ActionEvent actionEvent) {
        try {
            User selectedTech = lvTechs.getSelectionModel().getSelectedItem();
            TechDoc techDoc = lvTechDocs.getSelectionModel().getSelectedItem();
            super.getTModel().addTech(techDoc, selectedTech);
            fillTechs(techDoc);
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * Turns the tech menu pane invisible.
     */
    @FXML
    private void handleRemoveTechMenu(ActionEvent actionEvent) {
        try {
            User selectedTech = lvTechsWorking.getSelectionModel().getSelectedItem();
            TechDoc techDoc = lvTechDocs.getSelectionModel().getSelectedItem();
            super.getTModel().removeTech(techDoc, selectedTech);
            fillTechs(techDoc);
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * Closes the tech menu
     */
    @FXML
    private void handleCancelAddTechMenu(ActionEvent actionEvent) {
        techMenu();
    }

    /**
     * A button used to delete tech documents
     */
    @FXML
    private void handleDeleteTechDoc(ActionEvent actionEvent) throws SQLException {
        TechDoc techDoc = lvTechDocs.getSelectionModel().getSelectedItem();
        super.getTModel().deleteTechDoc(techDoc);
        lvTechDocs.getItems().remove(techDoc);
    }

    /**
     * Adds a listener to the selected item property of lvTechDocs listview.
     */
    private void addListener() {
        Platform.runLater(() -> {
            lvTechDocs.getSelectionModel().selectedItemProperty().addListener((((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    btnEditTechDoc.setDisable(false);
                } else {
                    btnEditTechDoc.setDisable(true);
                }
                try {
                    fillTechs(newValue);
                } catch (Exception e) {
                    displayError(e);
                }
            })));
        });
    }

    /**
     * If the tech menu is visible, it will slide out, else it will display the tech menu.
     */
    private void techMenu(){
        if (addTechMenu.isVisible()) {
            TranslateTransition slideOut = new TranslateTransition(Duration.seconds(0.5), addTechMenu);
            slideOut.setToX(-addTechMenu.getWidth());
            slideOut.setOnFinished(e -> addTechMenu.setVisible(false));
            slideOut.play();
        } else {
            addTechMenu.setTranslateX(-addTechMenu.getWidth());
            addTechMenu.setVisible(true);

            TranslateTransition slideIn = new TranslateTransition(Duration.seconds(0.5), addTechMenu);
            slideIn.setToX(0);
            slideIn.play();
        }
    }

    /**
     * Creates two lists, a list of technicians that have access to the tech document, and a list of poeple
     * that does not have access to the tech document.
     */
    private void fillTechs(TechDoc techDoc) throws Exception {

        ObservableList<User> linkedTechList = FXCollections.observableArrayList();
        ObservableList<User> allUserList = super.getUModel().getObservableUsers();
        ObservableList<User> techList = FXCollections.observableArrayList();
        ObservableList<User> techsWithAccessList = FXCollections.observableArrayList();

        Thread thread = new Thread(() -> {
            try {
                for (int i = 0; i < allUserList.size(); i++) {
                    if (allUserList.get(i).getUserType().getId() == 2) {
                        techList.add(allUserList.get(i));
                    }
                }

                if (lvTechDocs.getSelectionModel() != null) {
                    linkedTechList.addAll(super.getUModel().getLinkedUsers(techDoc));
                    for (int i = 0; i < linkedTechList.size(); i++) {
                        techList.remove(linkedTechList.get(i));
                        techsWithAccessList.add(linkedTechList.get(i));
                    }
                    lvTechs.setItems(techList);
                    lvTechsWorking.setItems(techsWithAccessList);
                }
            } catch (Exception e) {
                displayError(e);
            }
        });

        Platform.runLater(thread);
    }

    /**
     * Checks the user type, and disable functions according to who can access what.
     */
    private void checkUser() {
        if (user.getUserType().getId() == 2) {
            btnEditCustomer.setVisible(false);
            btnAddTech.setVisible(false);
            btnRemoveTech.setVisible(false);
            btnManageTech.setVisible(false);
            btnCancelCustomer.setVisible(false);
            btnCreateCustomer.setVisible(false);
        } else if (user.getUserType().getId() == 1) {
            btnEditCustomer.setVisible(false);
            btnCancelCustomer.setVisible(false);
            btnCreateCustomer.setVisible(false);
            techDocHighlighter();
        } else {
            btnAddTech.setVisible(false);
            btnCreateNewTech.setVisible(false);
            btnDeleteTechDoc.setVisible(false);
            techDocHighlighter();
        }
    }

    /**
     * Sets the color of the text to a bold red font, if the tech document is locked.
     * If it is unlocked, it removes the styling.
     */
    private void techDocHighlighter() {
        lvTechDocs.setCellFactory(param -> new ListCell<TechDoc>() {
            @Override
            protected void updateItem(TechDoc techDoc, boolean empty) {
                super.updateItem(techDoc, empty);
                if (empty || techDoc == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(techDoc.toString());
                    if (techDoc.isLocked()) {
                        setStyle("-fx-font-weight: bold;-fx-text-fill: red; -fx-font-size: 13");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
    }

    /**
     * States that an email pattern containts the given letters, numbers and signs.
     */
    private void checkEmailPattern(TextField textField){
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

    /**
     * Adds a listener to the text property, ensuring only letters are allowed
     */
    private void addAlphabeticListener(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("[a-æøåA-ÆØÅ]*")){
                textField.setText(newValue.replaceAll("[^a-æøåA-ÆØÅ]",""));
            }
        });
    }
}
