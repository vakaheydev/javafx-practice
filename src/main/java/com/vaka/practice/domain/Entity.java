package com.vaka.practice.domain;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entity {
    public Entity(String name, String description, LocalDate createdAt, LocalDate updatedAt) {
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Entity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    private Integer id;

    @Size(min = 3, max = 50)
    private String name;

    @Size(max = 255)
    private String description;

    private LocalDate createdAt;

    private LocalDate updatedAt;
}
