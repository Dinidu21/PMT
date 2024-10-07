package com.dinidu.lk.pmt.controller;

import com.dinidu.lk.pmt.regex.Regex;
import com.dinidu.lk.pmt.utils.FeedbackUtil;
import com.dinidu.lk.pmt.utils.MailUtil; // Import MailUtil
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.Random;

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

        if (userEmail == null || userEmail.isEmpty()) {
            FeedbackUtil.showFeedback(feedbackLabel, "Please enter your Email.", Color.RED);
            return;
        } else if (!rg.isEmailValid(userEmail)) {
            FeedbackUtil.showFeedback(feedbackLabel, "Please Enter Valid Email", Color.RED);
            return;
        }

        loadingIndicator.setVisible(true);

        // Generate a random OTP
        int otp = generateOTP();

        new Thread(() -> {
            try {
                // Send the OTP email using MailUtil
                MailUtil.sendMail(userEmail, otp); // Send the email with OTP

                // Simulate delay for sending email (optional)
                Thread.sleep(2000);

                // Update UI on the JavaFX Application Thread after sending email
                javafx.application.Platform.runLater(() -> {
                    loadingIndicator.setVisible(false); // Hide loading indicator
                    FeedbackUtil.showFeedback(feedbackLabel, "OTP sent to your email!", Color.GREEN); // Show success message
                });

            } catch (Exception e) {
                e.printStackTrace();

                // Update UI on the JavaFX Application Thread in case of error
                javafx.application.Platform.runLater(() -> {
                    loadingIndicator.setVisible(false); // Hide loading indicator
                    FeedbackUtil.showFeedback(feedbackLabel, "Failed to send OTP.", Color.RED); // Show error message
                });

            }
        }).start();
    }

    private int generateOTP() {
        Random random = new Random();
        return 100000 + random.nextInt(900000); // Generate a 6-digit OTP
    }

    @FXML
    private void handleBackToLogin() {
        transitionToScene(forgetpg, "/view/login-view.fxml");
    }
}
