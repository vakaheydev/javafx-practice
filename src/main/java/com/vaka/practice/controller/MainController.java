package com.vaka.practice.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML
    private TextField textField;

    @FXML
    private ListView<String> listView;

    private ObservableList<String> obsList = FXCollections.observableArrayList();

    @FXML
    private Button btn;

    @FXML
    private void click(ActionEvent event) {
        System.out.println("Clicked!");
        obsList.add(textField.getText());
    }

    public void setListView(){
        List<String> stringList = new ArrayList<>();
        stringList.add("String 1");
        stringList.add("String 2");
        stringList.add("String 3");
        stringList.add("String 4");

        obsList.setAll(stringList);
        listView.setItems(obsList);
//        listView.setCellFactory(
//                new Callback<>() {
//                    @Override
//                    public ListCell<String> call(ListView<String> listView) {
//                        return new ListCell<>();
//                    }
//                });
    }

    @FXML
    void initialize() {
        setListView();
        var selectionModel = listView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldV, String newV) {
                System.out.println("Selected: " + newV);
            }
        });
    }
}
