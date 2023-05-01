package GUI.Controller;

import BE.User;
import BE.UserType;
import BLL.BCrypt;
import GUI.Model.UsersModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class CreateUsersController extends BaseController{
    @FXML
    public TextField txtNameUser;
    @FXML
    public TextField txtUsernameUser;
    @FXML
    public TextField txtConfirmPwUser;
    @FXML
    public TextField txtPasswordUser;
    @FXML
    public Button btnSaveUser;
    @FXML
    public Button deleteUser;
    @FXML
    public Button cancel;
    @FXML
    public TableView<User> userList;
    @FXML
    public RadioButton techChecker;
    @FXML
    public RadioButton salesChecker;
    @FXML
    public RadioButton managerChecker;
    @FXML
    public TableColumn userscln;
    @FXML
    public Button btnEditUsers;
    @FXML
    public Label passwordLabel;
    @FXML
    public Label confirmPasswordLabel;
    private boolean txtInUsernameField;
    private boolean txtInNameField;
    private boolean txtInPasswordField;
    private boolean txtInConfirmField;
    private boolean checkerSalesField;
    private boolean checkerManagerField;
    private boolean checkerTechField;
    private boolean isEdit;


    @Override
    public void setup() throws Exception {
        super.setUModel(new UsersModel());
        insertIntoTable();
        btnSaveUser.setDisable(true);
        beGoneButton();
    }

    private void beGoneButton(){
        btnEditUsers.setDisable(true);
        userList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEditUsers.setDisable(false);
            } else {
                btnEditUsers.setDisable(true);
            }
        });

        deleteUser.setDisable(true);
        userList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                deleteUser.setDisable(false);
            } else {
                deleteUser.setDisable(true);
            }
        });

        txtUsernameUser.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtInUsernameField = true;
            } else {
                txtInUsernameField = false;
            }
            enableTheButtons();
        });

        txtNameUser.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtInNameField = true;
            } else {
                txtInNameField = false;
            }
            enableTheButtons();
        });

        txtPasswordUser.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtInPasswordField = true;
            } else {
                txtInPasswordField = false;
            }
            enableTheButtons();
        });

        txtConfirmPwUser.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtInConfirmField = true;
            } else {
                txtInConfirmField = false;
            }
            enableTheButtons();
        });

        techChecker.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                checkerTechField = true;
                managerChecker.setSelected(false);
                salesChecker.setSelected(false);
            } else {
                checkerTechField = false;
            }
            enableTheButtons();
        });
        managerChecker.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                checkerManagerField = true;
                techChecker.setSelected(false);
                salesChecker.setSelected(false);
            } else {
                checkerManagerField = false;
            }
            enableTheButtons();
        });
        salesChecker.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                checkerSalesField = true;
                managerChecker.setSelected(false);
                techChecker.setSelected(false);
            } else {
                checkerSalesField = false;
            }
            enableTheButtons();
        });
    }

    private void enableTheButtons() {
        if ((checkerSalesField || checkerManagerField || checkerTechField) && txtInNameField && txtInConfirmField && txtInPasswordField && txtInUsernameField) {
            btnSaveUser.setDisable(false);
        } else {
            btnSaveUser.setDisable(true);
        }
    }

    public void handleSaveUser(ActionEvent actionEvent) throws SQLException {
        if (isEdit) {
            editUser();
            isEdit = false;
            clearItAll();
            txtUsernameUser.setDisable(false);
        } else if (checkThatHoe()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Stupid");
            alert.setContentText("The username you are trying to create already exists");
            alert.showAndWait();
        } else {
            newUser();
            clearItAll();
        }
    }

    private boolean checkThatHoe(){
        for (User user : userList.getItems()) {
            if (txtUsernameUser.getText().equals(user.getUsername())){
                return true;
            }
        }
        return false;
    }

    private void clearItAll() {
        txtConfirmPwUser.clear();
        txtPasswordUser.clear();
        txtNameUser.clear();
        txtUsernameUser.clear();
        txtPasswordUser.clear();
        txtConfirmPwUser.clear();
        salesChecker.setSelected(false);
        managerChecker.setSelected(false);
        techChecker.setSelected(false);
    }

    private void editUser() throws SQLException {
        User user = userList.getSelectionModel().getSelectedItem();
        user.setName(txtNameUser.getText());

        if (!txtConfirmPwUser.getText().isEmpty()) {
            if (txtConfirmPwUser.equals(txtPasswordUser)){
                String salt = BCrypt.gensalt(10);
                String hashedPassword1 = BCrypt.hashpw(txtPasswordUser.getText(), salt);
                user.setPassword(hashedPassword1);
            }
        }

        super.getUModel().updateUser(user);
    }

    private void newUser(){
        String username = txtUsernameUser.getText();
        String name = txtNameUser.getText();
        String password = txtPasswordUser.getText();
        String confirmPassword = txtConfirmPwUser.getText();

        int userType = -1;
        if(techChecker.isSelected()) {
            userType = 2;
        } else if (managerChecker.isSelected()) {
            userType = 1;
        } else if (salesChecker.isSelected()) {
            userType = 3;
        }

        String salt = BCrypt.gensalt(10);
        String hashedPassword1 = BCrypt.hashpw(password, salt);
        String hashedPassword2 = BCrypt.hashpw(confirmPassword, salt);

        try {
            if (hashedPassword1.equals(hashedPassword2)) {
                User u = new User(username, hashedPassword1, name,userType);
                super.getUModel().createUser(u);
            }
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }


    public void handleDeleteUser(ActionEvent actionEvent) {
        try {
            User user = userList.getSelectionModel().getSelectedItem();
            super.getUModel().deleteUser(user);
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void handleCancelWindow(ActionEvent actionEvent) {
        closeWindow(cancel);
    }

    private void insertIntoTable(){
        userscln.setCellValueFactory(new PropertyValueFactory<>("name"));
        userList.getColumns().addAll();
        userList.setItems(super.getUModel().getObservableUsers());
    }

    public void handleEditUsers(ActionEvent actionEvent) {
        isEdit = true;
        User selectedUser = userList.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            txtNameUser.setText(selectedUser.getName());
            txtUsernameUser.setText(selectedUser.getUsername());
            txtUsernameUser.setDisable(true);

            passwordLabel.setText("New Password");
            confirmPasswordLabel.setText("Confirm New Password");

            if (selectedUser.getUserType() == 3) {
                techChecker.setSelected(false);
                managerChecker.setSelected(false);
                salesChecker.setSelected(true);
            } else if (selectedUser.getUserType() == 1) {
                techChecker.setSelected(false);
                managerChecker.setSelected(true);
                salesChecker.setSelected(false);
            } else {
                techChecker.setSelected(true);
                managerChecker.setSelected(false);
                salesChecker.setSelected(false);
            }

        } else {
            txtNameUser.clear();
            txtPasswordUser.clear();
            txtConfirmPwUser.clear();
            passwordLabel.setText("Password");
            confirmPasswordLabel.setText("Confirm Password");
        }

    }
}
