package com.vaka.practice.controller;

import com.vaka.practice.dao.JdbcEntityDao;
import com.vaka.practice.domain.Entity;
import com.vaka.practice.domain.EntityTable;
import com.vaka.practice.exception.EntityNotFoundException;
import com.vaka.practice.factory.ServiceFactory;
import com.vaka.practice.service.EntityService;
import com.vaka.practice.service.SimpleEntityService;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
public class MainController {
    @FXML
    private TableView<EntityTable> tableView;

    private ObservableList<EntityTable> obsList;

    @FXML
    private TableColumn<EntityTable, Integer> idColumn;

    @FXML
    private TableColumn<EntityTable, String> nameColumn;

    @FXML
    private TableColumn<EntityTable, String> descriptionColumn;

    @FXML
    private TableColumn<EntityTable, String> createdAtColumn;

    @FXML
    private TableColumn<EntityTable, String> updatedAtColumn;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnRefresh;

    private final EntityService entityService = ServiceFactory.getEntityService();

    @FXML
    private void clickAdd(ActionEvent event) {
        log.debug("Clicked add");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("CreateEntity.fxml"));
            Parent root = loader.load();

            CreateEntityController createEntityController = loader.getController();
            createEntityController.setMainController(this);

            Stage stage = new Stage();
            stage.setTitle("Create Entity");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void clickEdit(ActionEvent event) {
        log.debug("Clicked edit");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("EditEntity.fxml"));
            Parent root = loader.load();

            EditEntityController editEntityController = loader.getController();
            editEntityController.setMainController(this);

            var selectionModel = tableView.getSelectionModel();
            ReadOnlyObjectProperty<EntityTable> entityTableReadOnlyObjectProperty = selectionModel.selectedItemProperty();
            EntityTable entityTable = entityTableReadOnlyObjectProperty.get();
            Entity entity = entityService.findById(entityTable.getId());

            editEntityController.setTextFields(entity);

            Stage stage = new Stage();
            stage.setTitle("Update Entity");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void clickDelete(ActionEvent event) {
        log.debug("Clicked delete");
        var selectionModel = tableView.getSelectionModel();
        ReadOnlyObjectProperty<EntityTable> entityTableReadOnlyObjectProperty = selectionModel.selectedItemProperty();
        EntityTable entityTable = entityTableReadOnlyObjectProperty.get();
        log.info("Clicked on: {}", entityTable);
        try {
            entityService.delete(entityTable.getId());
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
        refreshTable();
    }

    @FXML
    private void clickRefresh(ActionEvent event) {
        log.debug("Clicked refresh");
        refreshTable();
    }

    public void refreshTable() {
        log.info("Table refreshed");
        List<Entity> all = entityService.findAll();
        obsList = FXCollections.observableArrayList(all.stream().map(EntityTable::fromEntity).toList());
        tableView.setItems(obsList);
    }

    @FXML
    void initialize() {
        List<Entity> all = entityService.findAll();
        obsList = FXCollections.observableArrayList(all.stream().map(EntityTable::fromEntity).toList());

        tableView.setItems(obsList);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));

        log.info("Initialized successfully");
    }
}
