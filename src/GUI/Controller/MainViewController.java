package GUI.Controller;

<<<<<<< Updated upstream
import javafx.event.ActionEvent;
=======
import GUI.Model.CustomerModel;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
>>>>>>> Stashed changes
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
<<<<<<< Updated upstream

public class MainViewController {
    public Button createCustomers;
    public TextField searchBar;

    public void handleCreateCustomers(ActionEvent actionEvent) {
=======
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    @FXML
    private ListView lvPriv;
    @FXML
    private ListView lvCorp;
    @FXML
    private ListView lvGov;
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
>>>>>>> Stashed changes
    }

    private void loadLists(CustomerModel model) throws Exception {
        this.model = model;
        lvPriv.setItems(model.getPrivateCustomer());
        lvCorp.setItems(model.getBusinessCustomer());
        lvGov.setItems(model.getGovernmentCustomer());
    }



}
