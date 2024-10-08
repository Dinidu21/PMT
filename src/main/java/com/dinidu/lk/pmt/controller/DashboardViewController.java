package com.dinidu.lk.pmt.controller;

import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;

public class DashboardViewController extends BaseController{

    public AnchorPane dashboardpg;

    public void logoutAction(ActionEvent actionEvent) {
        transitionToScene(dashboardpg,"/view/login-view.fxml");
    }
}
