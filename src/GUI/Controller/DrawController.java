package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class DrawController extends BaseController implements Initializable {
    @FXML
    private Button btnLine;
    @FXML
    private Canvas canvas;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private TextField tfBrushSize;
    @FXML
    private Button btnBrush;
    private GraphicsContext brushTool;
    private boolean brushSelected;
    private boolean lineSelected;
    private boolean circleSelected;
    private boolean eraserSelected;
    private double startX;
    private double startY;
    private double endX;
    private double endY;

    @Override
    public void setup() throws Exception {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        brushTool = canvas.getGraphicsContext2D();
    }

    private void unselectAll() {
        brushSelected = false;
        lineSelected = false;
        circleSelected = false;
        eraserSelected = false;
    }

    @FXML
    private void handleSelectBrush(ActionEvent actionEvent) {
        unselectAll();
        brushSelected = true;
    }

    @FXML
    private void handleSelectLine(ActionEvent actionEvent) {
        unselectAll();
        lineSelected = true;
    }

    @FXML
    private void handleSelectCircle(ActionEvent actionEvent) {
        unselectAll();
        circleSelected = true;

    }

    @FXML
    private void handleSelectEraser(ActionEvent actionEvent) {
        unselectAll();
        eraserSelected = true;
    }

    @FXML
    private void mousePressed(MouseEvent e) {
        if (lineSelected) {
            linePressed(e);
        }
        if (circleSelected) {
            circlePressed(e);
        }
    }

    @FXML
    private void mouseDragged(MouseEvent e) {
            mouseDrag(e);

        if (brushSelected) {
            brushDragged(e);
        }
        if (eraserSelected) {
            eraserDragged(e);
        }
    }

    @FXML
    private void mouseReleased(MouseEvent e) {
        if (lineSelected) {
            lineReleased(e);
        }
        if (circleSelected) {
            circleReleased(e);
        }
    }

    private void eraserDragged(MouseEvent e) {
        double size = Double.parseDouble(tfBrushSize.getText());
        double x = e.getX() - size/2;
        double y = e.getY() - size/2;

        brushTool.setFill(colorPicker.getValue());
        brushTool.clearRect(x, y, size, size);
    }

    private void circlePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
        brushTool.setStroke(colorPicker.getValue());
    }

    private void circleReleased(MouseEvent e) {
        endX = e.getX();
        endY = e.getY();
        brushTool.fillOval(startX, startY, endX, endY);
    }

    private void linePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
        brushTool.setLineWidth(Double.parseDouble(tfBrushSize.getText()));
        brushTool.setStroke(colorPicker.getValue());
    }

    private void mouseDrag(MouseEvent e) {
        endX = e.getX();
        endY = e.getY();
    }

    private void lineReleased(MouseEvent e) {
        endX = e.getX();
        endY = e.getY();
        brushTool.strokeLine(startX, startY, endX, endY);
    }

    private void brushDragged(MouseEvent e) {
        double size = Double.parseDouble(tfBrushSize.getText());
        double x = e.getX() - size/2;
        double y = e.getY() - size/2;

        brushTool.setFill(colorPicker.getValue());
        brushTool.fillRoundRect(x, y, size, size, size, size);
    }
}
