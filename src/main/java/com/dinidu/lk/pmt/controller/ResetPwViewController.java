package com.dinidu.lk.pmt.controller;

import com.dinidu.lk.pmt.regex.Regex;
import com.dinidu.lk.pmt.utils.CustomAlert;
import com.dinidu.lk.pmt.utils.FeedbackUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.dinidu.lk.pmt.model.UserModel;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.sql.SQLException;

public class ResetPwViewController extends BaseController {

    public AnchorPane pwPage;
    @FXML
    private TextField newPasswordField;

    @FXML
    private TextField confirmPasswordField;

    @FXML
    private Button resetPasswordBtn;

    @FXML
    private Label passwordFeedback;

    private final Regex regex = new Regex();
    public static String userEmail;

    public void setUserEmail(String userEmail) {
        ResetPwViewController.userEmail = userEmail;
        System.out.println("Email: " + userEmail);
    }

    @FXML
    public void initialize() {
        // Listener for new password validation
        newPasswordField.textProperty().addListener((observable, oldValue, newValue) -> validatePassword(newValue));

        // Listener for confirm password validation
        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> validatePasswordMatch(newValue));

        // Handle reset password action
        resetPasswordBtn.setOnAction(e -> {
            try {
                handleChangePassword();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    // Handle the change password action
    private void handleChangePassword() throws SQLException {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            FeedbackUtil.showFeedback(passwordFeedback, "Please fill out both fields.", Color.RED);
            return;
        }

        if (!regex.isMinLength(newPassword)) {
            FeedbackUtil.showFeedback(passwordFeedback, "Password must meet the required criteria.", Color.RED);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            FeedbackUtil.showFeedback(passwordFeedback, "Passwords do not match.", Color.RED);
            return;
        }

        boolean updateSuccessful = UserModel.updatePassword(userEmail, newPassword);
        if (updateSuccessful) {
            FeedbackUtil.showFeedback(passwordFeedback, "Password changed successfully!", Color.GREEN);
            CustomAlert.showAlert("Success", "Your password has been updated.");
            transitionToScene(pwPage, "/view/login-view.fxml");
        } else {
            FeedbackUtil.showFeedback(passwordFeedback, "Failed to change password. Please try again.", Color.RED);
        }
    }
    // Validate the new password based on predefined rules
    private void validatePassword(String password) {
        if (password.isEmpty()) {
            FeedbackUtil.showFeedback(passwordFeedback, "Password cannot be empty.", Color.RED);
            return;
        }

        if (!regex.containsUpperCase(password)) {
            FeedbackUtil.showFeedback(passwordFeedback, "Must contain at least one uppercase letter.", Color.RED);
        } else if (!regex.containsLowerCase(password)) {
            FeedbackUtil.showFeedback(passwordFeedback, "Must contain at least one lowercase letter.", Color.RED);
        } else if (!regex.containsDigit(password)) {
            FeedbackUtil.showFeedback(passwordFeedback, "Must contain at least one digit.", Color.RED);
        } else if (!regex.isMinLength(password)) {
            FeedbackUtil.showFeedback(passwordFeedback, "Must be at least 8 characters long.", Color.RED);
        } else if (!regex.containsSpecialChar(password)) {
            FeedbackUtil.showFeedback(passwordFeedback, "Must contain at least one special character.", Color.RED);
        } else {
            FeedbackUtil.showFeedback(passwordFeedback, "Password looks good!", Color.GREEN);
        }
    }

    // Validate that the passwords entered both fields match
    private void validatePasswordMatch(String confirmPassword) {
        String newPassword = newPasswordField.getText();

        if (confirmPassword.isEmpty()) {
            FeedbackUtil.showFeedback(passwordFeedback, "Please confirm your new password.", Color.RED);
        } else if (!confirmPassword.equals(newPassword)) {
            FeedbackUtil.showFeedback(passwordFeedback, "Passwords do not match.", Color.RED);
        } else {
            FeedbackUtil.showFeedback(passwordFeedback, "Passwords match!", Color.GREEN);
        }
    }

    public void backtoLogin(MouseEvent mouseEvent) {transitionToScene(pwPage, "/view/login-view.fxml");}
}
