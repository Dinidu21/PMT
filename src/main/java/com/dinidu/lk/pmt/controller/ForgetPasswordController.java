package com.dinidu.lk.pmt.controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ForgetPasswordController {

    @FXML
    public TextField fpid; // Username input field
    @FXML
    public AnchorPane forgetpg; // Main AnchorPane for Forget Password page
    @FXML
    private ProgressIndicator loadingIndicator; // Loading indicator for sending email
    @FXML
    private Label feedbackLabel; // Label for feedback messages

    @FXML
    public void initialize() {
        // Set up the initial state of the loading indicator and feedback label
        loadingIndicator.setVisible(false);
        feedbackLabel.setText("");

        // Animate background color (optional)
        animateBackground();

        // Add focus listener to the TextField for animation
        fpid.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Scale up when focused
                fpid.setScaleX(1.05);
                fpid.setScaleY(1.05);
            } else {
                // Scale back down when focus is lost
                fpid.setScaleX(1.0);
                fpid.setScaleY(1.0);
            }
        });
    }

    private void animateBackground() {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), forgetpg);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.9);
        fadeTransition.setCycleCount(FadeTransition.INDEFINITE);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
    }

    @FXML
    private void handleSendResetEmail() {
        String username = fpid.getText();

        if (username.isEmpty()) {
            showFeedback("Please enter your username.", Color.RED);
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
                showFeedback("Reset link sent to your email!", Color.GREEN); // Show success message
            });
        }).start();
    }

    private void showFeedback(String message, Color color) {
        feedbackLabel.setText(message);
        feedbackLabel.setTextFill(color);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), feedbackLabel);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), feedbackLabel);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeIn.play();

        fadeIn.setOnFinished(event -> {
            fadeOut.play();
            fadeOut.setOnFinished(e -> feedbackLabel.setText("")); // Clear message after fading out
        });
    }
}
