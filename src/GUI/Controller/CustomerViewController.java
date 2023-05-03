package GUI.Controller;

import BE.Customer;
import BE.TechDoc;
import BE.User;
import GUI.Model.CustomerModel;
import GUI.Model.TechDocModel;
import GUI.Model.UsersModel;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerViewController extends BaseController{
    @FXML
    private Pane addTechMenu;
    @FXML
    private ListView<User> lvTechs;
    @FXML
    private Button btnAddTech;
    @FXML
    private Button btnCreateNewTech;
    @FXML
    private Button btnEditTechDoc;
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
    private User user;

    @Override
    public void setup() throws Exception {
        lockFieldsAndButtons();
        cbCustomerTypes.setItems(FXCollections.observableArrayList("Business", "Government", "Private"));
        checkUser();
        fillFields();
        fillTechs();
        super.setCModel(new CustomerModel());
        super.setTModel(new TechDocModel());
        lvTechDocs.setItems(super.getTModel().getTechDocs(customer, user));
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    private void handleSave(ActionEvent actionEvent) {
        try{
            btnEditCustomer.setDisable(false);
            lockFieldsAndButtons();

            String name = tfCustomerName.getText();
            String email = tfCustomerEmail.getText();
            String phoneNumber = tfCustomerPhoneNumber.getText();
            String pictureFP = tfPictureFilepath.getText();
            int customerType = cbCustomerTypes.getSelectionModel().getSelectedIndex() + 1;

            customer.setName(name);
            customer.setEmail(email);
            customer.setTlf(phoneNumber);
            customer.setPicture(pictureFP);
            customer.setCustomerType(customerType);

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
            controller.setUser(user);
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
            currentStage.setScene(new Scene(root));
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

    private void techMenu(){
        if (addTechMenu.isVisible()) {
            TranslateTransition slideOut = new TranslateTransition(Duration.seconds(0.5), addTechMenu);
            slideOut.setToX(btnAddTech.getScene().getWidth());
            slideOut.setOnFinished(e -> addTechMenu.setVisible(false));
            slideOut.play();
        } else {
            addTechMenu.setTranslateX(btnAddTech.getScene().getWidth());
            addTechMenu.toFront();
            addTechMenu.setVisible(true);

            TranslateTransition slideIn = new TranslateTransition(Duration.seconds(0.5), addTechMenu);
            slideIn.setToX(btnAddTech.getScene().getWidth() - addTechMenu.getWidth());
            slideIn.play();
        }
    }

    @FXML
    private void handleAddTech(ActionEvent actionEvent) {
        techMenu();
    }

    private void fillTechs(){
        ObservableList<User> allUserList = super.getUModel().getObservableUsers();
        ObservableList<User> techList = FXCollections.observableArrayList();
        for (int i = 0; i < allUserList.size()-1; i++) {
            if (allUserList.get(i).getUserType().getId() == 2){
                techList.add(allUserList.get(i));
            }
        }
        lvTechs.setItems(techList);
    }

    @FXML
    private void handleAddTechMenu(ActionEvent actionEvent) {
    }

    @FXML
    private void handleCancelAddTechMenu(ActionEvent actionEvent) {
        techMenu();
    }

    private void checkUser() {
        if (user.getUserType().getId() == 2) {
            btnEditCustomer.setVisible(false);
            btnAddTech.setVisible(false);
            btnCancelCustomer.setVisible(false);
            btnCreateCustomer.setVisible(false);
        } else if (user.getUserType().getId() == 1) {
            btnEditCustomer.setVisible(false);
            btnCancelCustomer.setVisible(false);
            btnCreateCustomer.setVisible(false);
        } else {
            btnAddTech.setVisible(false);
            btnCreateNewTech.setVisible(false);
            btnEditTechDoc.setVisible(false);
        }
    }
}
