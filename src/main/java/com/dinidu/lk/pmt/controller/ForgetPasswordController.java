package com.dinidu.lk.pmt.controller;

import com.dinidu.lk.pmt.regex.Regex;
import com.dinidu.lk.pmt.utils.FeedbackUtil;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ForgetPasswordController extends BaseController {

    @FXML
    public TextField fpid; // Email input field
    @FXML
    public AnchorPane forgetpg; // Main AnchorPane for Forget Password page
    @FXML
    private ProgressIndicator loadingIndicator; // Loading indicator for sending email
    @FXML
    private Label feedbackLabel; // Label for feedback messages

    @FXML
    public void initialize() {
        loadingIndicator.setVisible(false);
        feedbackLabel.setText("");
        animateBackground();

        fpid.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                fpid.setScaleX(1.05);
                fpid.setScaleY(1.05);
            } else {
                fpid.setScaleX(1.0);
                fpid.setScaleY(1.0);
            }
        });
    }

    private void animateBackground() {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(4), forgetpg);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.9);
        fadeTransition.setCycleCount(FadeTransition.INDEFINITE);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
    }

    @FXML
    private void handleSendResetEmail() {
        String userEmail = fpid.getText();
        Regex rg = new Regex();
        if (userEmail.isEmpty()) {
            FeedbackUtil.showFeedback(feedbackLabel,"Please enter your Email.", Color.RED);
            return;
        } else if (!rg.isEmailValid(userEmail)) {
            FeedbackUtil.showFeedback(feedbackLabel,"Please Enter Valid Email", Color.RED);
            return;
        }

        loadingIndicator.setVisible(true); // Show loading indicator

        // Simulate sending email (replace this with actual logic)
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Simulate delay for sending email
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            javafx.application.Platform.runLater(() -> {
                loadingIndicator.setVisible(false); // Hide loading indicator
                FeedbackUtil.showFeedback(feedbackLabel,"Reset link sent to your email!", Color.GREEN); // Show success message
            });
        }).start();
    }

    @FXML
    private void handleBackToLogin() {
        transitionToScene(forgetpg, "/view/login-view.fxml");
    }

}
