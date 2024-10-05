package com.dinidu.lk.pmt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class MyApplication extends Application {
    private static double xOffset = 0;
    private static double yOffset = 0;
    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            Parent root = FXMLLoader.load(getClass().getResource("/view/loading-view.fxml"));
            root.setOnMousePressed(mouseEvent -> {
                xOffset = mouseEvent.getSceneX();
                yOffset = mouseEvent.getSceneY();
            });
            root.setOnMouseDragged(mouseEvent -> {
                primaryStage.setX(mouseEvent.getScreenX() - xOffset);
                primaryStage.setY(mouseEvent.getScreenY() - yOffset);
            });
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/asserts/icons/PN.png")));
            primaryStage.getIcons().add(icon);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.show();

            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                javafx.application.Platform.runLater(() -> {
                    try {
                        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/view/login-view.fxml"));
                        AnchorPane mainPane = mainLoader.load();
                        Scene mainScene = new Scene(mainPane);
                        Stage mainStage = new Stage();
                        mainStage.setTitle("Project Management System");
                        mainStage.setScene(mainScene);
                        Image icon2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/asserts/icons/PN.png")));
                        mainStage.getIcons().add(icon2);
                        mainStage.centerOnScreen();
                        mainStage.show();
                        primaryStage.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}