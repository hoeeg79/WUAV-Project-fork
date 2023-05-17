package GUI.Controller;

import BE.Customer;
import BE.Device;
import BE.Pictures;
import BE.TechDoc;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ExportPDFController extends BaseController {

    @FXML
    private ImageView imgPdf;
    @FXML
    private CheckBox cbForCustomer;
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
    private Customer customer;
    private ArrayList<Device> deviceList;

    @Override
    public void setup() throws Exception {
        disableBoxes();
    }

    /**
     * Disables choice boxes if they fulfill the conditions.
     */
    private void disableBoxes() {
        if (techDoc.getFilePathDiagram() == null || techDoc.getFilePathDiagram().isEmpty()) {
            cbDrawing.setDisable(true);
        }

        if (techDoc.getPictures().isEmpty()) {
            cbPhotos.setDisable(true);
        }

        if (deviceList.isEmpty()) {
            cbDevices.setDisable(true);
        }

        if (techDoc.getSetupDescription().isEmpty()) {
            cbDescription.setSelected(false);
            cbDescription.setDisable(true);
        }

        if (techDoc.getExtraInfo() == null || techDoc.getExtraInfo().isEmpty()) {
            cbExtra.setDisable(true);
        }
    }

    @FXML
    private void handleExport(ActionEvent actionEvent) {
        generatePdf();
    }

    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        closeWindow(btnCancel);
    }

    /**
     * Generates a Pdf file depending on what checkboxes have been selected.
     */
    private void generatePdf() {
        try {
            //Opens file chooser to select a destination for the document.
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            fileChooser.setInitialFileName(techDoc.getSetupName());

            //Creates the document.
            File fileToSave = fileChooser.showSaveDialog(btnExport.getScene().getWindow());
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(fileToSave));
            Document document = new Document(pdfDoc, PageSize.A4);

            //Prepping fonts to be used in the document.
            PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
            PdfFont bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
            PdfFont italic = PdfFontFactory.createFont(StandardFonts.TIMES_ITALIC);
            PdfFont bolditalic = PdfFontFactory.createFont(StandardFonts.TIMES_BOLDITALIC);

            //Adds WUAV logo to the document.
            Image logo = new Image(ImageDataFactory.create("resources/Photatoes/logo.png"));
            float logoScaleFactor = 0.20F;
            logo.scaleAbsolute(logo.getImageWidth() * logoScaleFactor, logo.getImageHeight() * logoScaleFactor);
            logo.setHorizontalAlignment(HorizontalAlignment.LEFT);

            document.add(logo);

            //Adds the title of the tech-doc to the document
            Paragraph title = new Paragraph(techDoc.getSetupName());
            title.setFont(bold);
            title.setFontSize(18);
            title.setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            //Adds the current date to the document.
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = currentTime.format(formatter);
            Paragraph date = new Paragraph(formattedDate);
            date.setFont(font);
            date.setFontSize(15);
            date.setTextAlignment(TextAlignment.RIGHT);
            document.add(date);

            //Adds the customers name and address to the document.
            Paragraph customerInfo = new Paragraph();
            customerInfo.add(customer.getName() + "\n");
            customerInfo.add(customer.getStreetName() + "\n");
            customerInfo.add(customer.getZipcode() + ", " +customer.getCity());
            customerInfo.setTextAlignment(TextAlignment.LEFT);
            customerInfo.setFont(font);
            customerInfo.setFontSize(15);
            document.add(customerInfo);

            //Adds the description to the document if selected.
            if (cbDescription.isSelected()) {
                Paragraph description = new Paragraph();
                description.add("Description:" + "\n");
                description.add(techDoc.getSetupDescription());
                description.setFont(font);
                description.setFontSize(14);
                document.add(description);
            }

            //Adds the devices and formats them to the document if selected.
            if (cbDevices.isSelected()) {
                Paragraph devices = new Paragraph();
                devices.add("Device credentials:" + "\n");
                devices.add(formatDevices());
                devices.setFont(font);
                devices.setFontSize(14);
                document.add(devices);
            }

            //Adds the technical drawing if selected.
            if (cbDrawing.isSelected() && !techDoc.getFilePathDiagram().isEmpty()) {
                Paragraph text = new Paragraph("Technical drawing");
                text.setFontSize(14);
                text.setFont(font);
                Image drawing = new Image(ImageDataFactory.create(techDoc.getFilePathDiagram()));
                drawing.scaleAbsolute(200, 150);
                drawing.setHorizontalAlignment(HorizontalAlignment.CENTER);
                document.add(drawing);
            }

            //Adds the photos to a table to the document if selected.
            if (cbPhotos.isSelected() && techDoc.getPictures() != null) {
                ArrayList<Pictures> pictures = (ArrayList<Pictures>) techDoc.getPictures();
                Table pictureContainer = new Table(pictures.size());
                Image picture;
                pictureContainer.setBorder(Border.NO_BORDER);
                for (Pictures p: pictures) {
                    picture = new Image(ImageDataFactory.create(p.getFilePath()));
                    picture.scaleAbsolute(200, 200);
                    pictureContainer.addCell(createImageCell(picture));
                }
                pictureContainer.setHorizontalAlignment(HorizontalAlignment.CENTER);
                document.add(pictureContainer);
            }

            //Adds the extra info to the document if selected.
            if (cbExtra.isSelected()) {
                Paragraph extraInfo = new Paragraph();
                Paragraph extraTitle = new Paragraph();

                extraTitle.add("Additional Info:");
                extraTitle.setFont(bolditalic);
                extraTitle.setFontSize(15);
                document.add(extraTitle);

                extraInfo.add(techDoc.getExtraInfo());
                extraInfo.setFont(italic);
                extraInfo.setFontSize(14);
                document.add(extraInfo);
            }

            //Adds a sign line to the document if selected.
            if (cbForCustomer.isSelected()) {
                PdfCanvas canvas = new PdfCanvas(pdfDoc.getPage(pdfDoc.getNumberOfPages()));
                Rectangle pageSize = pdfDoc.getPage(pdfDoc.getNumberOfPages()).getPageSize();
                float x = pageSize.getLeft() + document.getLeftMargin();
                float y = pageSize.getBottom() + document.getBottomMargin();
                float width = pageSize.getWidth() - document.getLeftMargin() - document.getRightMargin();
                canvas.setStrokeColorRgb(0, 0, 0).setLineWidth(1).moveTo(x, y).lineTo(x + width, y).stroke();
                Paragraph signHere = new Paragraph("Sign Here:").setFixedPosition(pageSize.getLeft() + document.getLeftMargin(), y +5, 100);
                document.add(signHere);
            }

            //Finishes the document.
            document.close();
            previewPdf(fileToSave);

        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * Previews the pdf provided to the user.
     * @param pdf Pdf to be previewed.
     */
    private void previewPdf(File pdf) {
        try {
            PDDocument document = PDDocument.load(pdf);
            PDFRenderer renderer = new PDFRenderer(document);

            BufferedImage image = renderer.renderImageWithDPI(0, 72);
            javafx.scene.image.Image pdfImage = SwingFXUtils.toFXImage(image, null);
            imgPdf.setImage(pdfImage);

        } catch (IOException e) {
            displayError(e);
        }
    }

    /**
     * Generates a cell that contains an image without a border.
     * @param image Image to be shown.
     * @return Returns generated cell.
     */
    private static Cell createImageCell(Image image) {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.add(image.setAutoScale(true));
        return cell;
    }

    /**
     * Formats a device to a comprehensive string.
     * @return Formatted device string.
     */
    private String formatDevices() {
        StringBuilder result = new StringBuilder();

        for (Device d: deviceList) {
            result.append("Name: ").append(d.getDevice())
                    .append(", username: ").append(d.getUsername())
                    .append(", password: ").append(d.getPassword())
                    .append("\n");
        }


        return result.toString();
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setDeviceList(ArrayList<Device> deviceList) {
        this.deviceList = deviceList;
    }

    public void setTechDoc(TechDoc techDoc) {
        this.techDoc = techDoc;
    }
}
