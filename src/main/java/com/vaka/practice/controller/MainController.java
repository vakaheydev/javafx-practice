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
import java.util.Optional;
import java.util.function.Predicate;

import static com.vaka.practice.util.AlertUtil.*;
import static javafx.scene.control.Alert.AlertType.*;

@Slf4j
public class MainController {
    // <--- Table --->

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
    private TableView<EntityTable> tableView;

    // <--- Text Inputs --->

    @FXML
    private TextField textPage;

    @FXML
    private TextField textPageSize;

    @FXML
    private TextField textSearchName;

    // <--- Filters --->

    @FXML
    private TextField textFilterNameStarts;

    @FXML
    private TextField textFilterDescriptionStarts;

    @FXML
    private TextField textFilterIdGreater;

    @FXML
    private TextField textFilterIdLess;

    @FXML
    private CheckBox checkFilterNameStarts;

    @FXML
    private CheckBox checkFilterDescriptionStarts;

    @FXML
    private CheckBox checkFilterIdGreater;

    @FXML
    private CheckBox checkFilterIdLess;

    @FXML
    private Label pageTotal;

    // <--- Buttons --->

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnRefresh;

    // <--- Fields --->

    private int pageSize = 5;

    private int currentPage = 1;

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
                warnAlert("You've chosen nothing!");
                return;
            }

            Entity entity = entityService.findById(entityTable.getId());
            editEntityController.setTextFields(entity);

            Stage stage = new Stage();
            stage.setTitle("Update Entity");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
        } catch (IOException | EntityNotFoundException e) {
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
            warnAlert("You've chosen nothing!");
            return;
        }

        log.info("Clicked on: {}", entityTable);

        Alert alert = new Alert(CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Do you really want to delete this entity?");

        Optional<ButtonType> option = alert.showAndWait();

        if (option.isEmpty() || option.get() == ButtonType.CANCEL) {
            return;
        } else if (option.get() == ButtonType.OK) {
            try {
                entityService.delete(entityTable.getId());
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }

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

        if (!isTextValid(text)) {
            return;
        }

        try {
            int pageNumber = Integer.parseInt(text);

            if (totalPages == 0) {
                errorAlert("There is no data. Please, add something");
                textPage.clear();
                return;
            }

            if (pageNumber <= 0 || pageNumber > totalPages) {
                throw new NumberFormatException();
            }

            log.debug("Page number: {}", pageNumber);
            refreshTable(pageNumber);
        } catch (NumberFormatException ex) {
            errorAlert(String.format("Number of page should be a positive integer in range[%d - %d]", 1, totalPages));
            textPage.clear();
        }
    }

    @FXML
    public void pageSizeTyped(KeyEvent event) {
        String text = textPageSize.getText();

        if (!isTextValid(text)) {
            return;
        }

        try {
            int pages = Integer.parseInt(text);

            if (totalPages == 0) {
                errorAlert("There is no data. Please, add something");
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
            errorAlert(String.format("Number of page should be a positive integer in range[%d - %d]", 1, totalPages));
            textPage.clear();
        }
    }

    @FXML
    public void searchNameTyped(KeyEvent event) {
        String name = textSearchName.getText();

        if (!isTextValid(name)) {
            refreshTable(currentPage);
            return;
        }

        List<Entity> byName = entityService.findByName(name);
        List<EntityTable> entityTable = byName.stream()
                .map(EntityTable::fromEntity)
                .toList();

        if (!entityTable.isEmpty()) {
            infoAlert(String.format("Found %d entities with specified name", entityTable.size()));
        }

        obsList = FXCollections.observableArrayList(entityTable);
        tableView.setItems(obsList);

    }

    private boolean isTextValid(String text) {
        return text != null && !text.isEmpty();
    }

    public void filterClicked() {
        boolean nameSelected = checkFilterNameStarts.isSelected();
        boolean descSelected = checkFilterDescriptionStarts.isSelected();
        boolean idGreaterSelected = checkFilterIdGreater.isSelected();
        boolean idLessSelected = checkFilterIdLess.isSelected();

        String nameText = textFilterNameStarts.getText();
        String descText = textFilterDescriptionStarts.getText();
        String idGreaterText = textFilterIdGreater.getText();
        String idLessText = textFilterIdLess.getText();

        if (nameSelected) {
            filterTableByPredicate(x -> x.getName().startsWith(nameText), nameText, nameSelected);
        }

        if (descSelected) {
            filterTableByPredicate(x -> x.getDescription().startsWith(descText), descText, descSelected);
        }

        if (idGreaterSelected) {
            int id = getIdFromText(idGreaterText);

            if (id == -1) {
                return;
            }

            filterTableByPredicate(x -> x.getId() >= id, idGreaterText, idGreaterSelected);
        }

        if (idLessSelected) {
            int id = getIdFromText(idLessText);

            if (id == -1) {
                return;
            }

            filterTableByPredicate(x -> x.getId() <= id, idLessText, idLessSelected);
        }

        if (!nameSelected && !descSelected && !idGreaterSelected && !idLessSelected) {
            refreshTable(currentPage);
        }
    }

    private int getIdFromText(String text) {
        int id;

        try {
            id = Integer.parseInt(text);

            if (id < 0) {
                throw new NumberFormatException();
            }
            return id;
        } catch (NumberFormatException ex) {
            errorAlert("ID should be an integer in range [0, inf]");
            return -1;
        }

    }

    private void filterTableByPredicate(Predicate<EntityTable> predicate, String text, boolean selected) {
        if (text == null || text.isEmpty()) {
            refreshTable(currentPage);
            return;
        }

        if (selected) {
            List<EntityTable> filtered = obsList.stream()
                    .filter(predicate)
                    .toList();

            obsList = FXCollections.observableArrayList(filtered);
            tableView.setItems(obsList);
        } else {
            refreshTable(currentPage);
        }
    }

    private void handleCheckBoxEvent(CheckBox checkBox) {
        if (!checkBox.isSelected()) {
            obsList = FXCollections.observableArrayList(
                    entityService.findAllWithPagination(currentPage, pageSize).stream()
                            .map(EntityTable::fromEntity)
                            .toList()
            );
        }

        filterClicked();
    }

    @FXML
    void initialize() {
        entityService.init();
        refreshTable(currentPage);

        textPage.setText(String.valueOf(1));
        textPage.setFocusTraversable(false);
        textPageSize.setText(String.valueOf(5));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));

        checkFilterNameStarts.setOnAction(event -> handleCheckBoxEvent(checkFilterNameStarts));
        checkFilterDescriptionStarts.setOnAction(event -> handleCheckBoxEvent(checkFilterDescriptionStarts));
        checkFilterIdGreater.setOnAction(event -> handleCheckBoxEvent(checkFilterIdGreater));
        checkFilterIdLess.setOnAction(event -> handleCheckBoxEvent(checkFilterIdLess));

        log.info("Initialized successfully");
    }
}
