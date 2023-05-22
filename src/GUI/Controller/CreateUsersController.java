package GUI.Controller;

import BE.User;
import BLL.BCrypt;
import GUI.Model.UsersModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.util.Optional;

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
    public void setup() {
        try {
            super.setUModel(new UsersModel());
            insertIntoTable();
            btnSaveUser.setDisable(true);
            beGoneButton();
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * Adds listeners to text-fields and radiobuttons to see if something have been written or selected
     * to determine the state of the buttons in the view.
     */
    private void beGoneButton(){
        btnEditUsers.setDisable(true);
        userList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnEditUsers.setDisable(newValue == null);
        });

        btnDeleteUser.setDisable(true);
        userList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnDeleteUser.setDisable(newValue == null);
        });

        txtUsernameUser.textProperty().addListener((observable, oldValue, newValue) -> {
            txtInUsernameField = newValue != null;
            enableTheButtons();
        });

        txtNameUser.textProperty().addListener((observable, oldValue, newValue) -> {
            txtInNameField = newValue != null;
            enableTheButtons();
        });

        txtPasswordUser.textProperty().addListener((observable, oldValue, newValue) -> {
            txtInPasswordField = newValue != null;
            enableTheButtons();
        });

        txtConfirmPwUser.textProperty().addListener((observable, oldValue, newValue) -> {
            txtInConfirmField = newValue != null;
            enableTheButtons();
        });

        userType.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            userTypes = newValue != null;
            enableTheButtons();
        }));

    }

    /**
     * Enables the save button  if the fields have something written in them and a radiobutton have been selected.
     */
    private void enableTheButtons() {
        if (userTypes && txtInNameField && txtInConfirmField && txtInPasswordField && txtInUsernameField) {
            btnSaveUser.setDisable(false);
        }
    }

    /**
     * Saves the new or edited user to the database and checks if there is another use with the same name.
     * @param actionEvent
     */
    @FXML
    private void handleSaveUser(ActionEvent actionEvent) {
        try {
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
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * Compares the entered username with the other users.
     * @return True if no other user have that name.
     */
    private boolean checkUsername(){
        for (User user : userList.getItems()) {
            if (txtUsernameUser.getText().equals(user.getUsername())){
                return true;
            }
        }
        return false;
    }

    /**
     * Clears the text-fields and the selection of the radiobuttons.
     */
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

    /**
     * Edits the selected user to the edited values.
     */
    private void editUser() {
        try {
            User user = userList.getSelectionModel().getSelectedItem();
            user.setName(txtNameUser.getText());
            int userType = -1;

            if (techChecker.isSelected()) {
                userType = 2;
            } else if (managerChecker.isSelected()) {
                userType = 1;
            } else if (salesChecker.isSelected()) {
                userType = 3;
            }
            user.setUserTypeID(userType);

            if (!txtConfirmPwUser.getText().isEmpty()) {
                if (txtConfirmPwUser.equals(txtPasswordUser)) {
                    String salt = BCrypt.gensalt(10);
                    String hashedPassword1 = BCrypt.hashpw(txtPasswordUser.getText(), salt);
                    user.setPassword(hashedPassword1);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Passwords does not match");
                    alert.setContentText("The supplied passwords does not match, please try again");
                    alert.show();
                    return;
                }
            }
            super.getUModel().updateUser(user);
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * Creates a new user with the provided values from the text-fields and radiobuttons.
     */
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


    /**
     * Creates a confirmation pop-up. If the yes button is pressed the selected user is deleted.
     * @param actionEvent
     */
    @FXML
    private void handleDeleteUser(ActionEvent actionEvent) {
        try {
            User user = userList.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deletion Confirmation");
            alert.setContentText("Are you sure you want to delete " + user.getName().toUpperCase());
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                super.getUModel().deleteUser(user);
            }

        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * Closes the window.
     * @param actionEvent
     */
    @FXML
    private void handleCloseWindow(ActionEvent actionEvent) {
        closeWindow(btnClose);
    }

    /**
     * Inserts all users into the user table.
     * @throws Exception
     */
    private void insertIntoTable() throws Exception {
        userscln.setCellValueFactory(new PropertyValueFactory<>("name"));
        typecln.setCellValueFactory(new PropertyValueFactory<>("userType"));
        userList.getColumns().addAll();
        userList.setItems(super.getUModel().getObservableUsers());
    }

    /**
     * Adds the values from the selected user into their respective fields.
     * @param actionEvent
     */
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
