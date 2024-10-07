package com.dinidu.lk.pmt.controller;

import com.dinidu.lk.pmt.db.DBConnection;
import com.dinidu.lk.pmt.model.UserModel;
import com.dinidu.lk.pmt.regex.Regex;
import com.dinidu.lk.pmt.utils.CustomAlert;
import com.dinidu.lk.pmt.utils.FeedbackUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            UserModel userModel = new UserModel();
            boolean isVerified = userModel.verifyUser(username, password);
            if (isVerified) {
                CustomAlert.showAlert("Confirmation","Login successful !");
            } else {
                FeedbackUtil.showFeedback(feedbackpw, "Invalid username or password.", Color.RED);
            }
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

    public void onSignUp(ActionEvent actionEvent) {
        transitionToScene(loginPg, "/view/signUp-view.fxml");
    }
}
