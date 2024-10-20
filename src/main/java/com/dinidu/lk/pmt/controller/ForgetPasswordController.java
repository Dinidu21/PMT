package com.dinidu.lk.pmt.controller;

import com.dinidu.lk.pmt.model.UserModel;
import com.dinidu.lk.pmt.regex.Regex;
import com.dinidu.lk.pmt.utils.CustomErrorAlert;
import com.dinidu.lk.pmt.utils.FeedbackUtil;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
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
import lombok.Getter;

import java.security.SecureRandom;
import java.util.Objects;
@Getter

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
    public static String userEmail;

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
        userEmail = fpid.getText();
        ResetPwViewController rst = new ResetPwViewController();
        rst.setUserEmail(userEmail);
        Regex rg = new Regex();

        if (userEmail == null || userEmail.isEmpty()) {
            FeedbackUtil.showFeedback(feedbackLabel, "Please enter your Email.", Color.RED);
            return;
        }

        if (!rg.isEmailValid(userEmail)) {
            FeedbackUtil.showFeedback(feedbackLabel, "Please Enter Valid Email", Color.RED);
            return;
        }

        if (!UserModel.isEmailRegistered(userEmail)) {
            FeedbackUtil.showFeedback(feedbackLabel, "Email is not registered.", Color.RED);
            return;
        }

        loadingIndicator.setVisible(true);
        int otp = generateOTP();
        new Thread(() -> {
            try {
                // MailUtil.sendMail(userEmail, otp);

                Thread.sleep(2000);

                Platform.runLater(() -> {
                    loadingIndicator.setVisible(false);
                    FeedbackUtil.showFeedback(feedbackLabel, "OTP sent to your email!", Color.GREEN);
                    loadOTPView(otp);
                    System.out.println("Your otp is : "+otp);
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
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

    private void loadOTPView(int otp) {
        try {
            Stage otpStage = new Stage();
            otpStage.initStyle(StageStyle.TRANSPARENT);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/forgetpassword/otp-view.fxml"));
            Parent root = loader.load();
            OTPViewController otpController = loader.getController();
            otpController.setGeneratedOTP(otp);

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
            CustomErrorAlert.showAlert("ERROR","Error : "+e);
        }
    }
}
