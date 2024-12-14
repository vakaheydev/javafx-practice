package com.vaka.practice.controller;

import com.vaka.practice.domain.Entity;
import com.vaka.practice.domain.EntityTable;
import com.vaka.practice.exception.EntityNotFoundException;
import com.vaka.practice.factory.ServiceFactory;
import com.vaka.practice.service.EntityService;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class MainController {
    @FXML
    private TableView<EntityTable> tableView;

    @FXML
    private TextField textPage;

    @FXML
    private TextField textPageSize;

    @FXML
    private Label pageTotal;

    private int pageSize = 5;

    private int currentPage = 1;

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

    private int totalPages = 0;

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

            if (entityTable == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("You've chosen nothing!");
                alert.showAndWait();
                return;
            }

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

        if (entityTable == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("You've chosen nothing!");
            alert.showAndWait();
            return;
        }

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
        log.info("Table refreshed to page {}", currentPage);
        List<Entity> all = entityService.findAllWithPagination(currentPage, pageSize);
        obsList = FXCollections.observableArrayList(all.stream().map(EntityTable::fromEntity).toList());
        tableView.setItems(obsList);

        refreshPage();
    }

    public void refreshTable(int pageNumber) {
        log.info("Table refreshed to page {}", pageNumber);
        List<Entity> all = entityService.findAllWithPagination(pageNumber, pageSize);
        obsList = FXCollections.observableArrayList(all.stream().map(EntityTable::fromEntity).toList());
        tableView.setItems(obsList);

        currentPage = pageNumber;

        refreshPage();
    }

    private void refreshPage() {
        int totalEntities = entityService.count();
        totalPages = (int) Math.ceil((double) totalEntities / pageSize);
        pageTotal.setText("/" + totalPages);
    }

    @FXML
    public void pageTyped(KeyEvent event) {
        String text = textPage.getText();

        if (text == null || text.isEmpty()) {
            return;
        }

        try {
            int pageNumber = Integer.parseInt(text);

            if (totalPages == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("There is no data. Please, add something");
                alert.showAndWait();
                textPage.clear();
                return;
            }

            if (pageNumber <= 0 || pageNumber > totalPages) {
                throw new NumberFormatException();
            }

            log.debug("Page number: {}", pageNumber);
            refreshTable(pageNumber);
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation error");
            alert.setHeaderText(String.format("Number of page should be a positive integer in range[%d - %d]", 1, totalPages));
            alert.showAndWait();
            textPage.clear();
        }
    }

    @FXML
    public void pageSizeTyped(KeyEvent event) {
        String text = textPageSize.getText();

        if (text == null || text.isEmpty()) {
            return;
        }

        try {
            int pages = Integer.parseInt(text);

            if (totalPages == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("There is no data. Please, add something");
                alert.showAndWait();
                textPage.clear();
                return;
            }

            if (pages <= 0) {
                throw new NumberFormatException();
            }

            log.debug("Page size: {}", pages);
            this.pageSize = pages;
            refreshTable(currentPage);
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation error");
            alert.setHeaderText(String.format("Number of page should be a positive integer in range[%d - %d]", 1, totalPages));
            alert.showAndWait();
            textPage.clear();
        }
    }

    @FXML
    void initialize() {
        dbInit();
        refreshTable(currentPage);

        textPage.setText(String.valueOf(1));
        textPage.setFocusTraversable(false);
        textPageSize.setText(String.valueOf(5));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));

        log.info("Initialized successfully");
    }

    private void dbInit() {
        Entity entity = new Entity("CoolName", "Very cool description");
        for (int i = 0; i < 5; i++) {
            entityService.save(entity);
        }
    }
}
