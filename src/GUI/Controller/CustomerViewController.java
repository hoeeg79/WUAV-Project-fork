package GUI.Controller;

import BE.Customer;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CustomerViewController extends BaseController{
    @FXML
    private TextField tfPictureFilepath;
    @FXML
    private Button btnPictureFinder;
    @FXML
    private ComboBox cbCustomerTypes;
    @FXML
    private Button btnEditCustomer;
    @FXML
    private Button btnCancelCustomer;
    @FXML
    private Button btnCreateCustomer;
    @FXML
    private TextField tfCustomerName;
    @FXML
    private TextField tfCustomerEmail;
    @FXML
    private TextField tfCustomerPhoneNumber;
    private Customer customer;

    @Override

    public void setup() throws Exception {
        lockFieldsAndButtons();
        cbCustomerTypes.setItems(FXCollections.observableArrayList("Business", "Government", "Private"));
        fillFields();
    }

    private void lockFieldsAndButtons() {
        tfCustomerEmail.setDisable(true);
        tfCustomerPhoneNumber.setDisable(true);
        tfCustomerName.setDisable(true);
        tfPictureFilepath.setDisable(true);
        btnCancelCustomer.setDisable(true);
        btnCreateCustomer.setDisable(true);
        btnPictureFinder.setDisable(true);
        cbCustomerTypes.setDisable(true);
    }

    private void fillFields() {
        tfCustomerEmail.setText(customer.getEmail());
        tfCustomerName.setText(customer.getName());
        tfCustomerPhoneNumber.setText(String.valueOf(customer.getTlf()));
        tfPictureFilepath.setText(customer.getPicture());
        cbCustomerTypes.getSelectionModel().select(customer.getCustomerType() - 1);
    }

    @FXML
    private void handleSave(ActionEvent actionEvent) {
        btnEditCustomer.setDisable(false);
        lockFieldsAndButtons();

        int customerType = cbCustomerTypes.getSelectionModel().getSelectedIndex() + 1;
    }

    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        btnEditCustomer.setDisable(false);
        fillFields();
        lockFieldsAndButtons();
    }

    @FXML
    private void handleEdit(ActionEvent actionEvent) {
        btnEditCustomer.setDisable(true);
        tfCustomerEmail.setDisable(false);
        tfCustomerPhoneNumber.setDisable(false);
        tfCustomerName.setDisable(false);
        tfPictureFilepath.setDisable(false);
        btnCancelCustomer.setDisable(false);
        btnCreateCustomer.setDisable(false);
        btnPictureFinder.setDisable(false);
        cbCustomerTypes.setDisable(false);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @FXML
    private void handlePictureFinder(ActionEvent actionEvent) {

    }
}
