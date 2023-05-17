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


    @Override
    public void setup() throws Exception {
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
    }

    public void setUser(User user) {
        this.user = user;
    }

    private void loadTechDocs() throws SQLException {
        lvTechDocs.setItems(super.getTModel().getTechDocs(customer, user));
    }

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

    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        btnEditCustomer.setDisable(false);
        fillFields();
        lockFieldsAndButtons();
    }

    private void lockFieldsAndButtons() {
        tfCustomerEmail.setDisable(true);
        tfCustomerPhoneNumber.setDisable(true);
        tfCustomerName.setDisable(true);
        tfCustomerStreetName.setDisable(true);
        tfCustomerZipcode.setDisable(true);
        btnCancelCustomer.setDisable(true);
        btnCreateCustomer.setDisable(true);
    }

    private void fillFields() {
        tfCustomerEmail.setText(customer.getEmail());
        tfCustomerName.setText(customer.getName());
        tfCustomerPhoneNumber.setText(String.valueOf(customer.getTlf()));
        tfCustomerStreetName.setText(customer.getStreetName());
        tfCustomerZipcode.setText(customer.getZipcode());
    }

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
            currentStage.setScene(new Scene(root));
            currentStage.centerOnScreen();
            currentStage.show();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load MainView.fxml");
            alert.showAndWait();
        }
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

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
            double currentWidth = currentStage.getWidth();
            double currentHeight = currentStage.getHeight();

            currentStage.setScene(new Scene(root));
            currentStage.setWidth(currentWidth);
            currentStage.setHeight(currentHeight);

            currentStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load TechDocEditor.fxml");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCreateNew(ActionEvent actionEvent) {
        openTechDocEditor(btnCreateNewTech, null);
    }

    @FXML
    private void handleEditTechDoc(ActionEvent actionEvent) {
        TechDoc techDoc = lvTechDocs.getSelectionModel().getSelectedItem();
        openTechDocEditor(btnEditTechDoc, techDoc);
    }

    @FXML
    private void handleManageTechs(ActionEvent actionEvent) {
        techMenu();
    }

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

    @FXML
    private void handleCancelAddTechMenu(ActionEvent actionEvent) {
        techMenu();
    }

    @FXML
    private void handleDeleteTechDoc(ActionEvent actionEvent) throws SQLException {
        TechDoc techDoc = lvTechDocs.getSelectionModel().getSelectedItem();
        super.getTModel().deleteTechDoc(techDoc);
        lvTechDocs.getItems().remove(techDoc);
    }

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

    private void checkEmailPattern(TextField textField){
        emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", Pattern.CASE_INSENSITIVE);
    }

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

    private void addAlphabeticListener(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("[a-æøåA-ÆØÅ]*")){
                textField.setText(newValue.replaceAll("[^a-æøåA-ÆØÅ]",""));
            }
        });
    }
}
