package GUI.Controller;

import BE.*;
import GUI.Model.TechDocModel;
import GUI.Model.UsersModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class TechDocEditorController extends BaseController {

    @FXML
    private TableView<Device> tvDevice;
    @FXML
    private TableColumn tcDevice;
    @FXML
    private TableColumn tcUsername;
    @FXML
    private TableColumn tcPassword;
    @FXML
    private Label lblNoPictures;
    @FXML
    private Button btnDeletePicture;
    @FXML
    private Button btnAddPicture;
    @FXML
    private Button btnDevice;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnNextPicture;
    @FXML
    private ImageView imageViewTechDoc;
    @FXML
    private TextArea taExtraInfo;
    @FXML
    private Label lblSaveStatus;
    @FXML
    private TextArea taSetupDescription;
    @FXML
    private TextField tfTitle;
    @FXML
    private Button btnClose;
    private TechDoc techDoc;
    private Customer customer;
    private Pictures picture;
    private User user;
    private boolean isEdit;
    private ObservableList<Pictures> imageList;
    private int currentImageIndex = -1;

    @Override
    public void setup() throws Exception {
        super.setTModel(new TechDocModel());
        lblNoPictures.setVisible(true);
        if (!isEdit) {
            initializeList();
            generateTechDoc();
        } else{
            fillDevice(super.getTModel());
        }
    }

    @FXML
    private void handleClose(ActionEvent actionEvent) throws Exception {
        if (!isEdit) {
            //super.getTModel().deleteTechDoc(techDoc); //IMPLEMENT THIS!
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/CustomerView.fxml"));
        Parent root = loader.load();

        CustomerViewController controller = loader.getController();
        controller.setCustomer(customer);
        controller.setUModel(new UsersModel());
        controller.setUser(user);
        controller.setup();

        Stage currentStage = (Stage) btnClose.getScene().getWindow();
        currentStage.setScene(new Scene(root));
        currentStage.centerOnScreen();
        currentStage.show();
    }

    @FXML
    private void handleSave(ActionEvent actionEvent) throws SQLException {
        if (isEdit) {
            doEditOfDoc();
        } else {
            isEdit = true;
            doEditOfDoc();
            addTech(techDoc, user);
        }
        lblSaveStatus.setText("Saved successfully!");
        clearSavedLabelText();
    }

    private void generateTechDoc() throws SQLException {
        TechDoc newDoc = new TechDoc("not saved yet", customer.getId());
        techDoc = getTModel().createTechDoc(newDoc);
    }

    private void doEditOfDoc() throws SQLException {
        techDoc.setSetupName(tfTitle.getText());
        techDoc.setSetupDescription(taSetupDescription.getText());
        techDoc.setExtraInfo(taExtraInfo.getText());
        getTModel().updateTechDoc(techDoc);
    }

    public void setIsEdit(TechDoc techDoc) throws Exception {
        this.techDoc = techDoc;
        System.out.println(this.techDoc);
        isEdit = true;
        initializeList();
        fillFields();

    }

    private void initializeList() {
        imageList = FXCollections.observableArrayList();
    }

    private void fillFields() {
        taSetupDescription.setText(techDoc.getSetupDescription());
        tfTitle.setText(techDoc.getSetupName());
        taExtraInfo.setText(techDoc.getExtraInfo());
        if (techDoc.getPictures() != null) {
            imageList.addAll(techDoc.getPictures());
            currentImageIndex = 0;
        }
        displayCurrentImage();
    }

    public void setPicture(Pictures picture) {
        this.picture = picture;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private void addTech(TechDoc techDoc, User user) {
        try {
            super.getTModel().addTech(techDoc, user);
        } catch (SQLException e) {
            e.printStackTrace();
            displayError(e);
        }
    }

    private void clearSavedLabelText() {
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> lblSaveStatus.setText(""));
            }
        };
        timer.schedule(task, 5000);
    }

    public void handleAddPicture(ActionEvent actionEvent) throws SQLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Picture");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Files", "*.png", "*.jpg", "*.jpeg"));
        Stage stage = (Stage) btnAddPicture.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            picture = new Pictures(selectedFile.toURI().toString());
            imageList.add(super.getTModel().addTechPictures(picture, techDoc));
            Image image = new Image(picture.getFilePath());
            if (currentImageIndex == -1) {
                currentImageIndex = 0;
                displayCurrentImage();
            }
            imageViewTechDoc.setImage(image);
            imageViewTechDoc.setFitWidth(400);
            imageViewTechDoc.setFitHeight(400);

            lblNoPictures.setVisible(false);
        }
    }

    private void displayCurrentImage() {
        if (currentImageIndex >= 0 && currentImageIndex < imageList.size()) {
            Image currentImage = new Image(imageList.get(currentImageIndex).getFilePath());
            imageViewTechDoc.setImage(currentImage);
            imageViewTechDoc.setFitWidth(400);
            imageViewTechDoc.setFitHeight(400);
        }
    }

    public void handleNextPicture(ActionEvent actionEvent) {
        if (imageList.size() > 1) {
            currentImageIndex++;
            if (currentImageIndex >= imageList.size()) {
                currentImageIndex = 0;
            }
            displayCurrentImage();
        }
    }

    public void handleDeletePicture(ActionEvent actionEvent) {
        if (currentImageIndex >= 0 && currentImageIndex < imageList.size()) {
            imageList.remove(currentImageIndex);
            if (imageList.isEmpty()) {
                currentImageIndex = -1;
                imageViewTechDoc.setImage(null);
                lblNoPictures.setVisible(true);
            } else {
                if (currentImageIndex >= imageList.size()) {
                    currentImageIndex = 0;
                }
                displayCurrentImage();
            }
        }
    }

    public void handleOpenDevice(ActionEvent actionEvent) throws Exception {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/CreateDeviceView.fxml"));
        Parent root = loader.load();

        CreateDeviceController controller = loader.getController();
        controller.setup();
        controller.setTechDoc(techDoc);

        stage.setScene(new Scene(root));
        stage.setTitle("Create Device");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        stage.centerOnScreen();
        stage.showAndWait();
        refreshDevice();
    }

    private void fillDevice(TechDocModel model) throws Exception {
        tcDevice.setCellValueFactory(new PropertyValueFactory<>("device"));
        tcUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tcPassword.setCellValueFactory(new PropertyValueFactory<>("password"));

        tvDevice.getItems().clear();
        tvDevice.setItems(super.getTModel().getObservableDevices(techDoc));
    }

    private void refreshDevice() throws Exception{
        tvDevice.getItems().clear();
        tvDevice.setItems(super.getTModel().getObservableDevices(techDoc));
    }
}
