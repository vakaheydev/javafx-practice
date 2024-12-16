package com.vaka.practice.controller;

import com.vaka.practice.domain.Entity;
import com.vaka.practice.factory.ServiceFactory;
import com.vaka.practice.service.EntityService;
import jakarta.validation.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import static com.vaka.practice.util.AlertUtil.errorAlert;
import static com.vaka.practice.util.AlertUtil.infoAlert;

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
            infoAlert("Saved successfully!");
            closeWindow();
            mainController.refreshTable();
        } catch (ValidationException ex) {
            errorAlert(ex.getMessage());
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
