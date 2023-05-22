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

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
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
                disableButtons();
                
            } else {
                fillDevice();
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
     * Disables the pdf button if the document is approved.
     */
    private void enablePdfBtn(){
        btnPDF.setDisable(!techDoc.isLocked() && !techDoc.isApproved());
    }

    /**
     * Disables buttons that shouldn't be used before a document have been saved once.
     */
    private void disableButtons() {
        btnDevice.setDisable(true);
        btnDeleteDevice.setDisable(true);
        btnAddPicture.setDisable(true);
        btnDeletePicture.setDisable(true);
        btnNextPicture.setDisable(true);
        btnDraw.setDisable(true);
        btnReadyForApproval.setDisable(true);
    }

    /**
     * Enables buttons that can be used after a document have been saved once.
     */
    private void enableButtons() {
        btnDevice.setDisable(false);
        btnDeleteDevice.setDisable(false);
        btnAddPicture.setDisable(false);
        btnDeletePicture.setDisable(false);
        btnNextPicture.setDisable(false);
        btnDraw.setDisable(false);
        btnReadyForApproval.setDisable(false);
    }

    /**
     * Changes the view back to the customer view.
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
     * Saves the document and connects the user to the document if it's new.
     */
    @FXML
    private void handleSave(ActionEvent actionEvent) {
        if (isEdit) {
            doEditOfDoc();
        } else {
            isEdit = true;
            doEditOfDoc();
            addTech(techDoc, user);
            enableButtons();
            fillDevice();
        }
        lblSaveStatus.setText("Saved successfully!");
        clearSavedLabelText();
    }

    /**
     * Generates a new temporary document.
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
     * Updates the document with the values from the text-fields.
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
     * Sets the document to edit mode.
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
     * Initializes the image-list.
     */
    private void initializeList() {
        imageList = FXCollections.observableArrayList();
    }

    /**
     * Fills out the fields with the values of the document.
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
     * Checks if there are any picture to the document and then gets them.
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
     * Links a tech to the document.
     */
    private void addTech(TechDoc techDoc, User user) {
        try {
            super.getTModel().addTech(techDoc, user);
        } catch (SQLException e) {
            e.printStackTrace();
            displayError(e);
        }
    }

    /**
     * Clears the save status label after a delay.
     */
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
     * Opens a new window with the draw view fxml.
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
     * Displays the technical drawing if it has one.
     */
    private void displayDrawing() {
        try {
            if (techDoc.getFilePathDiagram() != null) {
                File file = new File(techDoc.getFilePathDiagram());
                Image drawing = new Image(String.valueOf(file.toURI()));
                techDrawing.setImage(drawing);
            }
        } catch (Exception ignored) {
            techDrawing.setImage(null);
        }
    }

    /**
     * Opens a new window with the picture description fxml to add pictures to the document.
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
     * Displays the first image in the image-list if there is any.
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
     * Changes the image to the next one in the image-list.
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
     * Deletes the current image shown with a confirmation box.
     */
    @FXML
    private void handleDeletePicture(ActionEvent actionEvent) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deletion Confirmation");
            alert.setContentText("Are you sure you want to delete this picture?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
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
            }

        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * Opens a new window with the create device view fxml to create new devices to the documents.
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
     * Fills out the device table view, with the specified columns.
     */
    private void fillDevice() {
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
     * Clears and populates the device-list.
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
     * Deletes the currently selected device with a confirmation box.
     */
    @FXML
    private void handleDeleteDevice(ActionEvent actionEvent) {
        try {
            Device deleteDevice = tvDevice.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deletion Confirmation");
            alert.setContentText("Are you sure you want to delete " + deleteDevice.getDevice().toUpperCase());
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK){
                super.getTModel().deleteDevice(deleteDevice);
                refreshDevice();
            }

        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * Updates the approval status and locks the tech document.
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
     * Disables the text-fields and buttons.
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
     * Opens a new window with the export pdf fxml to export the document as a pdf.
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
     * Enables the text-fields and buttons.
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
     * Sets the tooltip to the finalize button.
     */
    private void setupTooltipApproval() {
        Tooltip tooltip = new Tooltip("Locks the document for approval");
        Tooltip.install(btnReadyForApproval, tooltip);
    }

    /**
     * Sets the tooltip for the draw button.
     */
    private void setupTooltipDraw() {
        Tooltip tooltip = new Tooltip("Opens the drawing application");
        Tooltip.install(btnDraw, tooltip);
    }
}


