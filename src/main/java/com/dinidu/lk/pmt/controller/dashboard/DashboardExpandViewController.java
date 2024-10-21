package com.dinidu.lk.pmt.controller.dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class DashboardExpandViewController {

    @FXML
    private ComboBox<String> dashboardComboBox; // Define ComboBox with fx:id

    public void initialize() {
        // Set the initial selection
        dashboardComboBox.setValue("Business Overview");

        // Add items to the ComboBox
        dashboardComboBox.getItems().addAll(
                "Business Overview",
                "My Dashboard",
                "Webteam Dashboard"
        );
    }
}
