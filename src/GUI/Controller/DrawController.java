package GUI.Controller;

import BE.TechDoc;
import GUI.Model.TechDocModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DrawController extends BaseController implements Initializable {
    @FXML
    private Button btnEraser;
    @FXML
    private Button btnIcon;
    @FXML
    private ComboBox cbIcons;
    @FXML
    private Canvas tempCanvas;
    @FXML
    private Button btnLine;
    @FXML
    private Canvas canvas;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private ComboBox cbBrushSize;
    @FXML
    private Button btnBrush;
    private GraphicsContext brushTool;
    private GraphicsContext tempBrushTool;
    private boolean brushSelected;
    private boolean lineSelected;
    private boolean imageSelected;
    private boolean eraserSelected;
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private Image image;
    private List<File> imageFiles;
    private ObservableList icons;
    private TechDoc techDoc;
    private TechDocModel techDocModel;

    @Override
    public void setup() {
        try {
            techDocModel = new TechDocModel();
            System.out.println(techDoc);
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void setTechDoc(TechDoc techDoc) {
        this.techDoc = techDoc;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        brushTool = canvas.getGraphicsContext2D();
        tempBrushTool = tempCanvas.getGraphicsContext2D();
        brushSelected = true;
        disableButton(btnBrush);
        addIcons();
        cbIcons.valueProperty().addListener(observable -> {
            setIcon();
            handleSelectIcon();
        });
        addBrushSizes();
        cbBrushSize.getSelectionModel().select(0);
    }

    public void editDrawing() {
        if (techDoc.getFilePathDiagram() != null) {
            Image image = new Image(techDoc.getFilePathDiagram());
            brushTool.drawImage(image, 0, 0);
        }
    }

    private void setIcon() {
        int iconIndex = cbIcons.getSelectionModel().getSelectedIndex();
        File file = imageFiles.get(iconIndex);
        image = new Image(file.toURI().toString());
    }

    private void addIcons() {
        imageFiles = getImageFiles("resources/Icons");
        List<String> iconNames = new ArrayList<>();

        if (imageFiles != null) {
            for (File file : imageFiles) {
                iconNames.add(file.getName());
            }
        }

        icons = FXCollections.observableArrayList(iconNames);
        cbIcons.setItems(icons);
    }

    private List<File> getImageFiles(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        List<File> imageFiles = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && isImageFile(file)) {
                    imageFiles.add(file);
                }
            }
        }
        return imageFiles;
    }

    private boolean isImageFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg");
    }

    private void unselectAll() {
        brushSelected = false;
        lineSelected = false;
        imageSelected = false;
        eraserSelected = false;
    }

    @FXML
    private void handleSelectBrush() {
        unselectAll();
        brushSelected = true;
        enableButtons();
        disableButton(btnBrush);
    }

    @FXML
    private void handleSelectLine() {
        unselectAll();
        lineSelected = true;
        enableButtons();
        disableButton(btnLine);
    }

    @FXML
    private void handleSelectEraser() {
        unselectAll();
        eraserSelected = true;
        enableButtons();
        disableButton(btnEraser);
    }

    public void handleSelectIcon() {
        unselectAll();
        imageSelected = true;
        enableButtons();
        disableButton(btnIcon);
    }

    private void addBrushSizes() {
        Integer[] ints = {10, 12, 14, 16, 18, 20, 25, 30};
        ObservableList<Integer> brushSizes =  FXCollections.observableArrayList(ints);
        cbBrushSize.setItems(brushSizes);
    }

    public void handleSave(ActionEvent actionEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Png", "*.png"));
            fileChooser.setInitialFileName(techDoc.getSetupName() + " Technical-drawing");
            WritableImage imageToSave = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
            canvas.snapshot(null, imageToSave);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageToSave, null);
            File file = fileChooser.showSaveDialog(btnBrush.getScene().getWindow());
            techDoc.setFilePathDiagram(file.getAbsolutePath());
            ImageIO.write(bufferedImage, "png", file);
            techDocModel.updateDrawing(file.getAbsolutePath(), techDoc);
            closeWindow(btnBrush);
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void handleCancel(ActionEvent actionEvent) {
        closeWindow(btnBrush);
    }

    @FXML
    private void mousePressed(MouseEvent e) {
        if (lineSelected) {
            linePressed(e);
        }
        if (imageSelected) {
            imagePressed(e);
        }
    }

    @FXML
    private void mouseDragged(MouseEvent e) {
        if (lineSelected) {
            lineDrag(e);
        }
        if (brushSelected) {
            brushDragged(e);
        }
        if (eraserSelected) {
            eraserDragged(e);
        }
        if (imageSelected) {
            imageDrag(e);
        }
    }

    @FXML
    private void mouseReleased(MouseEvent e) {
        if (lineSelected) {
            lineReleased(e);
        }
        if (imageSelected) {
            imageReleased(e);
        }
    }

    private void eraserDragged(MouseEvent e) {
        double size = Double.parseDouble(cbBrushSize.getSelectionModel().getSelectedItem().toString());
        double x = e.getX() - size/2;
        double y = e.getY() - size/2;

        brushTool.setFill(colorPicker.getValue());
        brushTool.clearRect(x, y, size, size);
    }

    private void linePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
        brushTool.setLineWidth(Double.parseDouble(cbBrushSize.getSelectionModel().getSelectedItem().toString()));
        tempBrushTool.setLineWidth(Double.parseDouble(cbBrushSize.getSelectionModel().getSelectedItem().toString()));
        brushTool.setStroke(colorPicker.getValue());
        tempBrushTool.setStroke(colorPicker.getValue());
    }

    private void lineDrag(MouseEvent e) {
        endX = e.getX();
        endY = e.getY();
        tempBrushTool.clearRect(0, 0, tempCanvas.getWidth(), tempCanvas.getHeight());
        tempBrushTool.strokeLine(startX, startY, endX, endY);

    }

    private void lineReleased(MouseEvent e) {
        endX = e.getX();
        endY = e.getY();
        brushTool.strokeLine(startX, startY, endX, endY);
        tempBrushTool.clearRect(0, 0, tempCanvas.getWidth(), tempCanvas.getHeight());
    }

    private void brushDragged(MouseEvent e) {
        double size = Double.parseDouble(cbBrushSize.getSelectionModel().getSelectedItem().toString());
        double x = e.getX() - size/2;
        double y = e.getY() - size/2;

        brushTool.setFill(colorPicker.getValue());
        brushTool.fillRoundRect(x, y, size, size, size, size);
    }

    private void imagePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
    }

    private void imageDrag(MouseEvent e) {
        endX = e.getX();
        endY = e.getY();

        double width = endX-startX;
        double height = endY-startY;
        double scaleFactor = Math.min(width / image.getWidth(), height / image.getHeight());
        double scaledWidth = image.getWidth() * scaleFactor;
        double scaledHeight = image.getHeight() * scaleFactor;
        double x = startX + (width - scaledWidth) / 2;
        double y = startY + (height - scaledHeight) / 2;

        tempBrushTool.clearRect(0, 0, tempCanvas.getWidth(), tempCanvas.getHeight());
        tempBrushTool.drawImage(image, x, y, scaledWidth, scaledHeight);
    }

    private void imageReleased(MouseEvent e) {
        endX = e.getX();
        endY = e.getY();

        double width = endX-startX;
        double height = endY-startY;
        double scaleFactor = Math.min(width / image.getWidth(), height / image.getHeight());
        double scaledWidth = image.getWidth() * scaleFactor;
        double scaledHeight = image.getHeight() * scaleFactor;
        double x = startX + (width - scaledWidth) / 2;
        double y = startY + (height - scaledHeight) / 2;

        brushTool.drawImage(image, x, y, scaledWidth, scaledHeight);
        tempBrushTool.clearRect(0, 0, tempCanvas.getWidth(), tempCanvas.getHeight());
    }

    private void disableButton(Button b) {
        b.setDisable(true);
    }

    private void enableButtons() {
        btnEraser.setDisable(false);
        btnIcon.setDisable(false);
        btnBrush.setDisable(false);
        btnLine.setDisable(false);
    }
}
