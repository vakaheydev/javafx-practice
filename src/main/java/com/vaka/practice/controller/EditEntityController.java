package com.vaka.practice.controller;

import com.vaka.practice.domain.Entity;
import com.vaka.practice.exception.EntityNotFoundException;
import com.vaka.practice.factory.ServiceFactory;
import com.vaka.practice.service.EntityService;
import jakarta.validation.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

import static com.vaka.practice.util.AlertUtil.errorAlert;

public class EditEntityController {
    private MainController mainController;

    private EntityService entityService = ServiceFactory.getEntityService();

    @FXML
    private TextField id;

    @FXML
    private TextField name;

    @FXML
    private TextArea description;

    @FXML
    private TextField createdAt;

    @FXML
    private TextField updatedAt;

    @FXML
    Button btnSave;

    @FXML
    public void clickSave(ActionEvent event) {
        Entity entity = getEntityFromFields();

        try {
            entityService.update(entity.getId(), entity);
            mainController.refreshTable();
            closeWindow();
        } catch (EntityNotFoundException ex) {
            closeWindow();
        } catch (ValidationException ex) {
            errorAlert(ex.getMessage());
        }
    }

    public void setTextFields(Entity entity) {
        id.setText(String.valueOf(entity.getId()));
        name.setText(entity.getName());
        description.setText(entity.getDescription());
        createdAt.setText(entity.getCreatedAt().toString());
        updatedAt.setText(entity.getUpdatedAt().toString());
    }

    public Entity getEntityFromFields() {
        Integer eId = Integer.valueOf(id.getText());
        String eName = name.getText();
        String eDes = description.getText();
        String eCreatedAt = createdAt.getText();
        String eUpdatedAt = updatedAt.getText();

        return new Entity(eId, eName, eDes, LocalDate.parse(eCreatedAt), LocalDate.parse(eUpdatedAt));
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void closeWindow() {
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }
}
