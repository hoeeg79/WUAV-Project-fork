package GUI.Controller;

import BE.User;
import BLL.BCrypt;
import GUI.Model.UsersModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class CreateUsersController extends BaseController{
    @FXML
    private Button btnCancel;
    @FXML
    private TableColumn typecln;
    @FXML
    private ToggleGroup userType;
    @FXML
    private TextField txtNameUser;
    @FXML
    private TextField txtUsernameUser;
    @FXML
    private PasswordField txtConfirmPwUser;
    @FXML
    private PasswordField txtPasswordUser;
    @FXML
    private Button btnSaveUser;
    @FXML
    private Button btnDeleteUser;
    @FXML
    private Button btnClose;
    @FXML
    private TableView<User> userList;
    @FXML
    private RadioButton techChecker;
    @FXML
    private RadioButton salesChecker;
    @FXML
    private RadioButton managerChecker;
    @FXML
    private TableColumn userscln;
    @FXML
    private Button btnEditUsers;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label confirmPasswordLabel;
    private boolean txtInUsernameField;
    private boolean txtInNameField;
    private boolean txtInPasswordField;
    private boolean txtInConfirmField;
    private boolean userTypes;
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

        btnDeleteUser.setDisable(true);
        userList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnDeleteUser.setDisable(false);
            } else {
                btnDeleteUser.setDisable(true);
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

        userType.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                userTypes = true;
            } else {
                userTypes = false;
            }
            enableTheButtons();
        }));

    }

    private void enableTheButtons() {
        if (userTypes && txtInNameField && txtInConfirmField && txtInPasswordField && txtInUsernameField) {
            btnSaveUser.setDisable(false);
        }
    }

    @FXML
    private void handleSaveUser(ActionEvent actionEvent) throws Exception {
        if (isEdit) {
            editUser();
            isEdit = false;
            clearItAll();
            txtUsernameUser.setDisable(false);
            btnDeleteUser.setDisable(false);
            userList.getItems().clear();
            userList.setItems(getUModel().getObservableUsers());
        } else if (checkUsername()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("The username you are trying to create already exists");
            alert.showAndWait();
        } else {
            newUser();
            clearItAll();
        }
    }

    private boolean checkUsername(){
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
        txtUsernameUser.setDisable(false);
        txtPasswordUser.clear();
        txtConfirmPwUser.clear();
        salesChecker.setSelected(false);
        managerChecker.setSelected(false);
        techChecker.setSelected(false);
        userList.getSelectionModel().clearSelection();
    }

    private void editUser() throws Exception {
        User user = userList.getSelectionModel().getSelectedItem();
        user.setName(txtNameUser.getText());
        int userType = -1;

        if(techChecker.isSelected()) {
            userType = 2;
        } else if (managerChecker.isSelected()) {
            userType = 1;
        } else if (salesChecker.isSelected()) {
            userType = 3;
        }
        user.setUserTypeID(userType);

        if (!txtConfirmPwUser.getText().isEmpty()) {
            if (txtConfirmPwUser.equals(txtPasswordUser)){
                String salt = BCrypt.gensalt(10);
                String hashedPassword1 = BCrypt.hashpw(txtPasswordUser.getText(), salt);
                user.setPassword(hashedPassword1);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Passwords does not match");
                alert.setContentText("The supplied passwords does not match, please try again");
                alert.show();
                return;
            }
        }
        super.getUModel().updateUser(user);
    }

    private void newUser(){
        String username = txtUsernameUser.getText();
        String name = txtNameUser.getText();
        String password = txtPasswordUser.getText();
        String confirmPassword = txtConfirmPwUser.getText();

        Toggle selectedToggle = userType.getSelectedToggle();

        int userType = -1;
        if(selectedToggle == techChecker) {
            userType = 2;
        } else if (selectedToggle == managerChecker) {
            userType = 1;
        } else if (selectedToggle == salesChecker) {
            userType = 3;
        }

        String salt = BCrypt.gensalt(10);
        String hashedPassword1 = BCrypt.hashpw(password, salt);
        String hashedPassword2 = BCrypt.hashpw(confirmPassword, salt);

        try {
            if (hashedPassword1.equals(hashedPassword2)) {
                User u = new User(username,hashedPassword1,name,userType);
                super.getUModel().createUser(u);
            }
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }


    @FXML
    private void handleDeleteUser(ActionEvent actionEvent) {
        try {
            User user = userList.getSelectionModel().getSelectedItem();
            super.getUModel().deleteUser(user);
        } catch (Exception e) {
            displayError(e);
        }
    }

    @FXML
    private void handleCloseWindow(ActionEvent actionEvent) {
        closeWindow(btnClose);
    }

    private void insertIntoTable() throws Exception {
        userscln.setCellValueFactory(new PropertyValueFactory<>("name"));
        typecln.setCellValueFactory(new PropertyValueFactory<>("userType"));
        userList.getColumns().addAll();
        userList.setItems(super.getUModel().getObservableUsers());
    }

    @FXML
    private void handleEditUsers(ActionEvent actionEvent) {
        btnDeleteUser.setDisable(true);
        isEdit = true;
        User selectedUser = userList.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            txtNameUser.setText(selectedUser.getName());
            txtUsernameUser.setText(selectedUser.getUsername());
            txtUsernameUser.setDisable(true);

            passwordLabel.setText("New Password");
            confirmPasswordLabel.setText("Confirm New Password");

            if (selectedUser.getUserType().getId() == 3) {
                techChecker.setSelected(false);
                managerChecker.setSelected(false);
                salesChecker.setSelected(true);
            } else if (selectedUser.getUserType().getId() == 1) {
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

    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        clearItAll();
    }
}
