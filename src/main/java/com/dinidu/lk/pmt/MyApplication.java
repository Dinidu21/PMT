package com.dinidu.lk.pmt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;

public class MyApplication extends Application {
    @Override
    public void start(Stage stage) {
        try{
            Parent root = FXMLLoader.load(this.getClass().getResource(""));
            Scene scene = new Scene(root);
            stage.setTitle("Project Management System");
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,"Input/Output Error "+ e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}