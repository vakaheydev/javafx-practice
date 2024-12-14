package com.vaka.practice;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URI;
import java.util.Objects;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
//        Parent root = FXMLLoader.load(URI.create("Main.fxml").toURL());
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Main.fxml")));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Hello JavaFX");
        stage.setWidth(500);
        stage.setHeight(800);

        stage.show();
    }
}
