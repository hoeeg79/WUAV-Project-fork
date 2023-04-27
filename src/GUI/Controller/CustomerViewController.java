package GUI.Controller;

import BE.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CustomerViewController extends BaseController{
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
    private TextField tfCustomerPhonenumber;
    private Customer customer;

    @Override
    public void setup() throws Exception {
        lockFieldsAndButtons();
        fillFields();
    }

    private void lockFieldsAndButtons() {
        tfCustomerEmail.setDisable(true);
        tfCustomerPhonenumber.setDisable(true);
        tfCustomerName.setDisable(true);
        btnCancelCustomer.setDisable(true);
        btnCreateCustomer.setDisable(true);
    }

    private void fillFields() {
        tfCustomerEmail.setText(customer.getEmail());
        tfCustomerName.setText(customer.getName());
        tfCustomerPhonenumber.setText(String.valueOf(customer.getTlf()));
    }

    @FXML
    private void handleSave(ActionEvent actionEvent) {
        btnEditCustomer.setDisable(false);
        lockFieldsAndButtons();
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
        tfCustomerPhonenumber.setDisable(false);
        tfCustomerName.setDisable(false);
        btnCancelCustomer.setDisable(false);
        btnCreateCustomer.setDisable(false);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
