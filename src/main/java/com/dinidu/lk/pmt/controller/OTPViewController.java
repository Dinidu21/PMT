package com.dinidu.lk.pmt.controller;

import com.dinidu.lk.pmt.utils.CustomAlert;
import com.dinidu.lk.pmt.utils.CustomErrorAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

public class OTPViewController implements Initializable {

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

    private int generatedOTP;

    public void setGeneratedOTP(int generatedOTP) {
        this.generatedOTP = generatedOTP;
    }

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
           CustomErrorAlert.showAlert("Error", "Please enter the complete OTP.");
            return;
        }

        if (Integer.parseInt(enteredOTP) == generatedOTP) {
            CustomAlert.showAlert("Confirmation", "OTP Verified! Password reset can proceed.");
        } else {
            CustomErrorAlert.showAlert("Error", "Invalid OTP. Please try again.");
        }
    }
}
