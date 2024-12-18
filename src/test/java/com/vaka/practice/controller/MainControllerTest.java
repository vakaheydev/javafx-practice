package com.vaka.practice.controller;

import com.vaka.practice.domain.EntityTable;
import com.vaka.practice.exception.EntityNotFoundException;
import com.vaka.practice.factory.ServiceFactory;
import com.vaka.practice.service.EntityService;
import com.vaka.practice.util.TestsUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;

import java.time.LocalDate;

import static javafx.scene.input.KeyCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@Slf4j
public class MainControllerTest extends ApplicationTest {
    private static EntityService service;
    private MainController mainController;

    @BeforeAll
    public static void staticSetup() {
        service = ServiceFactory.getEntityService();
    }

    @AfterAll
    public static void destroy() {
        TestsUtil.clearDb();
    }

    @BeforeEach
    public void setup() {
        TestsUtil.clearDb();
        service.init();
        interact(() -> mainController.refreshTable());
        waitForFxEvents();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Main.fxml"));
        Parent root = loader.load();

        mainController = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();

        waitForFxEvents();
        sleep(1500);
    }

    @DisplayName("Should have default value")
    @Test
    public void testPageTextField() {
        TextField pageTextField = lookup("#textPage").query();
        String text = pageTextField.getText();

        TableView<?> tableView = lookup("#tableView").queryTableView();
        assertEquals(5, tableView.getItems().size());
        assertEquals("1", text);
    }

    @DisplayName("Should open new window when add button clicked")
    @Test
    public void testCreateEntityWindow() {
        Button btn = lookup("#btnAdd").queryButton();
        clickOn(btn);

        waitForFxEvents();

        TextField nameField = lookup("#name").query();
        TextArea descriptionField = lookup("#description").query();
        Button btnSave = lookup("#btnSave").queryButton();

        assertNotNull(nameField);
        assertNotNull(descriptionField);
        assertNotNull(btnSave);
        assertEquals("", nameField.getText());
        assertEquals("", descriptionField.getText());
    }

    @DisplayName("Should not open new window when edit button clicked")
    @Test
    public void testNotOpenedEditEntityWindow() {
        Button btn = lookup("#btnEdit").queryButton();
        clickOn(btn);

        waitForFxEvents();

        DialogPane alert = lookup(".alert").query();
        String headerText = alert.getHeaderText();

        assertEquals("You've chosen nothing!", headerText);
    }

    @DisplayName("Should open new window when edit button clicked")
    @Test
    public void testEditEntityWindow() {
        mainController.refreshTable();
        Button btn = lookup("#btnEdit").queryButton();

        waitForFxEvents();

        TableView<?> tableView = lookup("#tableView").queryTableView();

        interact(() -> {
            tableView.getSelectionModel().selectFirst();
        });

        clickOn(btn);
        waitForFxEvents();

        TextField idField = lookup("#id").query();
        TextField nameField = lookup("#name").query();
        TextArea descriptionField = lookup("#description").query();
        TextField createdAtField = lookup("#createdAt").query();
        TextField updatedAtField = lookup("#updatedAt").query();

        assertNotNull(idField);
        assertNotNull(nameField);
        assertNotNull(descriptionField);
        assertNotNull(createdAtField);
        assertNotNull(updatedAtField);

        assertEquals("1", idField.getText());
        assertEquals("Ivan", nameField.getText());
        assertEquals("ITHub student", descriptionField.getText());
        assertEquals(LocalDate.now().toString(), createdAtField.getText());
        assertEquals(LocalDate.now().toString(), updatedAtField.getText());
    }

    @DisplayName("Should delete entity when delete button clicked")
    @Test
    public void testDeleteClicked() {
        Button btn = lookup("#btnDelete").queryButton();

        waitForFxEvents();

        TableView<?> tableView = lookup("#tableView").queryTableView();

        interact(() -> {
            tableView.getSelectionModel().selectFirst();
        });

        clickOn(btn);
        waitForFxEvents();

        assertThrows(EntityNotFoundException.class, () -> service.findById(1));
    }

    @DisplayName("Should not open new window when delete button clicked")
    @Test
    public void testNotOpenedDeleteEntityWindow() {
        Button btn = lookup("#btnDelete").queryButton();
        clickOn(btn);

        waitForFxEvents();

        DialogPane alert = lookup(".alert").query();
        String headerText = alert.getHeaderText();

        assertEquals("You've chosen nothing!", headerText);
    }

    @DisplayName("Should filter by name")
    @Test
    public void testFilterByName() {
        CheckBox checkBox = lookup("#checkFilterNameStarts").query();
        TextField textField = lookup("#textFilterNameStarts").query();

        textField.setText("Ivan");

        clickOn(checkBox);
        waitForFxEvents();

        TableView<?> tableView = lookup("#tableView").queryTableView();
        assertEquals(1, tableView.getItems().size());
    }

    @DisplayName("Should filter by description")
    @Test
    public void testFilterByDescription() {
        sleep(500);
        CheckBox checkBox = lookup("#checkFilterDescriptionStarts").query();
        TextField textField = lookup("#textFilterDescriptionStarts").query();

        textField.setText("IT");

        clickOn(checkBox);

        waitForFxEvents();

        TableView<?> tableView = lookup("#tableView").queryTableView();
        assertEquals(4, tableView.getItems().size());
    }

    @DisplayName("Should filter by id greater")
    @Test
    public void testFilterByIdGreater() {
        CheckBox checkBox = lookup("#checkFilterIdGreater").query();
        TextField textField = lookup("#textFilterIdGreater").query();

        textField.setText("4");

        clickOn(checkBox);
        waitForFxEvents();

        TableView<?> tableView = lookup("#tableView").queryTableView();
        assertEquals(2, tableView.getItems().size());
    }

    @DisplayName("Should filter by id less")
    @Test
    public void testFilterByIdLess() {
        CheckBox checkBox = lookup("#checkFilterIdLess").query();
        TextField textField = lookup("#textFilterIdLess").query();

        textField.setText("3");

        clickOn(checkBox);
        waitForFxEvents();

        TableView<?> tableView = lookup("#tableView").queryTableView();
        assertEquals(3, tableView.getItems().size());
    }

    @DisplayName("Should filter by name and description")
    @Test
    public void testFilterByNameAndDescription() {
        CheckBox nameCheckBox = lookup("#checkFilterNameStarts").query();
        TextField nameTextField = lookup("#textFilterNameStarts").query();
        CheckBox descCheckBox = lookup("#checkFilterDescriptionStarts").query();
        TextField descTextField = lookup("#textFilterDescriptionStarts").query();

        descTextField.setText("IT");
        nameTextField.setText("Ivan");

        clickOn(descCheckBox);
        waitForFxEvents();
        clickOn(nameCheckBox);
        waitForFxEvents();

        TableView<?> tableView = lookup("#tableView").queryTableView();
        assertEquals(1, tableView.getItems().size());
    }

    @DisplayName("Should filter by description and id greater")
    @Test
    public void testFilterByDescriptionAndIdGreater() {
        CheckBox idCheckBox = lookup("#checkFilterIdGreater").query();
        TextField idTextField = lookup("#textFilterIdGreater").query();
        CheckBox descCheckBox = lookup("#checkFilterDescriptionStarts").query();
        TextField descTextField = lookup("#textFilterDescriptionStarts").query();

        descTextField.setText("IT");
        idTextField.setText("4");

        clickOn(descCheckBox);
        waitForFxEvents();
        clickOn(idCheckBox);
        waitForFxEvents();

        TableView<?> tableView = lookup("#tableView").queryTableView();
        assertEquals(1, tableView.getItems().size());
    }

    @DisplayName("Should cancel filter")
    @Test
    public void testCancelFilter() {
        CheckBox idCheckBox = lookup("#checkFilterIdGreater").query();
        TextField idTextField = lookup("#textFilterIdGreater").query();
        CheckBox descCheckBox = lookup("#checkFilterDescriptionStarts").query();
        TextField descTextField = lookup("#textFilterDescriptionStarts").query();

        descTextField.setText("IT");
        idTextField.setText("4");

        clickOn(descCheckBox);
        waitForFxEvents();
        clickOn(idCheckBox);
        waitForFxEvents();

        TableView<?> tableView = lookup("#tableView").queryTableView();
        assertEquals(1, tableView.getItems().size());

        clickOn(descCheckBox);
        waitForFxEvents();
        assertEquals(2, tableView.getItems().size());

        clickOn(idCheckBox);
        waitForFxEvents();
        assertEquals(5, tableView.getItems().size());

        clickOn(idCheckBox);
        waitForFxEvents();
        assertEquals(2, tableView.getItems().size());
    }

    @DisplayName("Should search by name")
    @Test
    public void testSearchByName() {
        TextField searchField = lookup("#textSearchName").query();

        clickOn(searchField);
        waitForFxEvents();

        write("Ivan");

        waitForFxEvents();

        DialogPane alert = lookup(".alert").query();
        String headerText = alert.getHeaderText();

        Button button = lookup(".button").queryButton();

        clickOn(button);
        waitForFxEvents();

        assertEquals("Found 1 entities with specified name", headerText);

        TableView<EntityTable> tableView = lookup("#tableView").queryTableView();
        assertEquals(1, tableView.getItems().size());
        assertEquals("Ivan", tableView.getItems().get(0).getName());
        assertEquals(1, tableView.getItems().get(0).getId());
    }
}
