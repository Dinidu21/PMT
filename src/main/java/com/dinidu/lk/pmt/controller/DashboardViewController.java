package com.dinidu.lk.pmt.controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button; // Import Button
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DashboardViewController extends BaseController {

    public AnchorPane dashboardpg; // main dashboard anchor
    public AnchorPane contentPane; // content area for dynamic views
    public AnchorPane card1, card2, card3, card4, card5, card6, card7, card8; // references to card containers

    // List to hold all navigation buttons
    private List<Button> navButtons = new ArrayList<>();
    private Button activeButton; // Track the currently active button

    public void initialize() {
        // Initialize buttons (assuming these buttons are accessible in your FXML)
        // Add buttons to the navButtons list
        navButtons.add((Button) dashboardpg.lookup("#projectsButton"));
        navButtons.add((Button) dashboardpg.lookup("#timesheetsButton"));
        navButtons.add((Button) dashboardpg.lookup("#taskButton"));
        navButtons.add((Button) dashboardpg.lookup("#reportsButton"));
        navButtons.add((Button) dashboardpg.lookup("#dashboardButton"));
    }

    public void clickOnReports(ActionEvent actionEvent) {
        handleButtonClick((Button) actionEvent.getSource());
        hideCardContainers();
        navigateTo("/view/nav-buttons/report-view.fxml");
    }

    public void clickOnTask(ActionEvent actionEvent) {
        handleButtonClick((Button) actionEvent.getSource());
        hideCardContainers();
        navigateTo("/view/nav-buttons/task-view.fxml");
    }

    public void clickOnDashboard(ActionEvent actionEvent) {
        handleButtonClick((Button) actionEvent.getSource());
        hideCardContainers();
        navigateTo("/view/nav-buttons/dashboard-expand-view.fxml");
    }

    public void myProfile(MouseEvent mouseEvent) {
        hideCardContainers();
        navigateTo("/view/nav-buttons/profile-view.fxml");
    }

    public void clickOnProjects(ActionEvent actionEvent) {
        handleButtonClick((Button) actionEvent.getSource());
        hideCardContainers();
        navigateTo("/view/nav-buttons/project-view.fxml");
    }

    public void clickOnTimesheets(ActionEvent actionEvent) {
        handleButtonClick((Button) actionEvent.getSource());
        hideCardContainers();
        navigateTo("/view/nav-buttons/timesheet-view.fxml");
    }

    public void notifyClick(MouseEvent mouseEvent) {
        hideCardContainers();
        navigateTo("/view/nav-buttons/notify-view.fxml");
    }

    public void settingsClick(MouseEvent mouseEvent) {
        hideCardContainers();
        navigateTo("/view/nav-buttons/settings-view.fxml");
    }

    public void navigateTo(String fxmlPath) {
        try {
            // Clear only the contentPane (not the entire dashboard)
            contentPane.getChildren().clear();
            AnchorPane load = FXMLLoader.load(getClass().getResource(fxmlPath));

            // Bind the loaded content to the size of the contentPane
            load.prefWidthProperty().bind(contentPane.widthProperty());
            load.prefHeightProperty().bind(contentPane.heightProperty());

            // Add fade-in transition for smooth appearance
            FadeTransition fadeIn = new FadeTransition(Duration.millis(750), load); // 750ms fade-in
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

            contentPane.getChildren().add(load);

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load page!").show();
        }
    }

    // Method to hide card containers
    private void hideCardContainers() {
        card1.setVisible(false);
        card2.setVisible(false);
        card3.setVisible(false);
        card4.setVisible(false);
        card5.setVisible(false);
        card6.setVisible(false);
        card7.setVisible(false);
        card8.setVisible(false);
    }

    // Method to show card containers
    private void showCardContainers() {
        // Show each card container with a fade-in transition
        fadeIn(card1);
        fadeIn(card2);
        fadeIn(card3);
        fadeIn(card4);
        fadeIn(card5);
        fadeIn(card6);
        fadeIn(card7);
        fadeIn(card8);
    }

    private void fadeIn(Node node) {
        node.setVisible(true);  // Make the node visible before applying animation
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(700), node);
        fadeTransition.setFromValue(0.0); // Start fully transparent
        fadeTransition.setToValue(1.0);   // End fully opaque
        fadeTransition.play();
    }

    public void mainIconOnClick(MouseEvent mouseEvent) {
        contentPane.getChildren().clear();
        showCardContainers();
    }

    // Handle button click and style changes
    private void handleButtonClick(Button clickedButton) {
        // Reset the active button if one is already active
        if (activeButton != null) {
            activeButton.getStyleClass().remove("active");
        }

        // Set the clicked button as active
        clickedButton.getStyleClass().add("active");
        activeButton = clickedButton;
    }
}
