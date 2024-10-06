package com.dinidu.lk.pmt.controller;

import com.dinidu.lk.pmt.regex.Regex;
import com.dinidu.lk.pmt.utils.CustomAlert;
import com.dinidu.lk.pmt.utils.FeedbackUtil;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class LoginViewController extends BaseController{

    @FXML
    public AnchorPane loginPg; // Main AnchorPane for login page
    public Label feedbackpw;
    @FXML
    private TextField usernameField; // Username input field
    @FXML
    private PasswordField passwordField; // Password input field

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        Regex regex = new Regex();

        if (!regex.isMinLength(password)) {
            FeedbackUtil.showFeedback(feedbackpw, "Password must be at least 8 characters long.", Color.RED);
        } else if (!regex.containsUpperCase(password)) {
            FeedbackUtil.showFeedback(feedbackpw, "Password must contain at least one uppercase letter.", Color.RED);
        } else if (!regex.containsLowerCase(password)) {
            FeedbackUtil.showFeedback(feedbackpw, "Password must contain at least one lowercase letter.", Color.RED);
        } else if (!regex.containsDigit(password)) {
            FeedbackUtil.showFeedback(feedbackpw, "Password must contain at least one digit.", Color.RED);
        } else {
            CustomAlert.showAlert("CONFIRMATION","Login Successful");
        }
    }




    @FXML
    private void handleCancel() {
        usernameField.clear();
        passwordField.clear();
    }

    @FXML
    private void handleForgotPassword(MouseEvent mouseEvent) {
        transitionToScene(loginPg, "/view/forgetpassword/forget_password.fxml");
    }
}
