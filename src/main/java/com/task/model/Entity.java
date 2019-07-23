package com.task.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Entity {
    private UUID id;

    public Entity() {
        this.id = UUID.randomUUID();
    }
}
