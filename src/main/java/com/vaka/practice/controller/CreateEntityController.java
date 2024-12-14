package com.vaka.practice.controller;

import com.vaka.practice.domain.Entity;
import com.vaka.practice.factory.ServiceFactory;
import com.vaka.practice.service.EntityService;
import jakarta.validation.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateEntityController {
    private MainController mainController;
    EntityService entityService = ServiceFactory.getEntityService();

    @FXML
    private TextField name;

    @FXML
    private TextArea description;

    @FXML
    public void clickSave(ActionEvent event) {
        Entity entity = new Entity(name.getText(), description.getText());
        log.info("Saved entity: {}", entity);
        try {
            entityService.save(entity);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validation error");
            alert.setHeaderText("Saved successfully!");
            alert.showAndWait();
            closeWindow();
            mainController.refreshTable();
        } catch (ValidationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation error");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void closeWindow() {
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }
}
