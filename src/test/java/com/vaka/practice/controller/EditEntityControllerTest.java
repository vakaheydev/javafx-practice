package com.vaka.practice.controller;

import com.vaka.practice.domain.Entity;
import com.vaka.practice.exception.EntityNotFoundException;
import com.vaka.practice.factory.ServiceFactory;
import com.vaka.practice.service.EntityService;
import com.vaka.practice.util.TestsUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@Slf4j
public class EditEntityControllerTest extends ApplicationTest {
    private static EditEntityController controller;
    private static EntityService service;

    @BeforeAll
    public static void staticSetup() {
        service = ServiceFactory.getEntityService();
    }

    @BeforeEach
    public void setup() throws EntityNotFoundException {
        TestsUtil.clearDb();
        Entity entity = new Entity("NameToEdit", "DescToEdit");
        service.save(entity);

        controller.setTextFields(service.findById(1));
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader createEntityLoader = new FXMLLoader(getClass().getClassLoader().getResource("EditEntity.fxml"));
        Parent createRoot = createEntityLoader.load();

        controller = createEntityLoader.getController();

        FXMLLoader mainLoader = new FXMLLoader(getClass().getClassLoader().getResource("Main.fxml"));
        mainLoader.load();
        controller.setMainController(Objects.requireNonNull(mainLoader.getController()));

        stage.setScene(new Scene(createRoot));
        stage.show();

        sleep(1000);
    }

    @DisplayName("Should have default fields")
    @Test
    public void testShouldHaveTextFields() throws EntityNotFoundException {
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
        assertEquals("NameToEdit", nameField.getText());
        assertEquals("DescToEdit", descriptionField.getText());
        assertEquals(LocalDate.now().toString(), createdAtField.getText());
        assertEquals(LocalDate.now().toString(), updatedAtField.getText());
    }

    @DisplayName("Should save entity")
    @Test
    public void testShouldSaveEntity() throws EntityNotFoundException {
        TextField nameField = lookup("#name").query();
        TextArea descriptionField = lookup("#description").query();
        Button saveButton = lookup("#btnSave").query();

        nameField.setText("TestName");
        descriptionField.setText("TestDescription");

        clickOn(saveButton);

        Entity byId = service.findById(1);

        log.info(byId.toString());

        assertEquals(1, byId.getId());
        assertEquals("TestName", byId.getName());
        assertEquals("TestDescription", byId.getDescription());
        assertEquals(LocalDate.now(), byId.getCreatedAt());
        assertEquals(LocalDate.now(), byId.getUpdatedAt());
    }

    @DisplayName("Shouldn't save entity (ValidationException by name)")
    @Test
    public void testShouldNotSaveEntityByName() throws EntityNotFoundException {
        TextField nameField = lookup("#name").query();
        TextArea descriptionField = lookup("#description").query();
        Button saveButton = lookup("#btnSave").query();

        nameField.setText("X");
        descriptionField.setText("TestDescription");

        clickOn(saveButton);

        waitForFxEvents();
        DialogPane alert = lookup(".alert").query();

        String headerText = alert.getHeaderText();

        assertEquals("name: size must be between 3 and 50\n", headerText);

        log.info("Alert text: {}", headerText);

        Entity entity = service.findById(1);

        assertEquals("NameToEdit", entity.getName());
        assertEquals("DescToEdit", entity.getDescription());
    }

    @DisplayName("Shouldn't save entity (ValidationException by description)")
    @Test
    public void testShouldNotSaveEntityByDescription() throws EntityNotFoundException {
        TextField nameField = lookup("#name").query();
        TextArea descriptionField = lookup("#description").query();
        Button saveButton = lookup("#btnSave").query();

        nameField.setText("TestName");
        descriptionField.setText("TestDescription".repeat(100));

        clickOn(saveButton);

        waitForFxEvents();
        DialogPane alert = lookup(".alert").query();

        String headerText = alert.getHeaderText();

        assertEquals("description: size must be between 0 and 255\n", headerText);

        log.info("Alert text: {}", headerText);

        Entity entity = service.findById(1);

        assertEquals("NameToEdit", entity.getName());
        assertEquals("DescToEdit", entity.getDescription());
    }
}
