package GUI.Controller;


import javafx.event.ActionEvent;

import GUI.Model.CustomerModel;
import javafx.animation.TranslateTransition;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainViewController extends BaseController implements Initializable {
    @FXML
    private ListView lvPriv;
    @FXML
    private ListView lvCorp;
    @FXML
    private ListView lvGov;

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

    CustomerModel model = new CustomerModel();

    private CustomerModel CModel;
    @Override
    public void setup() throws Exception {
        CModel = super.getCModel();
    }

    public void handleCreateCustomersMenu(ActionEvent actionEvent) {
        customerMenu();
    }

    public void handleCreateCustomer(ActionEvent actionEvent) {

    }

    public void handleCancelCustomer(ActionEvent actionEvent) {
        customerMenu();
    }

    public MainViewController() throws Exception {
        this.model = model;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadLists(model);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        clearCustomerMenu();
        cbCustomerTypes.setItems(FXCollections.observableArrayList("Technician", "Project Manager", "Salesperson"));
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
    public void handleCreateUsers(ActionEvent actionEvent) throws Exception {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/CreateUsersView.fxml"));
        Parent root = loader.load();

        CreateUsersController controller = loader.getController();
        controller.setUModel(super.getUModel());
        controller.setup();

        stage.setScene(new Scene(root));
        stage.setTitle("Create User");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        stage.show();
    }

    private void loadLists(CustomerModel model) throws Exception {
        this.model = model;
        lvPriv.setItems(model.getPrivateCustomer());
        lvCorp.setItems(model.getBusinessCustomer());
        lvGov.setItems(model.getGovernmentCustomer());
    }

    public void handleCreateCustomers(ActionEvent actionEvent) {
        String name = tfCustomerName.getText();
        String Email = tfCustomerEmail.getText();
        String tlf = tfCustomerPhonenumber.getText();
        String image = tfCustomerImage.getText();

        //CModel.createCustomer(customer);
    }
}

