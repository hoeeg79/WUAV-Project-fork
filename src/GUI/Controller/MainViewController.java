package GUI.Controller;

import BE.User;
import GUI.Model.UsersModel;
import javafx.event.ActionEvent;

import BE.Customer;
import GUI.Model.CustomerModel;
import javafx.animation.TranslateTransition;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.WHITE;
import static javafx.scene.text.TextAlignment.CENTER;


public class MainViewController extends BaseController implements Initializable {
    @FXML
    private Button btnCreateUsers;
    @FXML
    private Button btnLogOut;
    @FXML
    private Button btnOpenCustomer;
    @FXML
    private Button btnDeleteCustomer;
    @FXML
    private ListView<Customer> lvPriv;
    @FXML
    private ListView<Customer> lvCorp;
    @FXML
    private ListView<Customer> lvGov;

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
    private TextField tfSearchBar;
    @FXML
    private Button btnCustomerImage;
    @FXML
    private Button btnCancelCustomer;
    @FXML
    private Button btnCreateCustomersMenu;
    @FXML
    private Button btnCreateCustomer;
    @FXML
    private Pane createCustomerMenu;
    private Customer selectedCustomer;
    private User user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void setup() throws Exception {
        try {
            loadLists(super.getCModel());
            searchBar();
            clearCustomerMenu();
            cbCustomerTypes.setItems(FXCollections.observableArrayList("Business", "Government", "Private"));
            changeSelectedCustomer();
            checkUserType();
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void setUser(User user){
        this.user = user;
    }

    @FXML
    private void handleCreateCustomersMenu(ActionEvent actionEvent) {
        customerMenu();
    }

    @FXML
    private void handleCreateCustomer(ActionEvent actionEvent) throws SQLException {
        String name = tfCustomerName.getText();
        String email = tfCustomerEmail.getText();
        String tlf = tfCustomerPhonenumber.getText();
        String image = tfCustomerImage.getText();
        int customerType = cbCustomerTypes.getSelectionModel().getSelectedIndex() + 1;

        Customer customer = new Customer(name, email, tlf, image, customerType);

        super.getCModel().createCustomer(customer);
    }

    @FXML
    private void handleCancelCustomer(ActionEvent actionEvent) {
        customerMenu();
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
        controller.setup();

        stage.setScene(new Scene(root));
        stage.setTitle("Create User");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        stage.show();
    }

    private void loadLists(CustomerModel model) throws Exception {
        lvPriv.setItems(model.getPrivateCustomer());
        prepList(lvPriv);
        lvCorp.setItems(model.getBusinessCustomer());
        prepList(lvCorp);
        lvGov.setItems(model.getGovernmentCustomer());
        prepList(lvGov);
    }

    private void searchBar() throws Exception{
        tfSearchBar.textProperty().addListener(((observable, oldValue, newValue) -> {
            try{
                super.getCModel().customerSearch(newValue);
            } catch (Exception e){
                e.printStackTrace();
                displayError(e);
            }
        }));
    }
    private void prepList(ListView listView) {
        listView.setCellFactory(new Callback<ListView<Customer>, ListCell<Customer>>() {
            @Override
            public ListCell<Customer> call(ListView<Customer> listView) {
                return new ListCell<Customer>() {
                    @Override
                    protected void updateItem(Customer customer, boolean empty) {
                        super.updateItem(customer, empty);
                        ImageView imageView = new ImageView();
                        Text text = new Text();
                        text.setTextAlignment(CENTER);
                        text.setTranslateY(50);
                        StackPane stackPane = new StackPane(imageView, text);
                        setGraphic(stackPane);
                        if (customer == null || empty) {
                            text.setText(null);
                            setGraphic(null);
                        } else if (!customer.getPicture().isEmpty()) {
                            File imageFile = new File(customer.getPicture());
                            Image image = new Image(imageFile.toURI().toString());
                            imageView.setImage(image);
                            text.setText(customer.getName());
                            text.toFront();
                        } else {
                            Image image = new Image("defaultUserResize-noBG.png");
                            imageView.setImage(image);
                            text.setText(customer.getName());
                            text.toFront();
                        }
                    }
                };
            }
        });
    }

    @FXML
    private void handlePickImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(btnCustomerImage.getScene().getWindow());
        tfCustomerImage.setText(selectedFile.getAbsolutePath());
    }

    private void changeSelectedCustomer(){
        lvCorp.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) ->{
            if (newValue != null) {
                lvGov.getSelectionModel().clearSelection();
                lvPriv.getSelectionModel().clearSelection();
                selectedCustomer = newValue;
            }
        }));
        lvGov.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                lvCorp.getSelectionModel().clearSelection();
                lvPriv.getSelectionModel().clearSelection();
                selectedCustomer = newValue;
            }
        }));
        lvPriv.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                lvGov.getSelectionModel().clearSelection();
                lvCorp.getSelectionModel().clearSelection();
                selectedCustomer = newValue;
            }
        }));
    }

    private void setSceneSelectCompany(Button btn, Customer customer){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/CustomerView.fxml"));
            Parent root = loader.load();

            CustomerViewController controller = loader.getController();
            controller.setCustomer(customer);
            controller.setUModel(new UsersModel());
            controller.setUser(user);
            controller.setup();

            Stage currentStage = (Stage) btn.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load CustomerView.fxml");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteCustomer(ActionEvent actionEvent) throws SQLException {
        super.getCModel().deleteCustomer(selectedCustomer);
    }

    @FXML
    private void handleOpenCustomer(ActionEvent actionEvent) {
        setSceneSelectCompany(btnOpenCustomer, selectedCustomer);
    }

    @FXML
    private void handleLogOut(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) btnLogOut.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("GUI/View/LoginView.fxml"));

            stage.setScene(new Scene(root));
            stage.setTitle("WUAV Documentation System");
            stage.show();
        } catch (Exception e) {
            displayError(e);
        }
    }

    private void checkUserType() {
        if (user.getUserType().getId() == 2) {
            btnCreateCustomersMenu.setVisible(false);
            btnDeleteCustomer.setVisible(false);
            btnCreateUsers.setVisible(false);
        } else if (user.getUserType().getId() == 1) {
            btnCreateCustomersMenu.setVisible(false);
            btnDeleteCustomer.setVisible(false);
        } else {
            btnCreateUsers.setVisible(false);
        }
    }
}
