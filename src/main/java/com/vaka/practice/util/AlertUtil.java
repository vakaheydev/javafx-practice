package com.vaka.practice.util;

import javafx.scene.control.Alert;

public class AlertUtil {
    private AlertUtil() {
    }

    ;

    public static void infoAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(text);
        alert.showAndWait();
    }

    public static void warnAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(text);
        alert.showAndWait();
    }

    public static void errorAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(text);
        alert.showAndWait();
    }
}
