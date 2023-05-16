package GUI.Controller;

import BE.TechDoc;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.FileChooser;

import javax.swing.text.StyleConstants;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExportPDFController extends BaseController {

    @FXML
    private CheckBox cbDescription;
    @FXML
    private CheckBox cbDrawing;
    @FXML
    private CheckBox cbPhotos;
    @FXML
    private CheckBox cbDevices;
    @FXML
    private CheckBox cbExtra;
    @FXML
    private Button btnExport;
    @FXML
    private Button btnCancel;
    private TechDoc techDoc;

    @Override
    public void setup() throws Exception {

    }

    public void handleExport(ActionEvent actionEvent) {
        generatePdf();
    }

    public void handleCancel(ActionEvent actionEvent) {
        closeWindow(btnCancel);
    }

    private void generatePdf() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            fileChooser.setInitialFileName(techDoc.getSetupName());

            File fileToSave = fileChooser.showSaveDialog(btnExport.getScene().getWindow());
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(fileToSave));
            Document document = new Document(pdfDoc, PageSize.A4);

            PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
            PdfFont bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
            PdfFont italic = PdfFontFactory.createFont(StandardFonts.TIMES_ITALIC);

            com.itextpdf.layout.element.Image logo = new com.itextpdf.layout.element.Image(ImageDataFactory.create("resources/Photatoes/logo.png"));
            float scaleFactor = 0.20F;
            logo.scaleAbsolute(logo.getImageWidth() * scaleFactor, logo.getImageHeight() * scaleFactor);
            logo.setHorizontalAlignment(HorizontalAlignment.LEFT);

            document.add(logo);

            Paragraph title = new Paragraph(techDoc.getSetupName());
            title.setFont(bold);
            title.setFontSize(18);
            title.setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = currentTime.format(formatter);
            Paragraph date = new Paragraph(formattedDate);
            date.setFont(font);
            date.setFontSize(15);
            date.setTextAlignment(TextAlignment.RIGHT);
            document.add(date);

            if (cbDescription.isSelected()) {
                Paragraph description = new Paragraph(techDoc.getSetupDescription());
                description.setFont(font);
                description.setFontSize(15);
                document.add(description);
            }



            document.close();

        } catch (Exception e) {
            displayError(e);
        }
    }

    public void setTechDoc(TechDoc techDoc) {
        this.techDoc = techDoc;
    }
}
