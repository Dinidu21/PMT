package com.dinidu.lk.pmt.controller.dashboard;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class ReportViewController {

    @FXML
    private AnchorPane cardContainer;

    @FXML
    private AnchorPane mainContentPane;

    private boolean isCardContainerVisible = false;

    @FXML
    public void toggleCardContainer() {
        if (isCardContainerVisible) {
            hideCardContainer();
        } else {
            showCardContainer();
        }
    }

    private void showCardContainer() {
        // Fade in the cardContainer
        fadeIn(cardContainer);
        cardContainer.setVisible(true);

        // Move and resize the main content to make space for the cardContainer
        TranslateTransition moveContent = new TranslateTransition(Duration.millis(500), mainContentPane);
        moveContent.setFromX(0);  // Initially no offset (left-aligned)
        moveContent.setToX(334);  // Shift right by the width of the cardContainer
        moveContent.play();

        isCardContainerVisible = true;
    }

    private void hideCardContainer() {
        // Fade out the cardContainer
        fadeOut(cardContainer);

        // Move and resize the main content back to its original position (left-aligned)
        TranslateTransition moveContent = new TranslateTransition(Duration.millis(500), mainContentPane);
        moveContent.setFromX(334); // Shifted right
        moveContent.setToX(0);     // Move back to left-aligned
        moveContent.play();

        isCardContainerVisible = false;
    }

    private void fadeIn(AnchorPane node) {
        node.setVisible(true); // Make it visible before starting the fade-in
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), node);
        fadeIn.setFromValue(0.0); // Start from fully transparent
        fadeIn.setToValue(1.0);   // End at fully opaque
        fadeIn.play();
    }

    private void fadeOut(AnchorPane node) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), node);
        fadeOut.setFromValue(1.0); // Start fully opaque
        fadeOut.setToValue(0.0);   // End fully transparent
        fadeOut.setOnFinished(event -> node.setVisible(false)); // Hide after fading out
        fadeOut.play();
    }
}
