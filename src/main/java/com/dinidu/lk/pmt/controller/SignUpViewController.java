package com.dinidu.lk.pmt.controller;

import com.dinidu.lk.pmt.dto.UserDTO;
import com.dinidu.lk.pmt.model.UserModel;
import com.dinidu.lk.pmt.regex.Regex;
import com.dinidu.lk.pmt.utils.CustomAlert;
import com.dinidu.lk.pmt.utils.FeedbackUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import java.sql.SQLException;

public class SignUpViewController extends BaseController {

    public Button registerButton;
    public Button cancelButton;
    public TextField phoneField;
    public TextField emailField;
    public PasswordField passwordField;
    public TextField usernameField;
    public Label UsernameV;
    public Label PassV;
    public Label emailV;
    public Label phoneV;
    @FXML
    private AnchorPane registerPg;

    private final Regex regex = new Regex();

    @FXML
    private void handleRegister(ActionEvent event) {
        // Clear any previous feedback
        clearFeedback();

        // Validate all fields
        boolean isValid = true;

        // Validate username
        if (usernameField.getText().isEmpty()) {
            FeedbackUtil.showFeedback(UsernameV, "Username cannot be empty.", Color.RED);
            isValid = false;
        }

        // Validate email
        if (!regex.isEmailValid(emailField.getText())) {
            FeedbackUtil.showFeedback(emailV, "Invalid email address.", Color.RED);
            isValid = false;
        }

        // Validate password
        String password = passwordField.getText();
        if (!regex.isMinLength(password)) {
            FeedbackUtil.showFeedback(PassV, "Password must be at least 8 characters long.", Color.RED);
            isValid = false;
        } else if (!regex.containsUpperCase(password)) {
            FeedbackUtil.showFeedback(PassV, "Password must contain at least one uppercase letter.", Color.RED);
            isValid = false;
        } else if (!regex.containsLowerCase(password)) {
            FeedbackUtil.showFeedback(PassV, "Password must contain at least one lowercase letter.", Color.RED);
            isValid = false;
        } else if (!regex.containsDigit(password)) {
            FeedbackUtil.showFeedback(PassV, "Password must contain at least one digit.", Color.RED);
            isValid = false;
        }

        if (!regex.isPhoneNumberValid(phoneField.getText())) {
            FeedbackUtil.showFeedback(phoneV, "Invalid phone number. It must contain 10 digits.", Color.RED);
            isValid = false;
        }
        // If all fields are valid, navigate to the dashboard
        if (isValid) {
                UserDTO userDTO = new UserDTO(
                        usernameField.getText(),
                        passwordField.getText(),
                        emailField.getText(),
                        phoneField.getText()
                );

                UserModel userModel = new UserModel();
                try {
                    boolean isSaved = userModel.saveUser(userDTO);
                    if (isSaved) {
                        CustomAlert.showAlert("CONFIRMATION","User Has been saved Successfully !");
                        clearcontent();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to save user!").show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Error occurred while saving user: " + e.getMessage()).show();
                }

        }

    }

    @FXML
    private void handleCancel(ActionEvent event) {
        clearcontent();
        clearFeedback();
    }

    @FXML
    private void handleLogin(javafx.scene.input.MouseEvent event) {
        transitionToScene(registerPg, "/view/login-view.fxml");
    }

    private void clearFeedback() {
        UsernameV.setText("");
        PassV.setText("");
        emailV.setText("");
        phoneV.setText("");
    }
    private void clearcontent(){
        usernameField.clear();
        passwordField.clear();
        emailField.clear();
        phoneField.clear();
    }
}
