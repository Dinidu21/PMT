package com.dinidu.lk.pmt.controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;

public class DashboardViewController extends BaseController{

    public AnchorPane dashboardpg; // main dashboard anchor
    public AnchorPane contentPane; // content area for dynamic views
    public AnchorPane card1, card2, card3, card4, card5, card6, card7, card8; // references to card containers

    public void clickOnReports(ActionEvent actionEvent) {
        hideCardContainers();
        navigateTo("/view/report-view.fxml");
    }

    public void clickOnTask(ActionEvent actionEvent) {

    }

    public void clickOnDashboard(ActionEvent actionEvent) {

    }

    public void myProfile(MouseEvent mouseEvent) {

    }

    public void clickOnProjects(ActionEvent actionEvent) {

    }

    public void clickOnTimesheets(ActionEvent actionEvent) {

    }

    public void notifClick(MouseEvent mouseEvent) {

    }

    public void settingsClick(MouseEvent mouseEvent) {

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
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), load); // 500ms fade-in
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

    // Optional: Method to show card containers again (if needed for other navigation)
    private void showCardContainers() {
        card1.setVisible(true);
        card2.setVisible(true);
        card3.setVisible(true);
        card4.setVisible(true);
        card5.setVisible(true);
        card6.setVisible(true);
        card7.setVisible(true);
        card8.setVisible(true);
    }

    public void mainIconOnClick(MouseEvent mouseEvent) {
        navigateTo("/view/dashboard-view.fxml");
        showCardContainers();
    }
}
