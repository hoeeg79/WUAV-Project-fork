package GUI.Controller;

import GUI.Model.CustomerModel;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController extends BaseController implements Initializable {
    @FXML
    private ComboBox cbCustomerTypes;
    @FXML
    private TextField tfCustomerName;
    @FXML
    private TextField tfCustomerEmail;
    @FXML
    private TextField tfCustomerPhonenumber;
    @FXML
    private TextField tfCustomerImage;
    @FXML
    private Button btnCustomerImage;
    @FXML
    private Button cancelCustomer;
    @FXML
    private Button createCustomersMenu;
    @FXML
    private Button createCustomer;
    @FXML
    private TextField searchBar;
    @FXML
    private Pane createCustomerMenu;
    private CustomerModel CModel;
    @Override
    public void setup() throws Exception {
        CModel = super.getCModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbCustomerTypes.setItems(FXCollections.observableArrayList("Technician", "Project Manager", "Salesperson"));
    }

    public void handleCreateCustomersMenu(ActionEvent actionEvent) {
        customerMenu();
    }

    public void handleCreateCustomer(ActionEvent actionEvent) {
        String name = tfCustomerName.getText();
        String Email = tfCustomerEmail.getText();
        String tlf = tfCustomerPhonenumber.getText();
        String image = tfCustomerImage.getText();

        //CModel.createCustomer(customer);
    }

    public void handleCancelCustomer(ActionEvent actionEvent) {
        customerMenu();
        clearCustomerMenu();
    }

    private void clearCustomerMenu(){
        tfCustomerName.clear();
        tfCustomerEmail.clear();
        tfCustomerPhonenumber.clear();
        tfCustomerImage.clear();
    }

    private void customerMenu() {
        if (createCustomerMenu.isVisible()) {
            TranslateTransition slideOut = new TranslateTransition(Duration.seconds(0.5), createCustomerMenu);
            slideOut.setToX(-createCustomerMenu.getWidth());
            slideOut.setOnFinished(e -> createCustomerMenu.setVisible(false));
            slideOut.play();
        } else {
            createCustomerMenu.setTranslateX(-createCustomerMenu.getWidth());
            createCustomerMenu.toFront();
            createCustomerMenu.setVisible(true);

            TranslateTransition slideIn = new TranslateTransition(Duration.seconds(0.5), createCustomerMenu);
            slideIn.setToX(0);
            slideIn.play();
        }
    }

    @FXML
    public void handleCreateUsers(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/CreateUsersView.fxml"));
        Parent root = loader.load();

        //Someday we will put a model here

        stage.setScene(new Scene(root));
        stage.setTitle("Create User");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        stage.show();
    }


}
