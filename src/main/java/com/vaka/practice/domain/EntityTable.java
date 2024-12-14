package com.vaka.practice.domain;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class EntityTable {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();

    private SimpleStringProperty name = new SimpleStringProperty();

    private SimpleStringProperty description = new SimpleStringProperty();

    private SimpleStringProperty createdAt = new SimpleStringProperty();

    private SimpleStringProperty updatedAt = new SimpleStringProperty();

    public EntityTable(int id, String name, String des, String createdDate, String updatedDate) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(des);
        this.createdAt = new SimpleStringProperty(createdDate);
        this.updatedAt = new SimpleStringProperty(updatedDate);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
         this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getCreatedAt() {
        return createdAt.get();
    }

    public SimpleStringProperty createdAtProperty() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt.set(createdAt);
    }

    public String getUpdatedAt() {
        return updatedAt.get();
    }

    public SimpleStringProperty updatedAtProperty() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt.set(updatedAt);
    }

    public static EntityTable fromEntity(Entity entity) {
        EntityTable entityTable = new EntityTable();
        entityTable.setId(entity.getId());
        entityTable.setName(entity.getName());
        entityTable.setDescription(entity.getDescription());
        entityTable.setCreatedAt(entity.getCreatedAt().toString());
        entityTable.setUpdatedAt(entity.getUpdatedAt().toString());

        return entityTable;
    }
}
