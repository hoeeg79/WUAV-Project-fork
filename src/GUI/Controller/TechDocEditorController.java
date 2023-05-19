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
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TechDocEditorController extends BaseController {

    @FXML
    private Button btnReadyForApproval;
    @FXML
    private Button btnPDF;
    @FXML
    private TableView<Device> tvDevice;
    @FXML
    private TableColumn tcDevice;
    @FXML
    private TableColumn tcUsername;
    @FXML
    private TableColumn tcPassword;
    @FXML
    private Button btnDeleteDevice;
    @FXML
    private Button btnDraw;
    @FXML
    private Button btnSave;
    @FXML
    private Label lblPictureDescription;
    @FXML
    private ImageView techDrawing;
    @FXML
    private Label lblNoPictures;
    @FXML
    private Button btnDeletePicture;
    @FXML
    private Button btnAddPicture;
    @FXML
    private Button btnDevice;
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
    private final ArrayList<Device> deviceList = new ArrayList<>();

    @Override
    public void setup() {
        try {
            super.setTModel(new TechDocModel());
            lblNoPictures.setVisible(true);
            setupTooltipDraw();
            if (!isEdit) {
                initializeList();
                generateTechDoc();
            } else {
                fillDevice(super.getTModel());
            }
            if (techDoc.getPictures() != null) {
                lblNoPictures.setVisible(false);
            }
            enablePdfBtn();
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * A button that closes the techDocEditor view, and opens CustomerView instead.
     */
    @FXML
    private void handleClose(ActionEvent actionEvent) {
        try {
            if (!isEdit) {
                super.getTModel().deleteTechDoc(techDoc);
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
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * A button used to save a tech document.
     */
    @FXML
    private void handleSave(ActionEvent actionEvent) {
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

    /**
     * A method that generates a new tech document.
     */
    private void generateTechDoc() {
        try {
            TechDoc newDoc = new TechDoc("not saved yet", customer.getId());
            techDoc = getTModel().createTechDoc(newDoc);
        } catch (SQLException e) {
            displayError(e);
        }
    }

    /**
     * A method that gets the information on different text fields and updates a tech document.
     */
    private void doEditOfDoc() {
        try {
            techDoc.setSetupName(tfTitle.getText());
            techDoc.setSetupDescription(taSetupDescription.getText());
            techDoc.setExtraInfo(taExtraInfo.getText());
            getTModel().updateTechDoc(techDoc);
        } catch (SQLException e) {
            displayError(e);
        }
    }

    /**
     *
     * @param techDoc
     */
    public void setIsEdit(TechDoc techDoc) {
        try {
            this.techDoc = techDoc;
            isEdit = true;
            initializeList();
            fillFields();
            if (techDoc.isApproved()) {
                btnReadyForApproval.setText("Unlock");
                lockFields();
            } else if (techDoc.isLocked()) {
                lockFields();
                btnReadyForApproval.setText("Approved");
            } else {
                setupTooltipApproval();
            }
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * A method that initializes imageList.
     */
    private void initializeList() {
        imageList = FXCollections.observableArrayList();
    }

    /**
     * A method used to fill out fields.
     */
    private void fillFields() {
        taSetupDescription.setText(techDoc.getSetupDescription());
        tfTitle.setText(techDoc.getSetupName());
        taExtraInfo.setText(techDoc.getExtraInfo());
        getPicturesFromTechDoc();
        displayDrawing();
        displayCurrentImage();
    }

    /**
     * A method that gets the picture of a specified tech document.
     */
    private Boolean getPicturesFromTechDoc() {
        if (techDoc.getPictures() != null) {
            imageList.clear();
            imageList.addAll(techDoc.getPictures());
            if (currentImageIndex == -1) {
                currentImageIndex = 0;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * A setter for pictures.
     */
    public void setPicture(Pictures picture) {
        this.picture = picture;
    }

    /**
     * A setter for customers.
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * A setter for users.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * A method used to add a tech document to a user.
     */
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

    /**
     * A button used to open the DrawView.
     */
    @FXML
    private void handleDraw(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/DrawView.fxml"));
            Parent root = loader.load();

            DrawController controller = loader.getController();
            controller.setup();
            controller.setTechDoc(techDoc);
            controller.editDrawing();

            stage.setScene(new Scene(root));
            stage.setTitle("Technical Drawing");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.showAndWait();

            super.getTModel().getTechDoc(techDoc);
            displayDrawing();
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }

    /**
     * A method used to display a drawing if one exists.
     */
    private void displayDrawing() {
        try {
            if (techDoc.getFilePathDiagram() != null) {
                Image drawing = new Image(techDoc.getFilePathDiagram());
                techDrawing.setImage(drawing);
            }
        } catch (Exception ignored) {
            techDrawing.setImage(null);
        }
    }

    /**
     * A button used to open the PictureDescription view.
     */
    @FXML
    private void handleAddPicture(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/PictureDescription.fxml"));
            Parent root = loader.load();

            PictureDescriptionController controller = loader.getController();
            controller.setup();
            controller.setTechDoc(techDoc);
            stage.setScene(new Scene(root));
            stage.setTitle("Add Description");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.showAndWait();

            techDoc = super.getTModel().getTechDoc(techDoc);
            if (getPicturesFromTechDoc()) {
                lblNoPictures.setVisible(false);
                if (currentImageIndex == -1) {
                    currentImageIndex = 0;
                } else {
                    currentImageIndex = imageList.size() - 1;
                }
                displayCurrentImage();
            }
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * A method used to display the current image.
     */
    private void displayCurrentImage() {
        if (currentImageIndex >= 0 && currentImageIndex < imageList.size()) {
            Image currentImage = new Image(imageList.get(currentImageIndex).getFilePath());
            imageViewTechDoc.setImage(currentImage);
            imageViewTechDoc.setFitWidth(400);
            imageViewTechDoc.setFitHeight(400);
            lblPictureDescription.setText(imageList.get(currentImageIndex).getDescription());
        }
    }

    /**
     * A method used to go to the next picture in the imageList.
     */
    @FXML
    private void handleNextPicture(ActionEvent actionEvent) {
        if (imageList.size() > 1) {
            currentImageIndex++;
            if (currentImageIndex >= imageList.size()) {
                currentImageIndex = 0;
                displayCurrentImage();
            }
            displayCurrentImage();
        }
    }

    /**
     * A button used to delete a picture from the imageList.
     */
    @FXML
    private void handleDeletePicture(ActionEvent actionEvent) {
        try {
            if (currentImageIndex >= 0 && currentImageIndex < imageList.size()) {
                imageList.remove(currentImageIndex);
                super.getTModel().deletePictures(techDoc.getPictures().get(currentImageIndex));
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
        } catch (SQLException e) {
            displayError(e);
        }
    }

    /**
     * A button used to open the Create Device view.
     */
    @FXML
    private void handleOpenDevice(ActionEvent actionEvent) {
        try {
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
            stage.setResizable(false);
            stage.showAndWait();
            refreshDevice();
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * A method used to fill out the device table view, with the specified columns.
     */
    private void fillDevice(TechDocModel model) {
        try {
            tcDevice.setCellValueFactory(new PropertyValueFactory<>("device"));
            tcUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
            tcPassword.setCellValueFactory(new PropertyValueFactory<>("password"));

            tvDevice.getItems().clear();
            tvDevice.setItems(super.getTModel().getObservableDevices(techDoc));
            deviceList.addAll(super.getTModel().getObservableDevices(techDoc));
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * A method used to refresh the device list.
     */
    private void refreshDevice() {
        try {
            tvDevice.getItems().clear();
            tvDevice.setItems(super.getTModel().getObservableDevices(techDoc));
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * A button used to delete a specified device from a specific tech document.
     */
    @FXML
    private void handleDeleteDevice(ActionEvent actionEvent) {
        try {
            Device deleteDevice = tvDevice.getSelectionModel().getSelectedItem();
            super.getTModel().deleteDevice(deleteDevice);
            refreshDevice();
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * A method that sets btnPDF to disabled if a tech document is approved.
     */
    private void enablePdfBtn(){
        if (techDoc.isLocked() || techDoc.isApproved()){
            btnPDF.setDisable(false);
        } else {
            btnPDF.setDisable(true);
        }
    }

    /**
     * A method used to update the approval status and lock the tech document.
     */
    @FXML
    private void handleReadyForApproval(ActionEvent actionEvent) {
        try {
            if (techDoc.isApproved()) {
                techDoc.setApproved(false);
                btnReadyForApproval.setText("Finalize");
                unlockFields();
                super.getTModel().updateTechDoc(techDoc);
                enablePdfBtn();
                return;
            } else if (techDoc.isLocked()) {
                techDoc.setApproved(true);
                techDoc.setLocked(false);
                techDoc.setApproved(true);
            } else {
                techDoc.setLocked(true);
            }

            super.getTModel().updateTechDoc(techDoc);
            handleClose(actionEvent);
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * A method used to lock fields.
     */
    private void lockFields() {
        tfTitle.setDisable(true);
        taExtraInfo.setDisable(true);
        taSetupDescription.setDisable(true);
        btnAddPicture.setDisable(true);
        btnDeletePicture.setDisable(true);
        btnSave.setDisable(true);
        btnDraw.setDisable(true);
        btnDevice.setDisable(true);
        btnDeleteDevice.setDisable(true);
    }

    /**
     * A method used to open the ExportPDF view.
     */
    @FXML
    private void handleExportPDF(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/ExportPDF.fxml"));
            Parent root = loader.load();

            ExportPDFController controller = loader.getController();
            controller.setTechDoc(techDoc);
            controller.setDeviceList(deviceList);
            controller.setCustomer(customer);
            controller.setup();

            stage.setScene(new Scene(root));
            stage.setTitle("Export PDF");
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
     * A method used to unlock fields.
     */
    private void unlockFields() {
        tfTitle.setDisable(false);
        taExtraInfo.setDisable(false);
        taSetupDescription.setDisable(false);
        btnAddPicture.setDisable(false);
        btnDeletePicture.setDisable(false);
        btnSave.setDisable(false);
        btnDraw.setDisable(false);
        btnDevice.setDisable(false);
        btnDeleteDevice.setDisable(false);
    }

    /**
     * A method used to put a tooltip over the finalize button.
     */
    private void setupTooltipApproval() {
        Tooltip tooltip = new Tooltip("Locks the document for approval");
        Tooltip.install(btnReadyForApproval, tooltip);
    }

    /**
     * A method used to put a tooltip over the draw button.
     */
    private void setupTooltipDraw() {
        Tooltip tooltip = new Tooltip("Opens the drawing application");
        Tooltip.install(btnDraw, tooltip);
    }
}


