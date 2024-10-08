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
    public AnchorPane registerPg;

    private final Regex regex = new Regex();

    @FXML
    private void handleRegister(ActionEvent event) {
        clearFeedback();
        boolean isValid = true;

        if (usernameField.getText().isEmpty()) {
            FeedbackUtil.showFeedback(UsernameV, "Username cannot be empty.", Color.RED);
            isValid = false;
        }

        if (!regex.isEmailValid(emailField.getText())) {
            FeedbackUtil.showFeedback(emailV, "Invalid email address.", Color.RED);
            isValid = false;
        }

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

        if (isValid) {
            UserDTO userDTO = new UserDTO(
                    usernameField.getText(),
                    password,
                    emailField.getText(),
                    phoneField.getText()
            );

            UserModel userModel = new UserModel();
            try {
                boolean isSaved = userModel.saveUser(userDTO);
                if (isSaved) {
                    CustomAlert.showAlert("CONFIRMATION", "User has been saved successfully!");
                    clearContent();
                } else {
                    showError("Failed to save user! Please try again.");
                }
            } catch (Exception e) {
                showError("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        clearContent();
        clearFeedback();
    }

    @FXML
    private void handleLogin() {
        transitionToScene(registerPg, "/view/login-view.fxml");
    }

    private void clearFeedback() {
        UsernameV.setText("");
        PassV.setText("");
        emailV.setText("");
        phoneV.setText("");
    }

    private void clearContent() {
        usernameField.clear();
        passwordField.clear();
        emailField.clear();
        phoneField.clear();
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }
}
