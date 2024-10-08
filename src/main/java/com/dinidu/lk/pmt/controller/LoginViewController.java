package com.dinidu.lk.pmt.controller;

import com.dinidu.lk.pmt.model.UserModel;
import com.dinidu.lk.pmt.regex.Regex;
import com.dinidu.lk.pmt.utils.CustomAlert;
import com.dinidu.lk.pmt.utils.CustomErrorAlert;
import com.dinidu.lk.pmt.utils.FeedbackUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class LoginViewController extends BaseController{

    @FXML
    public AnchorPane loginPg;
    public Label feedbackpw;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

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
            UserModel userModel = new UserModel();
            String result = userModel.verifyUser(username, password);

            switch (result) {
                case "SUCCESS":
                    CustomAlert.showAlert("Confirmation", "Login successful!");
                    transitionToScene(loginPg,"/view/dasboard-view.fxml");
                    break;
                case "INVALID_USERNAME":
                    FeedbackUtil.showFeedback(feedbackpw, "Invalid username. Please check your username.", Color.RED);
                    break;
                case "INVALID_PASSWORD":
                    FeedbackUtil.showFeedback(feedbackpw, "Incorrect password. Please try again.", Color.RED);
                    break;
                case "ERROR":
                    FeedbackUtil.showFeedback(feedbackpw, "An error occurred. Please try again later.", Color.RED);
                    break;
                default:
                    FeedbackUtil.showFeedback(feedbackpw, "An unknown error occurred.", Color.RED);
                    break;
            }
        }
    }

    @FXML
    private void handleCancel() {
        usernameField.clear();
        passwordField.clear();
    }

    @FXML
    private void handleForgotPassword() {
        transitionToScene(loginPg, "/view/forgetpassword/forget_password.fxml");
    }

    public void onSignUp() {
        transitionToScene(loginPg, "/view/signUp-view.fxml");
    }
}
