package GUI.Controller;

import javafx.event.ActionEvent;

import BE.Customer;
import GUI.Model.CustomerModel;
import javafx.animation.TranslateTransition;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
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


    @Override
    public void setup() throws Exception {
        try {
            loadLists(super.getCModel());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        clearCustomerMenu();
        cbCustomerTypes.setItems(FXCollections.observableArrayList("Business", "Government", "Private"));
    }

    public void handleCreateCustomersMenu(ActionEvent actionEvent) {
        customerMenu();
    }


    public void handleCreateCustomer(ActionEvent actionEvent) throws SQLException {
        String name = tfCustomerName.getText();
        String email = tfCustomerEmail.getText();
        int tlf = Integer.parseInt(tfCustomerPhonenumber.getText());
        String image = tfCustomerImage.getText();
        int customerType = cbCustomerTypes.getSelectionModel().getSelectedIndex() + 1;

        Customer customer = new Customer(name, email, tlf, image, customerType);

        super.getCModel().createCustomer(customer);
    }

    public void handleCancelCustomer(ActionEvent actionEvent) {
        customerMenu();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
        lvPriv.setItems(model.getPrivateCustomer());
        prepList(lvPriv);
        lvCorp.setItems(model.getBusinessCustomer());
        prepList(lvCorp);
        lvGov.setItems(model.getGovernmentCustomer());
        prepList(lvGov);
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
                        getChildren().add(text);
                        if (customer == null || empty) {
                            setText(null);
                            setGraphic(null);
                            setBackground(null);
                        } else if (customer != null && (customer.getPicture() != "" || customer.getPicture() != null)) {
                            File imageFile = new File(customer.getPicture());
                            Image image = new Image(imageFile.toURI().toString());
                            imageView.setImage(image);
                            setGraphic(imageView);
                            text.setText(customer.getName());
                            text.toFront();
                        } else {
                            Image image = new Image("defaultUserResize.png");
                            imageView.setImage(image);
                            setGraphic(imageView);
                            text.setText(customer.getName());
                            text.toFront();
//                            setBackground(new Background(new BackgroundImage(
//                                    new Image("defaultUserResize.png"),
//                                    BackgroundRepeat.NO_REPEAT,
//                                    BackgroundRepeat.NO_REPEAT,
//                                    BackgroundPosition.CENTER,
//                                    BackgroundSize.DEFAULT
//                            )));
                        }
                    }
                };
            }
        });
    }



    public void handlePickImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(btnCustomerImage.getScene().getWindow());
        tfCustomerImage.setText(selectedFile.getAbsolutePath());
    }
}
