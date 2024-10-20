package com.dinidu.lk.pmt.controller;

import com.dinidu.lk.pmt.utils.CustomAlert;
import com.dinidu.lk.pmt.utils.CustomErrorAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Setter;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class OTPViewController extends BaseController implements Initializable {
    private static double xOffset = 0;
    private static double yOffset = 0;
    private static final int MAX_ATTEMPTS = 3;  // Maximum allowed attempts
    private int attemptCounter = 0;  // Track the number of invalid OTP attempts

    @FXML
    public AnchorPane otpPg;

    @FXML
    public Label sendotpEmail;

    @FXML
    public TextField otpField1;

    @FXML
    public TextField otpField2;

    @FXML
    public TextField otpField3;

    @FXML
    public TextField otpField4;

    @FXML
    public TextField otpField5;

    @FXML
    public TextField otpField6;

    @FXML
    public Button submitBtn;

    @Setter
    private int generatedOTP;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setNumericField(otpField1);
        setNumericField(otpField2);
        setNumericField(otpField3);
        setNumericField(otpField4);
        setNumericField(otpField5);
        setNumericField(otpField6);
        setFieldListeners();
    }

    private void setNumericField(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.length() > 1) {
                textField.setText(newValue.substring(0, 1));
            }
        });
    }

    private void setFieldListeners() {
        otpField1.setOnKeyReleased(event -> moveToNextField(otpField1, otpField2));
        otpField2.setOnKeyReleased(event -> moveToNextField(otpField2, otpField3));
        otpField3.setOnKeyReleased(event -> moveToNextField(otpField3, otpField4));
        otpField4.setOnKeyReleased(event -> moveToNextField(otpField4, otpField5));
        otpField5.setOnKeyReleased(event -> moveToNextField(otpField5, otpField6));
        otpField6.setOnKeyReleased(event -> moveToNextField(otpField6, null));
    }

    private void moveToNextField(TextField currentField, TextField nextField) {
        if (currentField.getText().length() == 1 && nextField != null) {
            nextField.requestFocus();
        }
    }

    public void handleSubmitOTP(ActionEvent actionEvent) {
        String enteredOTP = otpField1.getText() + otpField2.getText() + otpField3.getText() +
                otpField4.getText() + otpField5.getText() + otpField6.getText();
        if (enteredOTP.length() != 6) {
            resetOtpFieldBorders();  // Reset borders when the OTP is incomplete
            CustomErrorAlert.showAlert("Error", "Please enter the complete OTP.");
            return;
        }

        if (Integer.parseInt(enteredOTP) == generatedOTP) {
            resetOtpFieldBorders();  // Reset borders after successful OTP verification
            CustomAlert.showAlert("Confirmation", "OTP Verified! Password reset can proceed.");
            loadPasswordResetScreen();  // Call a method to load the next screen for password reset
        } else {
            attemptCounter++;  // Increment the failed attempt counter
            if (attemptCounter >= MAX_ATTEMPTS) {
                CustomErrorAlert.showAlert("Error", "Invalid OTP. Too many attempts. Please try again later.");
                redirectToLogin();  // Redirect to the login page after max attempts
            } else {
                CustomErrorAlert.showAlert("Error", "Invalid OTP. Please try again.");
                setInvalidOtpFieldBorders();  // Change borders to red on invalid OTP
            }
        }
    }

    // Method to set borders to red for invalid OTP
    private void setInvalidOtpFieldBorders() {
        otpField1.setStyle("-fx-border-color: red;");
        otpField2.setStyle("-fx-border-color: red;");
        otpField3.setStyle("-fx-border-color: red;");
        otpField4.setStyle("-fx-border-color: red;");
        otpField5.setStyle("-fx-border-color: red;");
        otpField6.setStyle("-fx-border-color: red;");
    }

    // Method to reset the border styles to default
    private void resetOtpFieldBorders() {
        otpField1.setStyle("");
        otpField2.setStyle("");
        otpField3.setStyle("");
        otpField4.setStyle("");
        otpField5.setStyle("");
        otpField6.setStyle("");
    }


    private void redirectToLogin() {
        try {
            Stage currentStage = (Stage) otpPg.getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login-view.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (Exception e) {
            CustomErrorAlert.showAlert("ERROR", "Error while redirecting to login: " + e.getMessage());
        }
    }

    private void loadPasswordResetScreen() {
        // Method to load password reset screen after successful OTP verification
        try {
            Stage otpStage = new Stage();
            otpStage.initStyle(StageStyle.TRANSPARENT);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/forgetpassword/reset-pw.fxml"));
            Parent root = loader.load();
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
            Stage currentStage = (Stage) otpPg.getScene().getWindow();
            currentStage.hide();
        } catch (Exception e) {
            CustomErrorAlert.showAlert("ERROR", "Error while loading password reset screen: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public void backtoLogin(MouseEvent mouseEvent) { transitionToScene(otpPg, "/view/login-view.fxml");}
}
