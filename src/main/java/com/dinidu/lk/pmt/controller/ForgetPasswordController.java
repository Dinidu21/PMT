package com.dinidu.lk.pmt.controller;

import com.dinidu.lk.pmt.regex.Regex;
import com.dinidu.lk.pmt.utils.FeedbackUtil;
import com.dinidu.lk.pmt.utils.MailUtil;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.security.SecureRandom;
import java.util.Objects;

public class ForgetPasswordController extends BaseController {

    private static double xOffset = 0;
    private static double yOffset = 0;

    @FXML
    public TextField fpid;
    @FXML
    public AnchorPane forgetpg;
    @FXML
    private ProgressIndicator loadingIndicator;
    @FXML
    private Label feedbackLabel;

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
        int otp = generateOTP();
        new Thread(() -> {
            try {
                //MailUtil.sendMail(userEmail, otp);

                Thread.sleep(2000);

                javafx.application.Platform.runLater(() -> {
                    loadingIndicator.setVisible(false);
                    FeedbackUtil.showFeedback(feedbackLabel, "OTP sent to your email!", Color.GREEN);
                    loadOTPView();
                });
            } catch (Exception e) {
                e.printStackTrace();
                javafx.application.Platform.runLater(() -> {
                    loadingIndicator.setVisible(false);
                    FeedbackUtil.showFeedback(feedbackLabel, "Failed to send OTP.", Color.RED);
                });
            }
        }).start();
    }

    private int generateOTP() {
        SecureRandom random = new SecureRandom();
        return 100000 + random.nextInt(900000);
    }

    @FXML
    private void handleBackToLogin() {
        transitionToScene(forgetpg, "/view/login-view.fxml");
    }

    private void loadOTPView() {
        try {
            Stage otpStage = new Stage();
            otpStage.initStyle(StageStyle.TRANSPARENT);

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/forgetpassword/otpEnter-view.fxml")));
            root.setOnMousePressed(mouseEvent -> {
                xOffset = mouseEvent.getSceneX();
                yOffset = mouseEvent.getSceneY();
            });
            root.setOnMouseDragged(mouseEvent -> {
                otpStage.setX(mouseEvent.getScreenX() - xOffset);
                otpStage.setY(mouseEvent.getScreenY() - yOffset);
            });

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            otpStage.setScene(scene);
            otpStage.getIcons().add(new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResourceAsStream("/asserts/icons/PN.png"))));
            otpStage.centerOnScreen();
            otpStage.show();
            Stage currentStage = (Stage) forgetpg.getScene().getWindow();
            currentStage.hide();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
